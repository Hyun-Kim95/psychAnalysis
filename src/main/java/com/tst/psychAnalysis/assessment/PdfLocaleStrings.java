package com.tst.psychAnalysis.assessment;

import java.text.Normalizer;
import java.util.Locale;

/**
 * 결과 PDF·관리자 PDF에 쓰는 고정 문구(한/영).
 */
public final class PdfLocaleStrings {

    private static final PdfLocaleStrings KO = new PdfLocaleStrings(false);
    private static final PdfLocaleStrings EN = new PdfLocaleStrings(true);

    private final boolean english;

    private PdfLocaleStrings(boolean english) {
        this.english = english;
    }

    public static PdfLocaleStrings of(boolean english) {
        return english ? EN : KO;
    }

    public boolean isEnglish() {
        return english;
    }

    // --- Result PDF ---

    public String resultReportTitle(String localizedAssessmentName) {
        String base = localizedAssessmentName != null && !localizedAssessmentName.isBlank() ? localizedAssessmentName : unknownAssessment();
        return base + resultReportTitleSuffix();
    }

    public String unknownAssessment() {
        return english ? "Assessment" : "검사";
    }

    public String resultReportTitleSuffix() {
        return english ? " — Result Report" : " 결과 리포트";
    }

    public String formatTotalSummary(String rawPart, String tPart) {
        if (english) {
            return "Total raw: " + rawPart + " / Total T: " + tPart;
        }
        return "총점: " + rawPart + " / 총 T점수: " + tPart;
    }

    public String tScoreExplainerLine() {
        return english
                ? "T-scores: mean 50, SD 10. Near 50 = average; 60+ = relatively high; below 40 = relatively low."
                : "T점수: 평균 50·표준편차 10. 50 근처=평균, 60 이상=높은 편, 40 미만=낮은 편.";
    }

    public String tableHeaderScale() {
        return english ? "Scale" : "척도";
    }

    public String tableHeaderRaw() {
        return english ? "Raw" : "원점수";
    }

    public String tableHeaderT() {
        return english ? "T" : "T점수";
    }

    public String sectionScaleInterpretation() {
        return english ? "■ Scale interpretations" : "■ 척도별 해석";
    }

    public String sectionScaleInterpretationContinued() {
        return english ? "■ Scale interpretations (continued)" : "■ 척도별 해석 (계속)";
    }

    public String totalScoreTotalLabel() {
        return english ? "Total (TOTAL):" : "총점 (TOTAL):";
    }

    public String interpretationFootnote() {
        return english
                ? "The above text is for reference based on each test's scoring rules (total or T-score bands). "
                + "For counseling or clinical decisions, consult a qualified professional."
                : "위 해석은 검사별 기준(총점 또는 T점수 구간)에 따른 참고 설명입니다. 심리상담·임상적 판단이 필요할 경우 전문가와 상담하시기 바랍니다.";
    }

    public String sectionTScoreBarCharts() {
        return english ? "■ T-score bar chart" : "■ 척도별 T점수 그래프";
    }

    public String tScoreBarChartHint() {
        return english
                ? "Bar colors (T): blue / gray / orange (40 & 60 cutoffs). Vertical line at T=50. Raw scores are in the table below."
                : "막대 색(T): 파란·회색·주황(40/60 기준). 세로선 T=50. 원점수는 아래 표.";
    }

    public String panelTScoreTitle() {
        return english ? "T-score" : "T 점수";
    }

    public String panelRawScoreTitle() {
        return english ? "Raw score" : "원점수";
    }

    public String axisCaptionTScore(double axisMin, double axisMax) {
        if (english) {
            return String.format(Locale.US, "T-score (norm mean 50). Range %.0f – %.0f", axisMin, axisMax);
        }
        return String.format(Locale.KOREA, "T점수 (규준 평균 50). 표시 범위 %.0f ~ %.0f", axisMin, axisMax);
    }

    public String axisCaptionRaw(double axisMax) {
        if (english) {
            return String.format(Locale.US, "Raw score (0 – %.1f)", axisMax);
        }
        return String.format(Locale.KOREA, "원점수 (0 ~ %.1f)", axisMax);
    }

    public String pdfGenerationFailed() {
        return english ? "Failed to generate PDF." : "PDF 생성 중 오류가 발생했습니다.";
    }

    // --- Admin summary PDF ---

    public String adminReportTitle() {
        return english ? "Admin report" : "관리자 리포트";
    }

    public String adminDashboardSummary() {
        return english ? "■ Dashboard summary" : "■ 대시보드 요약";
    }

    public String[] adminSummaryHeaders() {
        return english
                ? new String[]{"Completed sessions", "Result reports", "Completed today"}
                : new String[]{"총 검사 완료 수", "총 결과 리포트 수", "오늘 검사 완료 수"};
    }

