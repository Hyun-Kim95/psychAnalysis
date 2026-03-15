package com.tst.psychAnalysis.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tst.psychAnalysis.assessment.Item;
import com.tst.psychAnalysis.assessment.Norm;
import com.tst.psychAnalysis.assessment.NormRepository;
import com.tst.psychAnalysis.assessment.Scale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScoringService {

    private final ItemResponseRepository itemResponseRepository;
    private final NormRepository normRepository;
    private final ObjectMapper objectMapper;

    public ScoringService(ItemResponseRepository itemResponseRepository,
                          NormRepository normRepository,
                          ObjectMapper objectMapper) {
        this.itemResponseRepository = itemResponseRepository;
        this.normRepository = normRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public Result score(ResponseSession session) {
        List<ItemResponse> responses = itemResponseRepository.findAll().stream()
                .filter(r -> r.getResponseSession().getId().equals(session.getId()))
                .toList();

        Map<String, List<ItemResponse>> byScale = responses.stream()
                .collect(Collectors.groupingBy(r -> {
                    Item item = r.getItem();
                    Scale scale = item.getScale();
                    return scale.getCode();
                }));

        Map<String, Double> scaleRawScores = new HashMap<>();
        double totalRaw = 0.0;

        for (Map.Entry<String, List<ItemResponse>> entry : byScale.entrySet()) {
            String scaleCode = entry.getKey();
            List<ItemResponse> scaleResponses = entry.getValue();

            double sum = 0.0;
            for (ItemResponse ir : scaleResponses) {
                Item item = ir.getItem();
                int raw = ir.getRawValue();
                int scored = item.isReverseScored() ? 6 - raw : raw;
                double weighted = scored * item.getWeight();
                ir.setScoredValue((double) scored);
                ir.setWeightedScore(weighted);
                sum += weighted;
            }
            scaleRawScores.put(scaleCode, sum);
            totalRaw += sum;
        }

        itemResponseRepository.saveAll(responses);

        Map<String, Norm> normByScale = normRepository.findByAssessmentId(session.getAssessment().getId())
                .stream()
                .collect(Collectors.toMap(
                        n -> n.getScale() == null ? "TOTAL" : n.getScale().getCode(),
                        n -> n
                ));

        Map<String, Double> scaleTScores = new HashMap<>();
        for (Map.Entry<String, Double> e : scaleRawScores.entrySet()) {
            String scaleCode = e.getKey();
            double raw = e.getValue();
            Norm norm = normByScale.get(scaleCode);
            if (norm != null) {
                double t = 50.0 + 10.0 * (raw - norm.getMean()) / norm.getSd();
                scaleTScores.put(scaleCode, t);
            }
        }

        Double totalT = null;
        Norm totalNorm = normByScale.get("TOTAL");
        if (totalNorm != null) {
            totalT = 50.0 + 10.0 * (totalRaw - totalNorm.getMean()) / totalNorm.getSd();
            scaleTScores.put("TOTAL", totalT);
        }

        Result result = new Result();
        result.setId(UUID.randomUUID());
        result.setResponseSession(session);
        result.setTotalRawScore(totalRaw);
        result.setTotalTScore(totalT);
        result.setCreatedAt(java.time.LocalDateTime.now());

        try {
            result.setScaleRawScoresJson(objectMapper.writeValueAsString(scaleRawScores));
            result.setScaleTScoresJson(objectMapper.writeValueAsString(scaleTScores));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("결과를 직렬화하는 중 오류가 발생했습니다.", e);
        }

        return result;
    }
}

