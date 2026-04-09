package com.tst.psychAnalysis.admin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tst.psychAnalysis.assessment.LocalizationTexts;
import com.tst.psychAnalysis.assessment.NeoScaleGroupBuilder;
import com.tst.psychAnalysis.assessment.PdfLocaleStrings;
import com.tst.psychAnalysis.assessment.Scale;
import com.tst.psychAnalysis.assessment.ScaleRepository;
import com.tst.psychAnalysis.common.ApiResponse;
import com.tst.psychAnalysis.response.ResponseSessionRepository;
import com.tst.psychAnalysis.response.Result;
import com.tst.psychAnalysis.response.ResultPdfService;
import com.tst.psychAnalysis.response.ResultRepository;
import com.tst.psychAnalysis.response.ScaleGroupDto;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminAuthService authService;
    private final ResponseSessionRepository responseSessionRepository;
    private final ResultRepository resultRepository;
    private final AccessLogCountRepository accessLogCountRepository;
    private final ObjectMapper objectMapper;
    private final StatisticsService statisticsService;
    private final AdminReferenceService adminReferenceService;
    private final AdminReportPdfService adminReportPdfService;
    private final ScaleRepository scaleRepository;
    private final ResultPdfService resultPdfService;

    public AdminController(AdminAuthService authService,
                           ResponseSessionRepository responseSessionRepository,
                           ResultRepository resultRepository,
                           AccessLogCountRepository accessLogCountRepository,
                           ObjectMapper objectMapper,
                           StatisticsService statisticsService,
                           AdminReferenceService adminReferenceService,
                           AdminReportPdfService adminReportPdfService,
                           ScaleRepository scaleRepository,
                           ResultPdfService resultPdfService) {
        this.authService = authService;
        this.responseSessionRepository = responseSessionRepository;
        this.resultRepository = resultRepository;
        this.accessLogCountRepository = accessLogCountRepository;
        this.objectMapper = objectMapper;
        this.statisticsService = statisticsService;
        this.adminReferenceService = adminReferenceService;
        this.adminReportPdfService = adminReportPdfService;
        this.scaleRepository = scaleRepository;
        this.resultPdfService = resultPdfService;
    }

    @PostMapping("/login")
    public ApiResponse<AdminLoginResponse> login(@RequestBody AdminLoginRequest request) {
        String token = authService.login(request.loginId(), request.password());
        return ApiResponse.success(new AdminLoginResponse(token));
    }

    @GetMapping("/dashboard")
    public ApiResponse<Map<String, Object>> dashboard(@RequestHeader("X-Admin-Token") String token) {
        if (!authService.isValidToken(token)) {
            throw new IllegalArgumentException("인증이 필요합니다.");
        }

        long totalCompleted = responseSessionRepository.count();
        long totalResults = resultRepository.count();

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        long todayCompleted = responseSessionRepository.count(); // TODO: submittedAt 기준으로 필터링 가능

        Map<String, Object> body = Map.of(
                "totalCompleted", totalCompleted,
                "totalResults", totalResults,
                "todayCompleted", todayCompleted
        );

        return ApiResponse.success(body);
    }

    @GetMapping("/access-logs")
    public ApiResponse<List<AccessLogCountDto>> accessLogs(
            @RequestHeader("X-Admin-Token") String token,
            @RequestHeader(value = "Accept-Language", required = false) String language,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        if (!authService.isValidToken(token)) {
            throw new IllegalArgumentException("인증이 필요합니다.");
        }

        boolean english = LocalizationTexts.isEnglish(language);
        LocalDate fromDate = from != null ? from : LocalDate.now().minusDays(30);
        LocalDate toDate = to != null ? to : LocalDate.now();
        List<AccessLogCount> logs = accessLogCountRepository
                .findByLogDateBetweenOrderByLogDateDescClientIpMaskedAscEventTypeAsc(fromDate, toDate);

        List<AccessLogCountDto> body = logs.stream()
                .map(l -> new AccessLogCountDto(
                        l.getId(),
                        l.getClientIpMasked(),
                        l.getLogDate(),
                        LocalizationTexts.accessLogEventLabel(l.getEventType(), english),
                        l.getCount()
                ))
                .toList();

        return ApiResponse.success(body);
    }

    @GetMapping("/responses")
    public ApiResponse<List<AdminResponseSummary>> responses(
            @RequestHeader("X-Admin-Token") String token,
            @RequestHeader(value = "Accept-Language", required = false) String language
    ) {
        boolean english = LocalizationTexts.isEnglish(language);
        if (!authService.isValidToken(token)) {
            throw new IllegalArgumentException("인증이 필요합니다.");
        }

        List<AdminResponseSummary> summaries = resultRepository.findAllByOrderByResponseSession_SubmittedAtDesc().stream()
                .map(result -> {
                    var session = result.getResponseSession();
                    String assessmentName = session.getAssessment() != null ? session.getAssessment().getName() : null;
                    return new AdminResponseSummary(
                            session.getId(),
                            session.getSubmittedAt(),
                            session.getGroupCode(),
                            LocalizationTexts.assessmentName(assessmentName, english),
                            result.getTotalRawScore(),
                            result.getTotalTScore()
                    );
                })
                .toList();

        return ApiResponse.success(summaries);
    }

    @GetMapping("/responses/{sessionId}")
    public ApiResponse<AdminResponseDetail> responseDetail(
            @RequestHeader("X-Admin-Token") String token,
            @PathVariable("sessionId") UUID sessionId,
            @RequestHeader(value = "Accept-Language", required = false) String language
    ) {
        boolean english = LocalizationTexts.isEnglish(language);
        if (!authService.isValidToken(token)) {
            throw new IllegalArgumentException("인증이 필요합니다.");
        }

        var result = resultRepository.findAll().stream()
                .filter(r -> r.getResponseSession().getId().equals(sessionId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("응답을 찾을 수 없습니다."));

        var session = result.getResponseSession();
        String assessmentName = session.getAssessment() != null ? session.getAssessment().getName() : null;

        Map<String, Double> scaleRaw = readMap(result.getScaleRawScoresJson());
        Map<String, Double> scaleT = readMap(result.getScaleTScoresJson());

        List<String> scaleOrder = List.of();
        Map<String, String> scaleDisplayNames = Map.of();
        List<ScaleGroupDto> scaleGroups = List.of();
        var assessment = session.getAssessment();
        if (assessment != null) {
            List<Scale> scales = scaleRepository.findByAssessmentIdOrderByIdAsc(assessment.getId());
            if (!scales.isEmpty()) {
                scaleOrder = scales.stream().map(Scale::getCode).toList();
                scaleDisplayNames = scales.stream()
                        .collect(Collectors.toMap(
                                Scale::getCode,
                                s -> LocalizationTexts.scaleName(s.getCode(), s.getName() != null ? s.getName() : s.getCode(), english)
                        ));
                scaleGroups = NeoScaleGroupBuilder.buildGroupsIfNeo(assessmentName, scaleOrder, english);
            }
        }

        AdminResponseDetail detail = new AdminResponseDetail(
                session.getId(),
                result.getId(),
                session.getSubmittedAt(),
                session.getGroupCode(),
                LocalizationTexts.assessmentName(assessmentName, english),
                result.getTotalRawScore(),
                result.getTotalTScore(),
                scaleRaw,
                scaleT,
                scaleOrder,
                scaleDisplayNames,
                scaleGroups
        );

        return ApiResponse.success(detail);
    }

    /** 선택한 응답의 결과 PDF (검사 완료 후 다운로드하는 PDF와 동일한 배치) */
    @GetMapping("/results/{resultId}/pdf")
    public ResponseEntity<byte[]> getResponseResultPdf(
            @RequestHeader("X-Admin-Token") String token,
            @PathVariable("resultId") UUID resultId,
            @RequestHeader(value = "Accept-Language", required = false) String language,
            @RequestParam(value = "lang", required = false) String lang
    ) {
        if (!authService.isValidToken(token)) {
            throw new IllegalArgumentException("인증이 필요합니다.");
        }
        boolean english = LocalizationTexts.englishFromAcceptLanguageOrLangParam(language, lang);

        Result result = resultRepository.findById(resultId)
                .orElseThrow(() -> new IllegalArgumentException("결과를 찾을 수 없습니다."));
        byte[] pdfBytes = resultPdfService.generate(result, english);
        String assessmentName = result.getResponseSession().getAssessment() != null
                ? result.getResponseSession().getAssessment().getName()
                : null;
        LocalDateTime at = result.getCreatedAt() != null ? result.getCreatedAt() : LocalDateTime.now();
        String dateTime = at.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String localized = LocalizationTexts.assessmentName(assessmentName, english);
        PdfLocaleStrings pdf = PdfLocaleStrings.of(english);
        String base = pdf.buildResultPdfBaseFileName(localized);
        String fileName = base + "-" + dateTime + ".pdf";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentLength(pdfBytes.length);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }

    private Map<String, Double> readMap(String json) {
        if (json == null) {
            return Map.of();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Double>>() {});
        } catch (Exception e) {
            throw new IllegalStateException("결과를 읽는 중 오류가 발생했습니다.", e);
        }
    }

    @GetMapping("/stats/group-t-test")
    public ApiResponse<StatisticsService.GroupTTestResult> groupTTest(
            @RequestHeader("X-Admin-Token") String token,
            @RequestParam(name = "metric", required = false, defaultValue = "totalT") String metric
    ) {
        if (!authService.isValidToken(token)) {
            throw new IllegalArgumentException("인증이 필요합니다.");
        }

        var result = statisticsService.computeWelchTTest(metric);
        return ApiResponse.success(result);
    }

    @GetMapping("/stats/reliability")
    public ApiResponse<Map<String, Double>> reliability(
            @RequestHeader("X-Admin-Token") String token
    ) {
        if (!authService.isValidToken(token)) {
            throw new IllegalArgumentException("인증이 필요합니다.");
        }

        Map<String, Double> alphas = statisticsService.computeCronbachAlphaByScale();
        return ApiResponse.success(alphas);
    }

    @GetMapping("/reference")
    public ApiResponse<List<AssessmentReferenceDto>> reference(
            @RequestHeader("X-Admin-Token") String token,
            @RequestHeader(value = "Accept-Language", required = false) String language
    ) {
        boolean english = LocalizationTexts.isEnglish(language);
        if (!authService.isValidToken(token)) {
            throw new IllegalArgumentException("인증이 필요합니다.");
        }
        return ApiResponse.success(adminReferenceService.getReferenceData(english));
    }

    @PostMapping("/report/summary-pdf")
    public ResponseEntity<byte[]> reportSummaryPdf(
            @RequestHeader("X-Admin-Token") String token,
            @RequestHeader(value = "Accept-Language", required = false) String language,
            @RequestBody(required = false) AdminDashboardChartsPayload charts
    ) {
        if (!authService.isValidToken(token)) {
            throw new IllegalArgumentException("인증이 필요합니다.");
        }
        boolean english = LocalizationTexts.isEnglish(language);
        try {
            byte[] pdfBytes = adminReportPdfService.generateSummary(
                    charts != null ? charts : new AdminDashboardChartsPayload(), english);
            String fileName = "admin-report-summary-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".pdf";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentLength(pdfBytes.length);
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        } catch (Exception e) {
            throw new IllegalStateException("PDF 생성 중 오류가 발생했습니다.", e);
        }
    }

    @GetMapping("/report/reference-pdf")
    public ResponseEntity<byte[]> reportReferencePdf(
            @RequestHeader("X-Admin-Token") String token,
            @RequestHeader(value = "Accept-Language", required = false) String language
    ) {
        if (!authService.isValidToken(token)) {
            throw new IllegalArgumentException("인증이 필요합니다.");
        }
        boolean english = LocalizationTexts.isEnglish(language);
        try {
            byte[] pdfBytes = adminReportPdfService.generateReference(english);
            String fileName = "admin-report-reference-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".pdf";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentLength(pdfBytes.length);
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        } catch (Exception e) {
            throw new IllegalStateException("PDF 생성 중 오류가 발생했습니다.", e);
        }
    }
}

