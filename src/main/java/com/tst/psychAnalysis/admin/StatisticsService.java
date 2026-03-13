package com.tst.psychAnalysis.admin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;

    public StatisticsService(ResultRepository resultRepository,
                             ItemResponseRepository itemResponseRepository,
                             ObjectMapper objectMapper) {
        this.resultRepository = resultRepository;
        this.itemResponseRepository = itemResponseRepository;
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

    public Map<String, Double> computeCronbachAlphaByScale() {
        List<ItemResponse> responses = itemResponseRepository.findAll();

        Map<String, List<ItemResponse>> byScale = responses.stream()
                .filter(r -> r.getItem().getScale() != null)
                .collect(Collectors.groupingBy(r -> r.getItem().getScale().getCode()));

        Map<String, Double> result = new HashMap<>();
        for (Map.Entry<String, List<ItemResponse>> entry : byScale.entrySet()) {
            String scaleCode = entry.getKey();
            Double alpha = computeCronbachAlpha(entry.getValue());
            if (alpha != null) {
                result.put(scaleCode, alpha);
            }
        }
        return result;
    }

    private Double computeCronbachAlpha(List<ItemResponse> responses) {
        // group by session
        Map<UUID, List<ItemResponse>> bySession = responses.stream()
                .collect(Collectors.groupingBy(r -> r.getResponseSession().getId()));

        if (bySession.size() < 2) {
            return null;
        }

        // distinct items for this scale
        Set<Long> itemIds = responses.stream()
                .map(r -> r.getItem().getId())
                .collect(Collectors.toSet());

        int k = itemIds.size();
        if (k < 2) {
            return null;
        }

        // build per-item score vectors
        Map<Long, List<Double>> itemScores = new HashMap<>();
        List<Double> totalScores = new ArrayList<>();

        for (UUID sessionId : bySession.keySet()) {
            List<ItemResponse> sessionResponses = bySession.get(sessionId);
            Map<Long, Double> scoreByItem = sessionResponses.stream()
                    .filter(r -> r.getScoredValue() != null)
                    .collect(Collectors.toMap(r -> r.getItem().getId(), ItemResponse::getScoredValue));

            double total = 0.0;
            for (Long itemId : itemIds) {
                Double v = scoreByItem.get(itemId);
                if (v == null) {
                    return null;
                }
                total += v;
                itemScores.computeIfAbsent(itemId, id -> new ArrayList<>()).add(v);
            }
            totalScores.add(total);
        }

        if (totalScores.size() < 2) {
            return null;
        }

        double sumItemVars = 0.0;
        for (List<Double> scores : itemScores.values()) {
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

