package com.tst.psychAnalysis.response;

import com.tst.psychAnalysis.assessment.NeoScaleInterpretation;
import com.tst.psychAnalysis.assessment.Scale;
import com.tst.psychAnalysis.assessment.ScaleRepository;
import com.tst.psychAnalysis.assessment.ScaleInterpretationFacade;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tst.psychAnalysis.common.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/results")
public class ResultController {

    private final ResultRepository resultRepository;
    private final ScaleRepository scaleRepository;
    private final ObjectMapper objectMapper;
    private final ResultPdfService resultPdfService;

    public ResultController(ResultRepository resultRepository,
                            ScaleRepository scaleRepository,
                            ObjectMapper objectMapper,
                            ResultPdfService resultPdfService) {
        this.resultRepository = resultRepository;
        this.scaleRepository = scaleRepository;
        this.objectMapper = objectMapper;
        this.resultPdfService = resultPdfService;
    }

    @GetMapping("/{id}")
    public ApiResponse<ResultResponse> getResult(@PathVariable("id") UUID id) {
        Result result = resultRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("결과를 찾을 수 없습니다."));

        Long assessmentId = result.getResponseSession().getAssessment().getId();
        List<Scale> scales = scaleRepository.findByAssessmentIdOrderByIdAsc(assessmentId);
        List<String> scaleOrder = scales.stream().map(Scale::getCode).toList();
        Map<String, String> scaleDisplayNames = scales.stream()
                .collect(Collectors.toMap(Scale::getCode, Scale::getName, (a, b) -> a));

        Map<String, Double> scaleRawScores = readMap(result.getScaleRawScoresJson());
        Map<String, Double> scaleTScores = readMap(result.getScaleTScoresJson());

        String assessmentName = result.getResponseSession().getAssessment().getName();
        Map<String, String> interpretations = ScaleInterpretationFacade.interpret(
                assessmentName, result.getTotalRawScore(), scaleRawScores, scaleTScores);

        List<ScaleGroupDto> scaleGroups = buildScaleGroupsIfNeo(assessmentName, scaleOrder);

        ResultResponse body = new ResultResponse(
                assessmentName,
                result.getTotalRawScore(),
                result.getTotalTScore(),
                scaleRawScores,
                scaleTScores,
                scaleOrder,
                scaleDisplayNames,
                interpretations,
                scaleGroups
        );

        return ApiResponse.success(body);
    }

    /** NEO 검사이고 하위척도(N1, E1 등)가 있으면 주척도별 그룹 목록 반환 */
    private List<ScaleGroupDto> buildScaleGroupsIfNeo(String assessmentName, List<String> scaleOrder) {
        if (assessmentName == null || !assessmentName.contains("NEO") || scaleOrder == null || scaleOrder.isEmpty()) {
            return List.of();
        }
        boolean hasFacets = scaleOrder.stream().anyMatch(NeoScaleInterpretation.FACET_ORDER::contains);
        if (!hasFacets) return List.of();

        Map<String, List<String>> byFactor = new LinkedHashMap<>();
        for (String code : scaleOrder) {
            if (code == null || code.length() < 2) continue;
            String factor = code.substring(0, 1);
            if (NeoScaleInterpretation.MAIN_FACTOR_NAMES.containsKey(factor)) {
                byFactor.computeIfAbsent(factor, k -> new ArrayList<>()).add(code);
            }
        }
        List<ScaleGroupDto> groups = new ArrayList<>();
        for (String f : List.of("N", "E", "O", "A", "C")) {
            if (byFactor.containsKey(f)) {
                String label = NeoScaleInterpretation.MAIN_FACTOR_NAMES.get(f);
                groups.add(new ScaleGroupDto(f + " (" + label + ")", byFactor.get(f)));
            }
        }
        return groups;
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> getResultPdf(@PathVariable("id") UUID id) {
        Result result = resultRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("결과를 찾을 수 없습니다."));

        byte[] pdfBytes = resultPdfService.generate(result);

        String assessmentName = result.getResponseSession().getAssessment().getName();
        String fileName = buildPdfFileName(assessmentName, result);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentLength(pdfBytes.length);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    private static String buildPdfFileName(String assessmentName, Result result) {
        LocalDateTime at = result.getCreatedAt() != null ? result.getCreatedAt() : LocalDateTime.now();
        String dateTime = at.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String base = (assessmentName != null ? assessmentName.replace(" ", "") : "검사") + "결과";
        return base + "-" + dateTime + ".pdf";
    }

    private Map<String, Double> readMap(String json) {
        if (json == null) {
            return Map.of();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Double>>() {});
        } catch (IOException e) {
            throw new IllegalStateException("결과를 읽는 중 오류가 발생했습니다.", e);
        }
    }
}

