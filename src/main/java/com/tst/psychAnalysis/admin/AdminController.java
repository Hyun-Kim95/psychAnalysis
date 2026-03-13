package com.tst.psychAnalysis.admin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tst.psychAnalysis.common.ApiResponse;
import com.tst.psychAnalysis.response.ResponseSessionRepository;
import com.tst.psychAnalysis.response.ResultRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminAuthService authService;
    private final ResponseSessionRepository responseSessionRepository;
    private final ResultRepository resultRepository;
    private final AccessLogRepository accessLogRepository;
    private final ObjectMapper objectMapper;
    private final StatisticsService statisticsService;

    public AdminController(AdminAuthService authService,
                           ResponseSessionRepository responseSessionRepository,
                           ResultRepository resultRepository,
                           AccessLogRepository accessLogRepository,
                           ObjectMapper objectMapper,
                           StatisticsService statisticsService) {
        this.authService = authService;
        this.responseSessionRepository = responseSessionRepository;
        this.resultRepository = resultRepository;
        this.accessLogRepository = accessLogRepository;
        this.objectMapper = objectMapper;
        this.statisticsService = statisticsService;
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
    public ApiResponse<List<AccessLog>> accessLogs(
            @RequestHeader("X-Admin-Token") String token,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        if (!authService.isValidToken(token)) {
            throw new IllegalArgumentException("인증이 필요합니다.");
        }

        List<AccessLog> logs = accessLogRepository.findAll();

        return ApiResponse.success(logs);
    }

    @GetMapping("/responses")
    public ApiResponse<List<AdminResponseSummary>> responses(
            @RequestHeader("X-Admin-Token") String token
    ) {
        if (!authService.isValidToken(token)) {
            throw new IllegalArgumentException("인증이 필요합니다.");
        }

        List<AdminResponseSummary> summaries = resultRepository.findAll().stream()
                .map(result -> {
                    var session = result.getResponseSession();
                    return new AdminResponseSummary(
                            session.getId(),
                            session.getSubmittedAt(),
                            session.getGroupCode(),
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
            @PathVariable("sessionId") UUID sessionId
    ) {
        if (!authService.isValidToken(token)) {
            throw new IllegalArgumentException("인증이 필요합니다.");
        }

        var result = resultRepository.findAll().stream()
                .filter(r -> r.getResponseSession().getId().equals(sessionId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("응답을 찾을 수 없습니다."));

        var session = result.getResponseSession();

        Map<String, Double> scaleRaw = readMap(result.getScaleRawScoresJson());
        Map<String, Double> scaleT = readMap(result.getScaleTScoresJson());

        AdminResponseDetail detail = new AdminResponseDetail(
                session.getId(),
                session.getSubmittedAt(),
                session.getGroupCode(),
                result.getTotalRawScore(),
                result.getTotalTScore(),
                scaleRaw,
                scaleT
        );

        return ApiResponse.success(detail);
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
}

