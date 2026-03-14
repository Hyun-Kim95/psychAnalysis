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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

    public List<AssessmentReferenceDto> getReferenceData() {
        List<Assessment> assessments = assessmentRepository.findByIsActiveTrueOrderByIdAsc();
        List<AssessmentReferenceDto> result = new ArrayList<>();

        for (Assessment a : assessments) {
            String name = a.getName();
            List<AssessmentReferenceDto.NormRowDto> norms = buildNorms(a.getId());
            String interpretationGuide = getInterpretationGuide(name);
            result.add(new AssessmentReferenceDto(name, norms, interpretationGuide));
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
}
