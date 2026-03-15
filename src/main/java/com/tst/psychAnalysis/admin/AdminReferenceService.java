package com.tst.psychAnalysis.admin;

import com.tst.psychAnalysis.assessment.Assessment;
import com.tst.psychAnalysis.assessment.AssessmentRepository;
import com.tst.psychAnalysis.assessment.BaiInterpretation;
import com.tst.psychAnalysis.assessment.BdiInterpretation;
import com.tst.psychAnalysis.assessment.NeoScaleInterpretation;
import com.tst.psychAnalysis.assessment.Norm;
import com.tst.psychAnalysis.assessment.NormRepository;
import com.tst.psychAnalysis.assessment.ResilienceInterpretation;
import com.tst.psychAnalysis.assessment.Scale;
import com.tst.psychAnalysis.assessment.ScaleRepository;
import com.tst.psychAnalysis.assessment.TciScaleInterpretation;
import com.tst.psychAnalysis.response.ScaleGroupDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 검사별 기준점수(규준) 및 해석 기준 문구를 조회합니다.
 */
@Service
public class AdminReferenceService {

    private final AssessmentRepository assessmentRepository;
    private final NormRepository normRepository;
    private final ScaleRepository scaleRepository;

    public AdminReferenceService(AssessmentRepository assessmentRepository,
                                 NormRepository normRepository,
                                 ScaleRepository scaleRepository) {
        this.assessmentRepository = assessmentRepository;
        this.normRepository = normRepository;
        this.scaleRepository = scaleRepository;
    }

    /**
     * 검사별 기준점수(규준) 및 해석 기준 목록.
     * T점수 기준이 동일한 간단/상세는 중복이므로 상세(이름에 " (상세)" 포함)는 제외.
     */
    public List<AssessmentReferenceDto> getReferenceData() {
        List<Assessment> assessments = assessmentRepository.findByIsActiveTrueOrderByIdAsc();
        List<AssessmentReferenceDto> result = new ArrayList<>();

        for (Assessment a : assessments) {
            String name = a.getName();
            if (name != null && name.contains(" (상세)")) {
                continue; // 기준·해석은 T점수 기준으로 간단과 동일하므로 상세는 표시하지 않음
            }
            List<AssessmentReferenceDto.NormRowDto> norms = buildNorms(a.getId());
            String interpretationGuide = getInterpretationGuide(name);
            List<ScaleGroupDto> scaleGroups = buildScaleGroupsIfNeo(name, a.getId());
            result.add(new AssessmentReferenceDto(name, norms, interpretationGuide, scaleGroups));
        }
        return result;
    }

    private List<AssessmentReferenceDto.NormRowDto> buildNorms(Long assessmentId) {
        List<Norm> normList = normRepository.findByAssessmentId(assessmentId);
        List<Scale> scales = scaleRepository.findByAssessmentIdOrderByIdAsc(assessmentId);
        // 척도가 하나뿐인 검사는 규준도 한 건만 노출(중복 제거)
        boolean singleScale = scales.size() <= 1;
        List<AssessmentReferenceDto.NormRowDto> rows = new ArrayList<>();
        for (Norm n : normList) {
            if (singleScale && n.getScale() != null) {
                continue; // 척도가 하나일 땐 TOTAL(scale_id null)만 사용
            }
            String code = n.getScale() != null ? n.getScale().getCode() : "TOTAL";
            String scaleName = n.getScale() != null ? n.getScale().getName() : "총점";
            rows.add(new AssessmentReferenceDto.NormRowDto(code, scaleName, n.getMean(), n.getSd()));
        }
        // 총점(TOTAL) 행은 맨 아래로
        rows.sort(Comparator.comparing((AssessmentReferenceDto.NormRowDto r) -> "TOTAL".equals(r.scaleCode()) ? 1 : 0));
        return rows;
    }

    private String getInterpretationGuide(String assessmentName) {
        if (assessmentName == null) return "";
        if (assessmentName.contains("BDI")) return BdiInterpretation.getReferenceDescription();
        if (assessmentName.contains("BAI")) return BaiInterpretation.getReferenceDescription();
        if (assessmentName.contains("TCI")) return TciScaleInterpretation.getReferenceDescription();
        if (assessmentName.contains("NEO")) return NeoScaleInterpretation.getReferenceDescription();
        if (assessmentName.contains("회복탄력성")) return ResilienceInterpretation.getReferenceDescription();
        return "(해석 기준 없음)";
    }

    /** NEO 검사일 때 주척도별 그룹(N·E·O·A·C + N1~C6) 반환 — 검사결과/기준점수 화면과 동일 */
    private List<ScaleGroupDto> buildScaleGroupsIfNeo(String assessmentName, Long assessmentId) {
        if (assessmentName == null || !assessmentName.contains("NEO")) return List.of();
        List<Scale> scales = scaleRepository.findByAssessmentIdOrderByIdAsc(assessmentId);
        if (scales.isEmpty()) return List.of();
        List<String> scaleOrder = scales.stream().map(Scale::getCode).toList();
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
}
