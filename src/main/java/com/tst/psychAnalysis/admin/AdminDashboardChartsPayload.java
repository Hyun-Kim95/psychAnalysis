package com.tst.psychAnalysis.admin;

/**
 * 관리자 대시보드에서 캡처한 차트 이미지를 담는 페이로드.
 * 각 필드는 data:image/png;base64,... 형식의 Data URL 문자열입니다.
 */
public class AdminDashboardChartsPayload {

    private String chartAssessment;
    private String chartAvgScore;
    private String chartDailySubmissions;
    private String chartTScore;

    public String getChartAssessment() {
        return chartAssessment;
    }

    public void setChartAssessment(String chartAssessment) {
        this.chartAssessment = chartAssessment;
    }

    public String getChartAvgScore() {
        return chartAvgScore;
    }

    public void setChartAvgScore(String chartAvgScore) {
        this.chartAvgScore = chartAvgScore;
    }

    public String getChartDailySubmissions() {
        return chartDailySubmissions;
    }

    public void setChartDailySubmissions(String chartDailySubmissions) {
        this.chartDailySubmissions = chartDailySubmissions;
    }

    public String getChartTScore() {
        return chartTScore;
    }

    public void setChartTScore(String chartTScore) {
        this.chartTScore = chartTScore;
    }
}

