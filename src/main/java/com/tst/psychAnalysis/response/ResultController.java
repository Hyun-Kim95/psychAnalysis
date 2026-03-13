package com.tst.psychAnalysis.response;

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
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/results")
public class ResultController {

    private final ResultRepository resultRepository;
    private final ObjectMapper objectMapper;
    private final ResultPdfService resultPdfService;

    public ResultController(ResultRepository resultRepository,
                            ObjectMapper objectMapper,
                            ResultPdfService resultPdfService) {
        this.resultRepository = resultRepository;
        this.objectMapper = objectMapper;
        this.resultPdfService = resultPdfService;
    }

    @GetMapping("/{id}")
    public ApiResponse<ResultResponse> getResult(@PathVariable("id") UUID id) {
        Result result = resultRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("결과를 찾을 수 없습니다."));

        Map<String, Double> scaleRawScores = readMap(result.getScaleRawScoresJson());
        Map<String, Double> scaleTScores = readMap(result.getScaleTScoresJson());

        ResultResponse body = new ResultResponse(
                result.getTotalRawScore(),
                result.getTotalTScore(),
                scaleRawScores,
                scaleTScores
        );

        return ApiResponse.success(body);
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> getResultPdf(@PathVariable("id") UUID id) {
        Result result = resultRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("결과를 찾을 수 없습니다."));

        byte[] pdfBytes = resultPdfService.generate(result);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentLength(pdfBytes.length);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"result-" + id + ".pdf\"");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
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

