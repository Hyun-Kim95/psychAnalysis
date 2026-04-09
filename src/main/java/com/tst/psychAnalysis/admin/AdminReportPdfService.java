package com.tst.psychAnalysis.admin;

import com.tst.psychAnalysis.assessment.Assessment;
import com.tst.psychAnalysis.assessment.AssessmentRepository;
import com.tst.psychAnalysis.assessment.LocalizationTexts;
import com.tst.psychAnalysis.assessment.PdfLocaleStrings;
import com.tst.psychAnalysis.response.ResponseSessionRepository;
import com.tst.psychAnalysis.response.ResultRepository;
import com.tst.psychAnalysis.response.ScaleGroupDto;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class AdminReportPdfService {

    private final ResponseSessionRepository responseSessionRepository;
    private final ResultRepository resultRepository;
    private final AssessmentRepository assessmentRepository;
    private final AdminReferenceService adminReferenceService;
    private final StatisticsService statisticsService;

    public AdminReportPdfService(ResponseSessionRepository responseSessionRepository,
                                 ResultRepository resultRepository,
                                 AssessmentRepository assessmentRepository,
                                 AdminReferenceService adminReferenceService,
                                 StatisticsService statisticsService) {
        this.responseSessionRepository = responseSessionRepository;
        this.resultRepository = resultRepository;
        this.assessmentRepository = assessmentRepository;
        this.adminReferenceService = adminReferenceService;
        this.statisticsService = statisticsService;
    }

    /**
     * 대시보드 요약(총 검사 수/결과 수 등)과 통계 그래프 데이터를 포함한 관리자용 PDF.
     * charts에는 프론트에서 캡처한 차트 이미지(Data URL)가 포함될 수 있습니다.
     */
    public byte[] generateSummary(AdminDashboardChartsPayload charts, boolean english) throws Exception {
        PdfLocaleStrings pdf = PdfLocaleStrings.of(english);
        long totalCompleted = responseSessionRepository.count();
        long totalResults = resultRepository.count();
        // 오늘 완료 수 (submittedAt 기준)
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        long todayCompleted = responseSessionRepository.findAll().stream()
                .filter(s -> s.getSubmittedAt() != null
                        && !s.getSubmittedAt().isBefore(startOfDay)
                        && !s.getSubmittedAt().isAfter(endOfDay))
                .count();

        // 결과 목록을 기반으로 그래프에 쓰이는 집계 값 계산
        List<com.tst.psychAnalysis.response.Result> results = resultRepository.findAll();

        // 1) 검사별 응답 수 및 평균 총점 — 활성 검사 전부 포함(응답 0건인 검사도 표시)
        java.util.Map<String, long[]> byAssessment = new java.util.LinkedHashMap<>();
        for (Assessment a : assessmentRepository.findByIsActiveTrueOrderByIdAsc()) {
            String name = a.getName() != null ? a.getName() : "(미지정)";
            byAssessment.put(name, new long[]{0L, 0L});
        }
        for (com.tst.psychAnalysis.response.Result r : results) {
            String name = r.getResponseSession().getAssessment() != null
                    ? r.getResponseSession().getAssessment().getName()
                    : "(미지정)";
            long[] acc = byAssessment.computeIfAbsent(name, k -> new long[2]);
            acc[0]++;
            if (r.getTotalRawScore() != null) {
                acc[1] += Math.round(r.getTotalRawScore() * 100);
            }
        }

        // 2) 척도별 신뢰도 (Cronbach α)
        java.util.Map<String, Double> reliability = statisticsService.computeCronbachAlphaByScale();

        // 3) 총 T점수 분포 (히스토그램)
        java.util.List<Double> tScores = new java.util.ArrayList<>();
        for (com.tst.psychAnalysis.response.Result r : results) {
            if (r.getTotalTScore() != null && !Double.isNaN(r.getTotalTScore())) {
                tScores.add(r.getTotalTScore());
            }
        }
        int[] bins = new int[]{25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75};
        int[] hist = new int[bins.length];
        for (Double t : tScores) {
            double v = t;
            boolean placed = false;
            for (int i = 0; i < bins.length - 1; i++) {
                if (v >= bins[i] && v < bins[i + 1]) {
                    hist[i]++;
                    placed = true;
                    break;
                }
            }
            if (!placed && v >= bins[bins.length - 1]) {
                hist[bins.length - 1]++;
            }
        }

        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream out = new ByteArrayOutputStream();
             InputStream fontStream = Objects.requireNonNull(
                     getClass().getResourceAsStream("/fonts/kor-font.ttf"),
                     "fonts/kor-font.ttf not found on classpath"
             )) {

            PDFont font = PDType0Font.load(document, fontStream, true);
            float margin = 45;
            float lineHeight = 14;
            PDRectangle pageSize = PDRectangle.A4;
            float pageWidth = pageSize.getWidth();
            float pageHeight = pageSize.getHeight();
            float y = pageHeight - margin;

            PDPage page = new PDPage(pageSize);
            document.addPage(page);
            PDPageContentStream cs = new PDPageContentStream(document, page);

            try {
                cs.setNonStrokingColor(Color.BLACK);
                // 상단 제목에서 부제목( - 대시보드 요약)과 생성일시는 표시하지 않습니다.
                drawTextAt(cs, font, pdf.adminReportTitle(), margin, y, 18);
                y -= 28;

                // 1. 대시보드 요약 (테이블)
                drawTextAt(cs, font, pdf.adminDashboardSummary(), margin, y, 12);
                y -= lineHeight + 4;
                float[] summaryCols = {150, 150, 150};
                String[] summaryHeader = pdf.adminSummaryHeaders();
                String[] summaryRow = {
                        String.valueOf(totalCompleted),
                        String.valueOf(totalResults),
                        String.valueOf(todayCompleted)
                };
                java.util.List<String[]> summaryRows = new java.util.ArrayList<>();
                summaryRows.add(summaryRow);
                y = drawTable(cs, font, margin, y, 20, summaryCols, summaryHeader, summaryRows);
                y -= 12;

                // 2. 검사별 응답 수 (세로 방향: 검사명 | 응답 수, 긴 검사명은 줄바꿈)
                drawTextAt(cs, font, pdf.adminResponsesByAssessment(), margin, y, 12);
                y -= lineHeight + 4;
                int assessmentCount = byAssessment.size();
                if (assessmentCount > 0) {
                    float tableWidth = pageWidth - margin * 2;
                    float[] assessmentCols = {tableWidth * 0.8f, tableWidth * 0.2f};
                    String[] assessmentHeader = {pdf.adminColAssessmentName(), pdf.adminColResponseCount()};
                    java.util.List<String[]> assessmentRows = new java.util.ArrayList<>();
                    for (java.util.Map.Entry<String, long[]> e : byAssessment.entrySet()) {
                        String label = "(미지정)".equals(e.getKey())
                                ? pdf.unspecifiedName()
                                : LocalizationTexts.assessmentName(e.getKey(), english);
                        String nameWrapped = wrapToSingleString(label, 24);
                        assessmentRows.add(new String[]{nameWrapped, pdf.countSuffix(e.getValue()[0])});
                    }
                    y = drawTableWithMultilineCells(cs, font, margin, y, 22, 10, assessmentCols, assessmentHeader, assessmentRows);
                    y -= 12;
                }

                // 3. 검사별 평균 총점 (세로 방향: 검사명 | 평균 총점)
                drawTextAt(cs, font, pdf.adminAvgTotalByAssessment(), margin, y, 12);
                y -= lineHeight + 4;
                if (assessmentCount > 0) {
                    float tableWidth = pageWidth - margin * 2;
                    float[] assessmentCols2 = {tableWidth * 0.8f, tableWidth * 0.2f};
                    String[] assessmentHeader2 = {pdf.adminColAssessmentName(), pdf.adminColMeanTotal()};
                    java.util.List<String[]> assessmentAvgRows = new java.util.ArrayList<>();
                    java.util.Locale numLoc = english ? java.util.Locale.US : java.util.Locale.KOREA;
                    for (java.util.Map.Entry<String, long[]> e : byAssessment.entrySet()) {
                        long count = e.getValue()[0];
                        long sumTimes100 = e.getValue()[1];
                        String avgStr = count > 0
                                ? String.format(numLoc, "%.1f", (sumTimes100 / 100.0) / count)
                                : "-";
                        String disp = "(미지정)".equals(e.getKey())
                                ? pdf.unspecifiedName()
                                : LocalizationTexts.assessmentName(e.getKey(), english);
                        String nameWrapped = wrapToSingleString(disp, 24);
                        assessmentAvgRows.add(new String[]{nameWrapped, avgStr});
                    }
                    y = drawTableWithMultilineCells(cs, font, margin, y, 22, 10, assessmentCols2, assessmentHeader2, assessmentAvgRows);
                    y -= 12;
                }

                // 4. 척도별 신뢰도 (Cronbach α) (가로 방향 테이블)
                drawTextAt(cs, font, pdf.adminReliabilityCronbach(), margin, y, 12);
                y -= lineHeight + 4;
                int relCount = reliability.size();
                if (relCount > 0) {
                    float tableWidth = pageWidth - margin * 2;
                    float colWidth = tableWidth / relCount;
                    float[] relCols = new float[relCount];
                    for (int i = 0; i < relCount; i++) relCols[i] = colWidth;
                    String[] relHeader = new String[relCount];
                    String[] relValues = new String[relCount];
                    int idx = 0;
                    for (java.util.Map.Entry<String, Double> e : reliability.entrySet()) {
                        String alphaStr = e.getValue() != null
                                ? String.format(english ? java.util.Locale.US : java.util.Locale.KOREA, "%.2f", e.getValue())
                                : "-";
                        relHeader[idx] = e.getKey();
                        relValues[idx] = alphaStr;
                        idx++;
                    }
                    java.util.List<String[]> relRows = new java.util.ArrayList<>();
                    relRows.add(relValues);
                    y = drawTable(cs, font, margin, y, 18, relCols, relHeader, relRows);
                    y -= 12;
                }

                // 5. 총 T점수 분포 (가로 방향 테이블)
                drawTextAt(cs, font, pdf.adminTotalTScoreDistribution(), margin, y, 12);
                y -= lineHeight + 4;
                int binCount = bins.length;
                if (binCount > 0) {
                    float tableWidth = pageWidth - margin * 2;
                    float colWidth = tableWidth / binCount;
                    float[] histCols = new float[binCount];
                    for (int i = 0; i < binCount; i++) histCols[i] = colWidth;
                    String[] histHeader = new String[binCount];
                    String[] histValues = new String[binCount];
                    for (int i = 0; i < binCount; i++) {
                        String label;
                        if (i < bins.length - 1) {
                            label = bins[i] + "~" + bins[i + 1];
                        } else {
                            label = bins[i] + "+";
                        }
                        histHeader[i] = label;
                        histValues[i] = pdf.countSuffix(hist[i]);
                    }
                    java.util.List<String[]> histRows = new java.util.ArrayList<>();
                    histRows.add(histValues);
                    y = drawTable(cs, font, margin, y, 18, histCols, histHeader, histRows);
                    y -= 10;
                }

                cs.setNonStrokingColor(new Color(107, 114, 128));
                drawTextAt(cs, font, pdf.adminSummaryFooterNote(), margin, y, 8);
                y -= lineHeight + 6;

                // 6. (선택) 프론트에서 캡처한 차트 이미지 삽입 (2×2 배치, 공간 부족 시 다음 페이지로)
                if (charts != null) {
                    float chartWidth = (pageWidth - margin * 3) / 2f;
                    float chartHeight = 150;
                    float oneRowHeight = 16 + chartHeight + 14;
                    float minYForCharts = margin + oneRowHeight;
                    float xLeft = margin;
                    float xRight = margin + chartWidth + margin;

                    float rowTop = y;
                    if (rowTop < minYForCharts) {
                        cs.close();
                        page = new PDPage(PDRectangle.A4);
                        document.addPage(page);
                        cs = new PDPageContentStream(document, page);
                        y = pageHeight - margin;
                        cs.setNonStrokingColor(Color.BLACK);
                        rowTop = y;
                    }
                    float bottomLeft = drawChartImageIfPresent(document, cs, font, pdf.chartTitleResponsesByAssessment(), charts.getChartAssessment(), xLeft, rowTop, chartWidth, chartHeight);
                    float bottomRight = drawChartImageIfPresent(document, cs, font, pdf.chartTitleMeanTotal(), charts.getChartAvgScore(), xRight, rowTop, chartWidth, chartHeight);
                    float rowBottom = Math.min(bottomLeft, bottomRight);
                    y = rowBottom - 24;

                    rowTop = y;
                    if (rowTop < minYForCharts) {
                        cs.close();
                        page = new PDPage(PDRectangle.A4);
                        document.addPage(page);
                        cs = new PDPageContentStream(document, page);
                        y = pageHeight - margin;
                        cs.setNonStrokingColor(Color.BLACK);
                        rowTop = y;
                    }
                    bottomLeft = drawChartImageIfPresent(document, cs, font, pdf.chartTitleReliability(), charts.getChartReliability(), xLeft, rowTop, chartWidth, chartHeight);
                    bottomRight = drawChartImageIfPresent(document, cs, font, pdf.chartTitleTScoreDistribution(), charts.getChartTScore(), xRight, rowTop, chartWidth, chartHeight);
                    rowBottom = Math.min(bottomLeft, bottomRight);
                    y = rowBottom - 16;
                }

            } finally {
                cs.close();
            }

            document.save(out);
            return out.toByteArray();
        }
    }

    /**
     * 검사별 규준(평균·표준편차) 및 해석만 포함한 관리자용 PDF.
     */
    public byte[] generateReference(boolean english) throws Exception {
        PdfLocaleStrings pdf = PdfLocaleStrings.of(english);
        List<AssessmentReferenceDto> referenceData = adminReferenceService.getReferenceData(english);

        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream out = new ByteArrayOutputStream();
             InputStream fontStream = Objects.requireNonNull(
                     getClass().getResourceAsStream("/fonts/kor-font.ttf"),
                     "fonts/kor-font.ttf not found on classpath"
             )) {

            PDFont font = PDType0Font.load(document, fontStream, true);
            float margin = 45;
            float lineHeight = 14;
            PDRectangle pageSize = PDRectangle.A4;
            float pageWidth = pageSize.getWidth();
            float pageHeight = pageSize.getHeight();
            float y = pageHeight - margin;

            PDPage page = new PDPage(pageSize);
            document.addPage(page);
            PDPageContentStream cs = new PDPageContentStream(document, page);

            try {
                cs.setNonStrokingColor(Color.BLACK);
                // 헤더에서 "- 기준·해석"과 생성일시는 제거하고, 제목만 굵게(폰트 크기로 강조)
                drawTextAt(cs, font, pdf.adminReportTitle(), margin, y, 18);
                y -= 26;

                cs.setNonStrokingColor(new Color(55, 65, 81));
                drawTextAt(cs, font, pdf.adminNormsAndInterpretationTitle(), margin, y, 13);
                y -= lineHeight + 6;
                cs.setNonStrokingColor(new Color(100, 116, 139));
                for (String para : pdf.adminTScoreIntroParagraph()) {
                    for (String tDescLine : wrap(para, 48)) {
                        drawTextAt(cs, font, tDescLine, margin, y, 9);
                        y -= lineHeight - 1;
                    }
                }
                y -= 8;
                cs.setNonStrokingColor(Color.BLACK);

                for (AssessmentReferenceDto ref : referenceData) {
                    if (y < margin + 80) {
                        cs.close();
                        page = new PDPage(PDRectangle.A4);
                        document.addPage(page);
                        cs = new PDPageContentStream(document, page);
                        y = pageHeight - margin;
                        cs.setNonStrokingColor(Color.BLACK);
                    }

                    // 검사 제목 강조
                    drawTextAt(cs, font, "【" + ref.assessmentName() + "】", margin, y, 12);
                    y -= lineHeight + 2;

                    if (ref.norms() != null && !ref.norms().isEmpty()) {
                        cs.setNonStrokingColor(new Color(71, 85, 105));
                        drawTextAt(cs, font, pdf.adminNormSectionTitle(), margin + 8, y, 10);
                        y -= 8;
                        cs.setNonStrokingColor(Color.BLACK);
                        float totalWidth = pageWidth - margin * 2 - 16;
                        float[] normCols = {totalWidth * 0.5f, totalWidth * 0.25f, totalWidth * 0.25f};
                        String[] normHeader = {pdf.adminNormHeaderScale(), pdf.adminNormHeaderMean(), pdf.adminNormHeaderSd()};
                        List<String[]> normRows;
                        Set<Integer> groupHeaderIndices = new HashSet<>();
                        if (ref.scaleGroups() != null && !ref.scaleGroups().isEmpty()) {
                            normRows = new ArrayList<>();
                            int rowIndex = 0;
                            for (ScaleGroupDto group : ref.scaleGroups()) {
                                groupHeaderIndices.add(rowIndex);
                                normRows.add(new String[]{group.groupLabel(), "", ""});
                                rowIndex++;
                                for (String code : group.scaleCodes()) {
                                    AssessmentReferenceDto.NormRowDto n = findNormByCode(ref.norms(), code);
                                    if (n != null) {
                                        String label = n.scaleName() != null && !n.scaleName().isEmpty()
                                                ? n.scaleName() + " (" + n.scaleCode() + ")"
                                                : n.scaleCode();
                                        normRows.add(new String[]{label, nullSafe(n.mean()), nullSafe(n.sd())});
                                        rowIndex++;
                                    }
                                }
                            }
                            AssessmentReferenceDto.NormRowDto totalNorm = findNormByCode(ref.norms(), "TOTAL");
                            if (totalNorm != null) {
                                String label = totalNorm.scaleName() != null && !totalNorm.scaleName().isEmpty()
                                        ? totalNorm.scaleName() : pdf.adminTotalScoreFallback();
                                normRows.add(new String[]{label, nullSafe(totalNorm.mean()), nullSafe(totalNorm.sd())});
                            }
                            float normRowHeight = 18;
                            float minYRef = margin + 50;
                            int fromRow = 0;
                            while (fromRow < normRows.size()) {
                                int rowsFit = (int) ((y - minYRef) / normRowHeight) - 1;
                                if (rowsFit <= 0) {
                                    cs.close();
                                    page = new PDPage(PDRectangle.A4);
                                    document.addPage(page);
                                    cs = new PDPageContentStream(document, page);
                                    y = pageHeight - margin;
                                    cs.setNonStrokingColor(Color.BLACK);
                                    continue;
                                }
                                int toRow = Math.min(fromRow + rowsFit, normRows.size());
                                List<String[]> segment = normRows.subList(fromRow, toRow);
                                Set<Integer> segmentGroupIndices = new HashSet<>();
                                for (Integer idx : groupHeaderIndices) {
                                    if (idx >= fromRow && idx < toRow) segmentGroupIndices.add(idx - fromRow);
                                }
                                y = drawTableStyled(cs, font, margin + 8, y, normRowHeight, normCols, normHeader, segment, segmentGroupIndices);
                                fromRow = toRow;
                                if (fromRow < normRows.size()) {
                                    y -= 8;
                                    cs.close();
                                    page = new PDPage(PDRectangle.A4);
                                    document.addPage(page);
                                    cs = new PDPageContentStream(document, page);
                                    y = pageHeight - margin;
                                    cs.setNonStrokingColor(Color.BLACK);
                                }
                            }
                        } else {
                            normRows = new ArrayList<>();
                            for (AssessmentReferenceDto.NormRowDto n : ref.norms()) {
                                String label = n.scaleName() != null && !n.scaleName().isEmpty()
                                        ? n.scaleName() + " (" + n.scaleCode() + ")"
                                        : n.scaleCode();
                                normRows.add(new String[]{label, nullSafe(n.mean()), nullSafe(n.sd())});
                            }
                            y = drawTable(cs, font, margin + 8, y, 18, normCols, normHeader, normRows);
                        }
                        y -= 18;
                    }

                    if (ref.interpretationGuide() != null && !ref.interpretationGuide().isEmpty()) {
                        cs.setNonStrokingColor(new Color(71, 85, 105));
                        drawTextAt(cs, font, pdf.adminInterpretationGuideTitle(), margin + 8, y, 10);
                        y -= lineHeight;
                        cs.setNonStrokingColor(Color.BLACK);

                        // 해석 기준은 척도 제목(NS, HA 등)과 그 하위 낮음/평균/높음 설명을
                        // 하나의 묶음처럼 보이도록 그룹화하여 출력한다.
                        String[] rawLines = ref.interpretationGuide().split("\\r?\\n");
                        for (String raw : rawLines) {
                            String trimmed = raw.trim();
                            if (trimmed.isEmpty()) {
                                y -= 4;
                                continue;
                            }
                            // "NS (자극추구)", "HA (위험회피)" 같은 척도 제목 라인 감지
                            boolean isScaleHeader = trimmed.matches("^[A-Z]{1,3} \\(.*\\).*");
                            if (isScaleHeader) {
                                // 블록 간 여백을 조금 더 줘서 묶음 느낌을 강화
                                y -= 4;
                                if (y < margin + 40) {
                                    cs.close();
                                    page = new PDPage(pageSize);
                                    document.addPage(page);
                                    cs = new PDPageContentStream(document, page);
                                    y = pageHeight - margin;
                                    cs.setNonStrokingColor(Color.BLACK);
                                }
                                cs.setNonStrokingColor(new Color(30, 41, 59));
                                drawTextAt(cs, font, trimmed, margin + 8, y, 10);
                                cs.setNonStrokingColor(Color.BLACK);
                                y -= lineHeight;
                            } else {
                                // "낮음:", "평균:", "높음:" 설명 라인들
                                for (String line : wrap(trimmed, 48)) {
                                    if (y < margin + 40) {
                                        cs.close();
                                        page = new PDPage(pageSize);
                                        document.addPage(page);
                                        cs = new PDPageContentStream(document, page);
                                        y = pageHeight - margin;
                                        cs.setNonStrokingColor(Color.BLACK);
                                    }
                                    // 척도 제목보다 한 단계 더 들여쓰기
                                    drawTextAt(cs, font, line, margin + 16, y, 9);
                                    y -= lineHeight - 2;
                                }
                            }
                        }
                        y -= 10;
                    }
                }

                y -= 8;
                cs.setNonStrokingColor(new Color(107, 114, 128));
                drawTextAt(cs, font, pdf.adminReferenceFooter(), margin, y, 8);
            } finally {
                cs.close();
            }

            document.save(out);
            return out.toByteArray();
        }
    }

    private static String nullSafe(Double value) {
        return value == null ? "-" : String.format("%.1f", value);
    }

    private static PDImageXObject imageFromDataUrl(PDDocument document, String dataUrl) throws Exception {
        if (dataUrl == null || dataUrl.isBlank()) return null;
        try {
            int comma = dataUrl.indexOf(',');
            String base64 = comma >= 0 ? dataUrl.substring(comma + 1) : dataUrl;
            byte[] bytes = Base64.getDecoder().decode(base64);
            return PDImageXObject.createFromByteArray(document, bytes, null);
        } catch (IllegalArgumentException e) {
            // base64 디코딩 실패 시 이미지를 건너뛰기
            return null;
        }
    }

    private float drawChartImageIfPresent(PDDocument document,
                                          PDPageContentStream cs,
                                          PDFont font,
                                          String title,
                                          String dataUrl,
                                          float margin,
                                          float currentY,
                                          float chartWidth,
                                          float chartHeight) throws Exception {
        PDImageXObject img = imageFromDataUrl(document, dataUrl);
        if (img == null) {
            return currentY;
        }
        drawTextAt(cs, font, title, margin, currentY, 11);
        currentY -= 16;
        float x = margin;
        float yBottom = currentY - chartHeight;
        cs.drawImage(img, x, yBottom, chartWidth, chartHeight);
        currentY = yBottom - 14;
        return currentY;
    }

    private void drawTextAt(PDPageContentStream cs, PDFont font, String text, float x, float baselineY, float fontSize) throws Exception {
        cs.beginText();
        cs.setFont(font, fontSize);
        cs.newLineAtOffset(x, baselineY);
        if (text != null && !text.isEmpty()) {
            cs.showText(text);
        }
        cs.endText();
    }

    /**
     * 간단한 테이블을 그립니다. (헤더 1행 + 데이터 N행, 세로·가로 선 포함)
     *
     * @return 마지막 행 아래의 y 좌표
     */
    private float drawTable(PDPageContentStream cs,
                            PDFont font,
                            float startX,
                            float startY,
                            float rowHeight,
                            float[] colWidths,
                            String[] header,
                            java.util.List<String[]> rows) throws Exception {
        float y = startY;
        float tableWidth = 0;
        for (float w : colWidths) {
            tableWidth += w;
        }

        // 헤더 + 데이터 행 수
        int totalRows = 1 + rows.size();

        // 가로선
        float currentY = y;
        for (int i = 0; i <= totalRows; i++) {
            cs.moveTo(startX, currentY);
            cs.lineTo(startX + tableWidth, currentY);
            currentY -= rowHeight;
        }
        cs.stroke();

        // 세로선
        float currentX = startX;
        cs.moveTo(startX, y);
        cs.lineTo(startX, y - rowHeight * totalRows);
        for (float w : colWidths) {
            currentX += w;
            cs.moveTo(currentX, y);
            cs.lineTo(currentX, y - rowHeight * totalRows);
        }
        cs.stroke();

        // 헤더 텍스트
        currentX = startX;
        float textY = y - rowHeight + rowHeight * 0.3f;
        for (int i = 0; i < header.length; i++) {
            drawTextAt(cs, font, header[i], currentX + 4, textY, 9);
            currentX += colWidths[i];
        }

        // 데이터 행 텍스트
        currentY = y - rowHeight;
        for (String[] row : rows) {
            currentX = startX;
            textY = currentY - rowHeight + rowHeight * 0.3f;
            for (int i = 0; i < row.length && i < colWidths.length; i++) {
                drawTextAt(cs, font, row[i], currentX + 4, textY, 9);
                currentX += colWidths[i];
            }
            currentY -= rowHeight;
        }

        return y - rowHeight * totalRows;
    }

    /**
     * 그룹 헤더 행에 연한 남색 배경을 적용한 테이블 (NEO 기준점수 등).
     */
    private float drawTableStyled(PDPageContentStream cs, PDFont font,
                                  float startX, float startY, float rowHeight,
                                  float[] colWidths, String[] header,
                                  List<String[]> rows,
                                  Set<Integer> groupHeaderIndices) throws Exception {
        float tableWidth = 0;
        for (float w : colWidths) tableWidth += w;
        int totalRows = 1 + rows.size();
        float tableBottom = startY - rowHeight * totalRows;

        cs.setNonStrokingColor(new Color(243, 244, 246));
        cs.addRect(startX, startY - rowHeight, tableWidth, rowHeight);
        cs.fill();

        if (groupHeaderIndices != null && !groupHeaderIndices.isEmpty()) {
            cs.setNonStrokingColor(new Color(238, 242, 255));
            for (Integer idx : groupHeaderIndices) {
                if (idx < 0 || idx >= rows.size()) continue;
                float rowY = startY - rowHeight * (2 + idx);
                cs.addRect(startX, rowY, tableWidth, rowHeight);
                cs.fill();
            }
        }

        cs.setNonStrokingColor(Color.BLACK);

        float currentY = startY;
        for (int i = 0; i <= totalRows; i++) {
            cs.moveTo(startX, currentY);
            cs.lineTo(startX + tableWidth, currentY);
            currentY -= rowHeight;
        }
        float currentX = startX;
        cs.moveTo(startX, startY);
        cs.lineTo(startX, tableBottom);
        for (float w : colWidths) {
            currentX += w;
            cs.moveTo(currentX, startY);
            cs.lineTo(currentX, tableBottom);
        }
        cs.stroke();

        float textY = startY - rowHeight + rowHeight * 0.35f;
        float cellX = startX;
        for (int i = 0; i < header.length; i++) {
            drawTextAt(cs, font, header[i], cellX + 4, textY, 10);
            cellX += colWidths[i];
        }

        currentY = startY - rowHeight;
        for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
            String[] row = rows.get(rowIndex);
            textY = currentY - rowHeight + rowHeight * 0.35f;
            boolean isGroupHeader = groupHeaderIndices != null && groupHeaderIndices.contains(rowIndex);

            if (isGroupHeader && row.length > 0 && row[0] != null && !row[0].isEmpty()) {
                cs.setNonStrokingColor(new Color(55, 48, 163));
                drawTextAt(cs, font, row[0], startX + 6, textY, 10);
                cs.setNonStrokingColor(Color.BLACK);
            } else {
                float dataCellX = startX;
                for (int i = 0; i < row.length && i < colWidths.length; i++) {
                    drawTextAt(cs, font, row[i] != null ? row[i] : "", dataCellX + 4, textY, 9);
                    dataCellX += colWidths[i];
                }
            }
            currentY -= rowHeight;
        }
        return tableBottom;
    }

    private static AssessmentReferenceDto.NormRowDto findNormByCode(
            List<AssessmentReferenceDto.NormRowDto> norms, String code) {
        if (norms == null || code == null) return null;
        for (AssessmentReferenceDto.NormRowDto n : norms) {
            if (code.equals(n.scaleCode())) return n;
        }
        return null;
    }

    private static List<String> wrap(String text, int maxCharsPerLine) {
        if (text == null || text.isEmpty()) return List.of();
        List<String> lines = new java.util.ArrayList<>();
        String[] paragraphs = text.split("\\n");
        for (String p : paragraphs) {
            int len = p.length();
            for (int i = 0; i < len; i += maxCharsPerLine) {
                int end = Math.min(i + maxCharsPerLine, len);
                lines.add(p.substring(i, end));
            }
        }
        return lines;
    }

    /** 긴 텍스트를 maxCharsPerLine 단위로 줄바꿈한 한 문자열(\n 구분) */
    private static String wrapToSingleString(String text, int maxCharsPerLine) {
        if (text == null || text.isEmpty()) return "";
        return String.join("\n", wrap(text, maxCharsPerLine));
    }

    /**
     * 셀 내용에 \n이 있으면 여러 줄로 그리는 테이블. (검사명 등 긴 텍스트용)
     * @param cellLineHeight 셀 내 한 줄 높이
     */
    private float drawTableWithMultilineCells(PDPageContentStream cs, PDFont font,
                                              float startX, float startY, float rowHeight, float cellLineHeight,
                                              float[] colWidths, String[] header,
                                              java.util.List<String[]> rows) throws Exception {
        float tableWidth = 0;
        for (float w : colWidths) tableWidth += w;
        int totalRows = 1 + rows.size();
        float tableBottom = startY - rowHeight * totalRows;

        float currentY = startY;
        for (int i = 0; i <= totalRows; i++) {
            cs.moveTo(startX, currentY);
            cs.lineTo(startX + tableWidth, currentY);
            currentY -= rowHeight;
        }
        cs.stroke();
        float currentX = startX;
        cs.moveTo(startX, startY);
        cs.lineTo(startX, tableBottom);
        for (float w : colWidths) {
            currentX += w;
            cs.moveTo(currentX, startY);
            cs.lineTo(currentX, tableBottom);
        }
        cs.stroke();

        float textY = startY - rowHeight + cellLineHeight;
        float cellX = startX;
        for (int i = 0; i < header.length; i++) {
            drawTextAt(cs, font, header[i], cellX + 4, textY, 9);
            cellX += colWidths[i];
        }

        currentY = startY - rowHeight;
        for (String[] row : rows) {
            float rowTop = currentY - rowHeight;
            cellX = startX;
            for (int col = 0; col < row.length && col < colWidths.length; col++) {
                String cell = row[col] != null ? row[col] : "";
                String[] lines = cell.split("\\n", -1);
                float lineY = currentY - rowHeight + cellLineHeight;
                for (String line : lines) {
                    drawTextAt(cs, font, line, cellX + 4, lineY, 9);
                    lineY -= cellLineHeight;
                }
                cellX += colWidths[col];
            }
            currentY -= rowHeight;
        }
        return tableBottom;
    }
}
