package com.tst.psychAnalysis.admin;

import com.tst.psychAnalysis.assessment.Assessment;
import com.tst.psychAnalysis.assessment.AssessmentRepository;
import com.tst.psychAnalysis.assessment.LocalizationTexts;
import com.tst.psychAnalysis.assessment.NeoScaleGroupBuilder;
import com.tst.psychAnalysis.assessment.Norm;
import com.tst.psychAnalysis.assessment.NormRepository;
import com.tst.psychAnalysis.assessment.Scale;
import com.tst.psychAnalysis.assessment.ScaleRepository;
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
        return getReferenceData(false);
    }

    public List<AssessmentReferenceDto> getReferenceData(boolean english) {
        List<Assessment> assessments = assessmentRepository.findByIsActiveTrueOrderByIdAsc();
        List<AssessmentReferenceDto> result = new ArrayList<>();

        for (Assessment a : assessments) {
            String name = a.getName();
            if (name != null && name.contains(" (상세)")) {
                continue; // 기준·해석은 T점수 기준으로 간단과 동일하므로 상세는 표시하지 않음
            }
            List<AssessmentReferenceDto.NormRowDto> norms = buildNorms(a.getId(), english);
            String interpretationGuide = getInterpretationGuide(name, english);
            List<ScaleGroupDto> scaleGroups = buildNeoGroupsForAssessment(name, a.getId(), english);
            result.add(new AssessmentReferenceDto(LocalizationTexts.assessmentName(name, english), norms, interpretationGuide, scaleGroups));
        }
        return result;
    }

    private List<AssessmentReferenceDto.NormRowDto> buildNorms(Long assessmentId, boolean english) {
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
            String defaultName = n.getScale() != null ? n.getScale().getName() : "총점";
            String scaleName = LocalizationTexts.scaleName(code, defaultName, english);
            rows.add(new AssessmentReferenceDto.NormRowDto(code, scaleName, n.getMean(), n.getSd()));
        }
        // 총점(TOTAL) 행은 맨 아래로
        rows.sort(Comparator.comparing((AssessmentReferenceDto.NormRowDto r) -> "TOTAL".equals(r.scaleCode()) ? 1 : 0));
        return rows;
    }

    private String getInterpretationGuide(String assessmentName, boolean english) {
        return LocalizationTexts.referenceDescription(assessmentName, english);
    }

    /** NEO 검사일 때 주척도별 그룹(N·E·O·A·C + N1~C6) 반환 — 검사결과/기준점수 화면과 동일 */
    private List<ScaleGroupDto> buildNeoGroupsForAssessment(String assessmentName, Long assessmentId, boolean english) {
        if (assessmentName == null || !assessmentName.contains("NEO")) {
            return List.of();
        }
        List<Scale> scales = scaleRepository.findByAssessmentIdOrderByIdAsc(assessmentId);
        if (scales.isEmpty()) {
            return List.of();
        }
        List<String> scaleOrder = scales.stream().map(Scale::getCode).toList();
        return NeoScaleGroupBuilder.buildGroupsIfNeo(assessmentName, scaleOrder, english);
    }
}
