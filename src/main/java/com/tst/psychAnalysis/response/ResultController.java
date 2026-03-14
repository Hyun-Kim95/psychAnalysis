package com.tst.psychAnalysis.response;

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

        ResultResponse body = new ResultResponse(
                assessmentName,
                result.getTotalRawScore(),
                result.getTotalTScore(),
                scaleRawScores,
                scaleTScores,
                scaleOrder,
                scaleDisplayNames,
                interpretations
        );

        return ApiResponse.success(body);
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