    public String adminResponsesByAssessment() {
        return english ? "■ Responses by assessment" : "■ 검사별 응답 수";
    }

    public String adminColAssessmentName() {
        return english ? "Assessment" : "검사명";
    }

    public String adminColResponseCount() {
        return english ? "Count" : "응답 수";
    }

    public String countSuffix(long n) {
        return english ? n + " cases" : n + "건";
    }

    public String adminAvgTotalByAssessment() {
        return english ? "■ Mean total score by assessment" : "■ 검사별 평균 총점";
    }

    public String adminColMeanTotal() {
        return english ? "Mean total" : "평균 총점";
    }

    public String adminReliabilityCronbach() {
        return english ? "■ Scale reliability (Cronbach α)" : "■ 척도별 신뢰도 (Cronbach α)";
    }

    public String adminTotalTScoreDistribution() {
        return english ? "■ Total T-score distribution" : "■ 총 T점수 분포";
    }

    public String adminSummaryFooterNote() {
        return english
                ? "This report summarizes dashboard metrics. For norms and interpretations, see the separate norms & interpretation report."
                : "이 리포트는 관리자용 대시보드 요약 정보입니다. 세부 규준 및 해석은 별도 \"기준·해석 리포트\"를 참고하세요.";
    }

    public String chartTitleResponsesByAssessment() {
        return english ? "Responses by assessment" : "검사별 응답 수";
    }

    public String chartTitleMeanTotal() {
        return english ? "Mean total score by assessment" : "검사별 평균 총점";
    }

    public String chartTitleReliability() {
        return english ? "Scale reliability (Cronbach α)" : "척도별 신뢰도 (Cronbach α)";
    }

    public String chartTitleTScoreDistribution() {
        return english ? "Total T-score distribution" : "총 T점수 분포";
    }

    public String unspecifiedName() {
        return english ? "(Unspecified)" : "(미지정)";
    }

    // --- Admin reference PDF ---

    public String adminNormsAndInterpretationTitle() {
        return english ? "Norms and interpretation by assessment" : "검사별 기준점수 및 해석";
    }

    public String[] adminTScoreIntroParagraph() {
        if (english) {
            return new String[]{
                    "What is a T-score? It standardizes raw scores with mean 50 and SD 10. When a raw score "
                            + "equals the norm mean, T=50. Scores of 60+ are relatively high; below 40 are relatively low. "
                            + "The means and SDs below are norm values used when interpreting raw scores as T-scores."
            };
        }
        return new String[]{
                "T점수란? 원점수를 표준화한 점수로 평균 50, 표준편차 10 기준입니다. 원점수가 규준 평균과 같을 때 T=50이며, "
                        + "60 이상은 높은 편, 40 미만은 낮은 편으로 해석합니다. 아래 평균·표준편차는 원점수를 T점수로 해석할 때 참조하는 규준 값입니다."
        };
    }

    public String adminNormSectionTitle() {
        return english ? "Norms" : "기준점수(규준)";
    }

    public String adminNormHeaderScale() {
        return english ? "Scale" : "척도";
    }

    public String adminNormHeaderMean() {
        return english ? "Mean" : "평균";
    }

    public String adminNormHeaderSd() {
        return english ? "SD" : "표준편차";
    }

    public String adminTotalScoreFallback() {
        return english ? "Total" : "총점";
    }

    public String adminInterpretationGuideTitle() {
        return english ? "Interpretation guide" : "해석 기준";
    }

    public String adminReferenceFooter() {
        return english
                ? "For administrator reference only. For counseling or clinical decisions, consult a qualified professional."
                : "위 내용은 관리자 참고용입니다. 심리상담·임상적 판단이 필요할 경우 전문가와 상담하시기 바랍니다.";
    }

    public String buildResultPdfBaseFileName(String localizedAssessmentName) {
        String base;
        if (localizedAssessmentName != null && !localizedAssessmentName.isBlank()) {
            base = english ? toAsciiFileStem(localizedAssessmentName) : localizedAssessmentName.replace(" ", "");
        } else {
            base = english ? "Assessment" : unknownAssessment();
        }
        return base + (english ? "-Result" : "결과");
    }

    /**
     * Content-Disposition·윈도우 호환을 위해 영문 PDF 파일명에 쓸 ASCII 토큰으로 만듭니다.
     */
    private static String toAsciiFileStem(String name) {
        String s = Normalizer.normalize(name, Normalizer.Form.NFD).replaceAll("\\p{M}+", "");
        s = s.replace("&", "and");
        s = s.replaceAll("[^a-zA-Z0-9._-]+", "-");
        s = s.replaceAll("-+", "-");
        s = s.replaceAll("^-|-$", "");
        return s.isEmpty() ? "Assessment" : s;
    }
}
