package com.tst.psychAnalysis.admin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tst.psychAnalysis.assessment.Item;
import com.tst.psychAnalysis.assessment.ItemRepository;
import com.tst.psychAnalysis.response.ItemResponse;
import com.tst.psychAnalysis.response.ItemResponseRepository;
import com.tst.psychAnalysis.response.Result;
import com.tst.psychAnalysis.response.ResultRepository;
import org.apache.commons.math3.distribution.TDistribution;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private final ResultRepository resultRepository;
    private final ItemResponseRepository itemResponseRepository;
    private final ItemRepository itemRepository;
    private final ObjectMapper objectMapper;

    public StatisticsService(ResultRepository resultRepository,
                             ItemResponseRepository itemResponseRepository,
                             ItemRepository itemRepository,
                             ObjectMapper objectMapper) {
        this.resultRepository = resultRepository;
        this.itemResponseRepository = itemResponseRepository;
        this.itemRepository = itemRepository;
        this.objectMapper = objectMapper;
    }

    public GroupTTestResult computeWelchTTest(String metric) {
        List<Result> results = resultRepository.findAll();

        List<Double> groupA = new ArrayList<>();
        List<Double> groupB = new ArrayList<>();

        for (Result r : results) {
            String groupCode = r.getResponseSession().getGroupCode();
            if (groupCode == null) {
                continue;
            }
            Double value = extractMetric(r, metric);
            if (value == null) {
                continue;
            }
            if ("B".equalsIgnoreCase(groupCode)) {
                groupB.add(value);
            } else {
                groupA.add(value);
            }
        }

        if (groupA.size() < 2 || groupB.size() < 2) {
            return null;
        }

        double meanA = mean(groupA);
        double meanB = mean(groupB);
        double varA = variance(groupA, meanA);
        double varB = variance(groupB, meanB);
        int nA = groupA.size();
        int nB = groupB.size();

        double se = Math.sqrt(varA / nA + varB / nB);
        double t = (meanA - meanB) / se;

        double dfNumerator = Math.pow(varA / nA + varB / nB, 2);
        double dfDenominator = Math.pow(varA / nA, 2) / (nA - 1) + Math.pow(varB / nB, 2) / (nB - 1);
        double df = dfNumerator / dfDenominator;

        TDistribution tDist = new TDistribution(df);
        double p = 2.0 * (1.0 - tDist.cumulativeProbability(Math.abs(t)));

        return new GroupTTestResult(nA, nB, meanA, meanB, varA, varB, t, df, p);
    }

    private Double extractMetric(Result r, String metric) {
        if ("totalT".equalsIgnoreCase(metric) || metric == null) {
            return r.getTotalTScore();
        }
        // scale T score
        String json = r.getScaleTScoresJson();
        if (json == null) {
            return null;
        }
        try {
            Map<String, Double> map = objectMapper.readValue(json, new TypeReference<Map<String, Double>>() {});
            return map.get(metric);
        } catch (Exception e) {
            return null;
        }
    }

    private double mean(List<Double> values) {
        return values.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
    }

    private double variance(List<Double> values, double mean) {
        if (values.size() < 2) {
            return Double.NaN;
        }
        double sumSq = 0.0;
        for (Double v : values) {
            double d = v - mean;
            sumSq += d * d;
        }
        return sumSq / (values.size() - 1);
    }

    /**
     * 검사(assessment)별·척도별 Cronbach α.
     * <p>
     * 이전에는 척도 코드만으로 전역 묶음을 만들어, 검사가 둘 이상이면 문항 ID 집합이 섞여
     * 어떤 응시도 모든 문항을 채운 것으로 나오지 않아 항상 계산이 불가했음.
     */
    public Map<String, Double> computeCronbachAlphaByScale() {
        List<ItemResponse> responses = itemResponseRepository.findAll();

        Map<String, List<ItemResponse>> byAssessmentAndScale = responses.stream()
                .filter(r -> r.getItem().getScale() != null
                        && r.getResponseSession().getAssessment() != null)
                .collect(Collectors.groupingBy(r -> {
                    long aid = r.getResponseSession().getAssessment().getId();
                    String code = r.getItem().getScale().getCode();
                    return aid + "\u001e" + code;
                }));

        Map<String, Double> result = new HashMap<>();
        for (Map.Entry<String, List<ItemResponse>> entry : byAssessmentAndScale.entrySet()) {
            List<ItemResponse> group = entry.getValue();
            if (group.isEmpty()) {
                continue;
            }
            long assessmentId = group.get(0).getResponseSession().getAssessment().getId();
            String scaleCode = group.get(0).getItem().getScale().getCode();
            String assessmentName = group.get(0).getResponseSession().getAssessment().getName();
            if (assessmentName == null || assessmentName.isBlank()) {
                assessmentName = "assessment-" + assessmentId;
            }

            List<Long> expectedItemIds = itemRepository.findByAssessmentIdOrderBySortOrderAsc(assessmentId).stream()
                    .filter(it -> it.getScale() != null && scaleCode.equals(it.getScale().getCode()))
                    .map(Item::getId)
                    .toList();

            Double alpha = computeCronbachAlphaForAssessmentScale(group, expectedItemIds);
            if (alpha != null) {
                // 프론트에서 검사명·척도명 표시용 (이름에 \u001e 는 사실상 없음)
                String displayKey = assessmentName + "\u001e" + scaleCode;
                result.put(displayKey, alpha);
            }
        }
        return result;
    }

    /**
     * @param expectedItemIds 해당 검사·척도의 정의상 문항 ID 목록(정렬·누락 없이 완전 응답한 세션만 사용)
     */
    private Double computeCronbachAlphaForAssessmentScale(List<ItemResponse> responses,
                                                          List<Long> expectedItemIds) {
        int k = expectedItemIds.size();
        if (k < 2) {
            return null;
        }
        Set<Long> itemIdSet = new LinkedHashSet<>(expectedItemIds);

        Map<UUID, List<ItemResponse>> bySession = responses.stream()
                .collect(Collectors.groupingBy(r -> r.getResponseSession().getId()));

        Map<Long, List<Double>> itemScores = new HashMap<>();
        List<Double> totalScores = new ArrayList<>();

        for (Map.Entry<UUID, List<ItemResponse>> e : bySession.entrySet()) {
            List<ItemResponse> sessionResponses = e.getValue();
            Map<Long, Double> scoreByItem = sessionResponses.stream()
                    .filter(r -> r.getScoredValue() != null)
                    .collect(Collectors.toMap(r -> r.getItem().getId(), ItemResponse::getScoredValue, (a, b) -> a));

            boolean complete = itemIdSet.stream().allMatch(scoreByItem::containsKey);
            if (!complete) {
                continue;
            }

            double total = 0.0;
            for (Long itemId : expectedItemIds) {
                double v = scoreByItem.get(itemId);
                total += v;
                itemScores.computeIfAbsent(itemId, id -> new ArrayList<>()).add(v);
            }
            totalScores.add(total);
        }

        if (totalScores.size() < 2) {
            return null;
        }

        double sumItemVars = 0.0;
        for (Long itemId : expectedItemIds) {
            List<Double> scores = itemScores.get(itemId);
            if (scores == null || scores.size() < 2) {
                return null;
            }
            double m = mean(scores);
            sumItemVars += variance(scores, m);
        }

        double totalMean = mean(totalScores);
        double totalVar = variance(totalScores, totalMean);

        if (totalVar == 0.0) {
            return null;
        }

        return (k / (double) (k - 1)) * (1.0 - (sumItemVars / totalVar));
    }

    public record GroupTTestResult(
            int nA,
            int nB,
            double meanA,
            double meanB,
            double varA,
            double varB,
            double t,
            double df,
            double pValue
    ) {
    }
}

