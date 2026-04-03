package com.tst.psychAnalysis.response;

import com.tst.psychAnalysis.assessment.NeoScaleInterpretation;
import com.tst.psychAnalysis.assessment.Scale;
import com.tst.psychAnalysis.assessment.ScaleRepository;
import com.tst.psychAnalysis.assessment.ScaleInterpretationFacade;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.util.Matrix;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ResultPdfService {

    private final ObjectMapper objectMapper;
    private final ScaleRepository scaleRepository;

    public ResultPdfService(ObjectMapper objectMapper, ScaleRepository scaleRepository) {
        this.objectMapper = objectMapper;
        this.scaleRepository = scaleRepository;
    }

    public byte[] generate(Result result) {
        Long assessmentId = result.getResponseSession().getAssessment().getId();
        String assessmentName = result.getResponseSession().getAssessment().getName();
        List<Scale> scales = scaleRepository.findByAssessmentIdOrderByIdAsc(assessmentId);
        List<String> scaleOrder = scales.stream().map(Scale::getCode).toList();
        Map<String, String> displayNames = scales.stream()
                .collect(Collectors.toMap(Scale::getCode, Scale::getName, (a, b) -> a));

        Map<String, Double> scaleRaw = readMap(result.getScaleRawScoresJson());
        Map<String, Double> scaleT = readMap(result.getScaleTScoresJson());
        Map<String, String> interpretations = ScaleInterpretationFacade.interpret(
                assessmentName, result.getTotalRawScore(), scaleRaw, scaleT);

        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream out = new ByteArrayOutputStream();
             InputStream fontStream = Objects.requireNonNull(
                     getClass().getResourceAsStream("/fonts/kor-font.ttf"),
                     "fonts/kor-font.ttf not found on classpath"
             )) {

            PDFont font = PDType0Font.load(document, fontStream, true);

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDRectangle box = page.getMediaBox();
            float pageHeight = box.getHeight();
            float pageWidth = box.getWidth();
            float marginLeft = 40;
            float marginRight = 55;
            float margin = marginLeft;
            float minY = margin + 50f;  // 이 아래로 내려가면 새 페이지

            PDPageContentStream cs = new PDPageContentStream(document, page);
            float y = pageHeight - margin;
            PageHolder holder = null;

            try {
                // 제목
                cs.setNonStrokingColor(Color.BLACK);
                String title = (assessmentName != null ? assessmentName : "검사") + " 결과 리포트";
                drawTextAt(cs, font, title, margin, y, 16);
                y -= 28;

                // 총점 요약
                String summary = "총점: " + nullSafe(result.getTotalRawScore())
                        + " / 총 T점수: " + nullSafeOneDecimal(result.getTotalTScore());
                drawTextAt(cs, font, summary, margin, y, 12);
                y -= 20;

                cs.setNonStrokingColor(Color.BLACK);

                // T점수 그래프는 요약 직후·첫 페이지 상단에 두어 공간 확보 (NEO 등 척도 많을 때 행 높이 자동 축소)
                List<String> codesInOrder = buildCodesInOrder(assessmentName, scaleOrder, scaleRaw, scaleT);
                if (!codesInOrder.isEmpty()) {
                    y = drawScaleBarCharts(cs, font, marginLeft, marginRight, pageWidth, y, codesInOrder, displayNames, scaleT, minY);
                    y -= 10;
                }

                // T점수 설명 (그래프 아래 한 줄)
                cs.setNonStrokingColor(new Color(71, 85, 105));
                drawTextAt(cs, font, "T점수: 평균 50·표준편차 10. 50 근처=평균, 60 이상=높은 편, 40 미만=낮은 편.", margin, y, 9);
                y -= 14;
                cs.setNonStrokingColor(Color.BLACK);

                // 척도별 점수 테이블. NEO는 주척도별 그룹 헤더 행 + 스타일 적용
                float tableWidth = pageWidth - marginLeft - marginRight;
                float[] colWidths = { tableWidth * 0.55f, tableWidth * 0.22f, tableWidth * 0.23f };
                String[] headers = { "척도", "원점수", "T점수" };
                java.util.List<String[]> rows = new java.util.ArrayList<>();
                java.util.Set<Integer> groupHeaderIndices = new java.util.HashSet<>();
                List<ScaleGroupDto> scaleGroups = buildScaleGroupsIfNeo(assessmentName, scaleOrder);
                if (!scaleGroups.isEmpty()) {
                    for (ScaleGroupDto group : scaleGroups) {
                        groupHeaderIndices.add(rows.size());
                        rows.add(new String[]{ group.groupLabel(), "", "" });
                        for (String code : group.scaleCodes()) {
                            Double raw = scaleRaw.get(code);
                            Double t = scaleT.get(code);
                            if (raw == null && t == null) continue;
                            String label = displayNames.getOrDefault(code, code);
                            rows.add(new String[]{
                                    label + " (" + code + ")",
                                    raw != null ? nullSafeOneDecimal(raw) : "-",
                                    t != null ? nullSafeOneDecimal(t) : "-"
                            });
                        }
                    }
                } else {
                    for (String code : scaleOrder) {
                        Double raw = scaleRaw.get(code);
                        Double t = scaleT.get(code);
                        if (raw == null && t == null) continue;
                        String label = displayNames.getOrDefault(code, code);
                        rows.add(new String[]{
                                label + " (" + code + ")",
                                raw != null ? nullSafeOneDecimal(raw) : "-",
                                t != null ? nullSafeOneDecimal(t) : "-"
                        });
                    }
                }
                float rowHeight = 20;
                // NEO 등 테이블이 길면 페이지를 넘어 잘리지 않도록 나누어 그리기
                int fromRow = 0;
                while (fromRow < rows.size()) {
                    int rowsFit = (int) ((y - minY) / rowHeight) - 1;
                    if (rowsFit <= 0) {
                        cs.close();
                        page = new PDPage(PDRectangle.A4);
                        document.addPage(page);
                        cs = new PDPageContentStream(document, page);
                        y = pageHeight - margin;
                        cs.setNonStrokingColor(Color.BLACK);
                        continue;
                    }
                    int toRow = Math.min(fromRow + rowsFit, rows.size());
                    java.util.List<String[]> segment = rows.subList(fromRow, toRow);
                    java.util.Set<Integer> segmentGroupIndices = new java.util.HashSet<>();
                    for (Integer idx : groupHeaderIndices) {
                        if (idx >= fromRow && idx < toRow) segmentGroupIndices.add(idx - fromRow);
                    }
                    y = drawTableStyled(cs, font, margin, y, rowHeight, colWidths, headers, segment, segmentGroupIndices);
                    fromRow = toRow;
                    if (fromRow < rows.size()) {
                        y -= 8;
                        cs.close();
                        page = new PDPage(PDRectangle.A4);
                        document.addPage(page);
                        cs = new PDPageContentStream(document, page);
                        y = pageHeight - margin;
                        cs.setNonStrokingColor(Color.BLACK);
                    }
                }
                y -= 16;
                cs.setNonStrokingColor(new Color(55, 65, 81));
                drawTextAt(cs, font, "■ 척도별 해석", margin, y, 11);
                y -= 18;

                cs.setNonStrokingColor(Color.BLACK);
                float lineHeight = 14;

                holder = new PageHolder(cs, page, y);
                // 척도별 해석: 공간 부족 시 새 페이지 추가하며 모두 출력
                if (!scaleGroups.isEmpty()) {
                    for (ScaleGroupDto group : scaleGroups) {
                        holder.y = ensureSpace(document, font, margin, pageHeight, minY, holder, 20);
                        drawTextAt(holder.cs, font, "【" + group.groupLabel() + "】", margin, holder.y, 10);
                        holder.y -= lineHeight;
                        for (String code : group.scaleCodes()) {
                            String label = displayNames.getOrDefault(code, code);
                            String interp = interpretations.get(code);
                            if (interp == null || interp.isEmpty()) continue;
                            int linesNeeded = 1 + wrap(interp, 42).size();
                            holder.y = ensureSpace(document, font, margin, pageHeight, minY, holder, (int)(linesNeeded * (lineHeight - 2) + 8));
                            drawTextAt(holder.cs, font, label + " (" + code + "):", margin, holder.y, 9);
                            holder.y -= lineHeight;
                            for (String line : wrap(interp, 42)) {
                                drawTextAt(holder.cs, font, "  " + line, margin, holder.y, 8);
                                holder.y -= lineHeight - 2;
                            }
                            holder.y -= 4;
                        }
                        holder.y -= 4;
                    }
                } else {
                    for (String code : scaleOrder) {
                        String label = displayNames.getOrDefault(code, code);
                        String interp = interpretations.get(code);
                        if (interp == null || interp.isEmpty()) continue;
                        int linesNeeded = 1 + wrap(interp, 42).size();
                        holder.y = ensureSpace(document, font, margin, pageHeight, minY, holder, (int)(linesNeeded * (lineHeight - 2) + 8));
                        drawTextAt(holder.cs, font, label + " (" + code + "):", margin, holder.y, 9);
                        holder.y -= lineHeight;
                        for (String line : wrap(interp, 42)) {
                            drawTextAt(holder.cs, font, "  " + line, margin, holder.y, 8);
                            holder.y -= lineHeight - 2;
                        }
                        holder.y -= 4;
                    }
                }
                String totalInterp = interpretations.get("TOTAL");
                if (totalInterp != null && !totalInterp.isEmpty()) {
                    int totalLines = 1 + wrap(totalInterp, 42).size();
                    holder.y = ensureSpace(document, font, margin, pageHeight, minY, holder, (int)(totalLines * (lineHeight - 2) + 8));
                    drawTextAt(holder.cs, font, "총점 (TOTAL):", margin, holder.y, 9);
                    holder.y -= lineHeight;
                    for (String line : wrap(totalInterp, 42)) {
                        drawTextAt(holder.cs, font, "  " + line, margin, holder.y, 8);
                        holder.y -= lineHeight - 2;
                    }
                    holder.y -= 4;
                }

                holder.y = ensureSpace(document, font, margin, pageHeight, minY, holder, 30);
                holder.y -= 8;
                String note = "위 해석은 검사별 기준(총점 또는 T점수 구간)에 따른 참고 설명입니다. 심리상담·임상적 판단이 필요할 경우 전문가와 상담하시기 바랍니다.";
                holder.cs.setNonStrokingColor(new Color(107, 114, 128));
                for (String line : wrap(note, 42)) {
                    drawTextAt(holder.cs, font, line, margin, holder.y, 9);
                    holder.y -= 12;
                }
            } finally {
                if (holder != null) holder.cs.close();
                else cs.close();
            }

            document.save(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("PDF 생성 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 현재 페이지에 필요한 만큼 공간이 있는지 확인하고, 부족하면 새 페이지를 추가한 뒤 y 반환.
     * 새 페이지 시 기존 cs는 닫고, holder에 새 cs와 y를 설정한다.
     */
    private static final class PageHolder {
        PDPageContentStream cs;
        PDPage page;
        float y;
        PageHolder(PDPageContentStream cs, PDPage page, float y) {
            this.cs = cs;
            this.page = page;
            this.y = y;
        }
    }

    private float ensureSpace(PDDocument document, PDFont font, float margin, float pageHeight, float minY,
                              PageHolder h, float requiredHeight) throws Exception {
        if (h.y - requiredHeight >= minY) return h.y;
        h.cs.close();
        PDPage newPage = new PDPage(PDRectangle.A4);
        document.addPage(newPage);
        h.page = newPage;
        h.cs = new PDPageContentStream(document, newPage);
        h.y = pageHeight - margin;
        h.cs.setNonStrokingColor(new Color(55, 65, 81));
        drawTextAt(h.cs, font, "■ 척도별 해석 (계속)", margin, h.y, 11);
        h.y -= 18;
        h.cs.setNonStrokingColor(Color.BLACK);
        return h.y;
    }

    /** NEO 검사이고 하위척도(N1, E1 등)가 있으면 주척도별 그룹 목록 반환 */
    private List<ScaleGroupDto> buildScaleGroupsIfNeo(String assessmentName, List<String> scaleOrder) {
        if (assessmentName == null || !assessmentName.contains("NEO") || scaleOrder == null || scaleOrder.isEmpty()) {
            return List.of();
        }
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

    /** 테이블·화면과 동일한 순서로, 원점수 또는 T점수가 있는 척도 코드만 나열 */
    private List<String> buildCodesInOrder(String assessmentName, List<String> scaleOrder,
                                            Map<String, Double> scaleRaw, Map<String, Double> scaleT) {
        List<ScaleGroupDto> scaleGroups = buildScaleGroupsIfNeo(assessmentName, scaleOrder);
        List<String> out = new ArrayList<>();
        if (!scaleGroups.isEmpty()) {
            for (ScaleGroupDto group : scaleGroups) {
                for (String code : group.scaleCodes()) {
                    if (scaleRaw.get(code) == null && scaleT.get(code) == null) continue;
                    out.add(code);
                }
            }
        } else {
            for (String code : scaleOrder) {
                if (scaleRaw.get(code) == null && scaleT.get(code) == null) continue;
                out.add(code);
            }
        }
        return out;
    }

    /**
     * 척도별 T점수 가로 막대만 표시(원점수는 표에서 확인).
     * @param minAllowedY 플롯 하단이 내려갈 수 있는 최소 y (페이지 여백)
     */
    private float drawScaleBarCharts(PDPageContentStream cs, PDFont font,
                                     float marginL, float marginR, float pageWidth, float y,
                                     List<String> codes, Map<String, String> displayNames,
                                     Map<String, Double> scaleT, float minAllowedY) throws Exception {
        cs.setNonStrokingColor(new Color(55, 65, 81));
        drawTextAt(cs, font, "■ 척도별 T점수 그래프", marginL, y, 11);
        y -= 16;
        cs.setNonStrokingColor(new Color(100, 116, 139));
        java.util.List<String> hintLines = wrap(
                "막대 색(T): 파란·회색·주황(40/60 기준). 세로선 T=50. 원점수는 아래 표.", 58);
        for (String line : hintLines) {
            drawTextAt(cs, font, line, marginL, y, 8);
            y -= 10;
        }
        cs.setNonStrokingColor(Color.BLACK);
        y -= 6;

        float panelFixed = 26 + 22 + 48 + 34 + 22;
        float avail = y - minAllowedY - 10;
        int n = codes.size();
        float rowH = 18f;
        if (n > 0 && avail > panelFixed) {
            float maxRow = (avail - panelFixed) / n;
            rowH = Math.max(10.5f, Math.min(18f, maxRow));
        }

        float totalW = pageWidth - marginL - marginR;
        float labelCol = Math.min(220, Math.max(130, totalW * 0.40f));
        float labelFont = rowH < 13.5f ? 6.8f : 7.5f;
        float axisFont = 6.8f;
        float boxPad = 8;
        float cornerR = 6f;

        return drawOneBarChartPanel(cs, font, marginL, totalW, y, codes, displayNames, scaleT, true,
                0, 0, 0, 0, labelCol, rowH, labelFont, axisFont, boxPad, cornerR);
    }

    /**
     * 웹 차트와 유사: 수치 X축·10단위 격자·45° 눈금·T=50 기준선·라벨 우측 정렬·플롯 배경·둥근 테두리.
     */
    private float drawOneBarChartPanel(PDPageContentStream cs, PDFont font,
                                       float originX, float totalW, float y,
                                       List<String> codes, Map<String, String> displayNames,
                                       Map<String, Double> values, boolean isTScore,
                                       double minT, double maxT, double minRaw, double maxRaw,
                                       float labelCol, float rowH, float labelFont, float axisFont,
                                       float boxPad, float cornerR) throws Exception {
        float innerRightPad = 14;
        float plotW = Math.max(100, totalW - labelCol - innerRightPad);
        float plotX = originX + labelCol;

        double axisMin;
        double axisMax;
        double tickStep;
        if (isTScore) {
            double vmin = Double.POSITIVE_INFINITY;
            double vmax = Double.NEGATIVE_INFINITY;
            for (String code : codes) {
                double v = values.getOrDefault(code, 0.0);
                vmin = Math.min(vmin, v);
                vmax = Math.max(vmax, v);
            }
            axisMin = Math.max(0, Math.floor(vmin / 10) * 10 - 10);
            axisMax = Math.ceil(vmax / 10) * 10 + 10;
            if (axisMax <= axisMin) axisMax = axisMin + 40;
            axisMin = Math.min(axisMin, 50);
            axisMax = Math.max(axisMax, 60);
            tickStep = 10;
        } else {
            axisMin = 0;
            axisMax = maxRaw;
            if (axisMax <= axisMin) axisMax = axisMin + 10;
            tickStep = pickNiceTickStep(axisMax);
            axisMax = Math.ceil(axisMax / tickStep) * tickStep;
            if (axisMax <= 0) axisMax = tickStep;
        }

        float panelTopY = y + 16;
        String panelTitle = isTScore ? "T 점수" : "원점수";
        cs.setNonStrokingColor(new Color(30, 64, 175));
        drawTextAt(cs, font, panelTitle, originX + boxPad * 0.5f, y, 11);
        cs.setNonStrokingColor(Color.BLACK);
        y -= rowH + 4;

        float chartTop = y + 2;
        float barBottomY = y - codes.size() * rowH - 2;
        float axisLineY = barBottomY - 2;
        float maxLabelW = labelCol - 10;

        // 플롯 배경 (카드 내부)
        cs.setNonStrokingColor(new Color(249, 250, 251));
        cs.addRect(plotX, barBottomY - 2, plotW, chartTop - barBottomY + 4);
        cs.fill();
        cs.setNonStrokingColor(Color.BLACK);

        // 세로 격자선 + X=0 기준 가로선
        cs.setStrokingColor(new Color(229, 231, 235));
        cs.setLineWidth(0.6f);
        for (double t = axisMin; t <= axisMax + 1e-6; t += tickStep) {
            float gx = valueToPlotX(t, axisMin, axisMax, plotX, plotW);
            cs.moveTo(gx, barBottomY - 2);
            cs.lineTo(gx, chartTop + 2);
        }
        cs.stroke();
        cs.setStrokingColor(new Color(209, 213, 219));
        cs.setLineWidth(1f);
        cs.moveTo(plotX, axisLineY);
        cs.lineTo(plotX + plotW, axisLineY);
        cs.stroke();
        cs.setStrokingColor(Color.BLACK);
        cs.setLineWidth(1f);

        // 막대
        for (String code : codes) {
            String full = displayNames.getOrDefault(code, code) + " (" + code + ")";
            String lab = truncateLabelToFit(font, full, maxLabelW, labelFont);
            double val = values.getOrDefault(code, 0.0);
            float barW = isTScore
                    ? (float) ((val - axisMin) / (axisMax - axisMin)) * plotW
                    : (float) ((val - axisMin) / (axisMax - axisMin)) * plotW;

            float baseline = y - 5;
            drawTextRightAligned(cs, font, lab, originX + labelCol - 4, baseline, labelFont);

            if (isTScore) {
                cs.setNonStrokingColor(tScoreColor(val));
            } else {
                cs.setNonStrokingColor(new Color(129, 140, 248));
            }
            float barH = rowH - 7;
            float barY = y - rowH + 5;
            cs.addRect(plotX, barY, Math.max(0.5f, barW), barH);
            cs.fill();
            cs.setNonStrokingColor(Color.BLACK);

            y -= rowH;
        }

        // T=50 기준선 (막대·격자 위)
        if (isTScore && 50 >= axisMin && 50 <= axisMax) {
            float lineX = valueToPlotX(50, axisMin, axisMax, plotX, plotW);
            cs.setStrokingColor(new Color(99, 102, 241));
            cs.setLineWidth(1.8f);
            cs.moveTo(lineX, chartTop + 3);
            cs.lineTo(lineX, barBottomY - 2);
            cs.stroke();
            cs.setLineWidth(1f);
            cs.setStrokingColor(Color.BLACK);
        }

        // X축 눈금 숫자 (약 45° 기울임, 웹 차트와 유사)
        float tickLabelY = axisLineY - 6;
        cs.setNonStrokingColor(new Color(71, 85, 105));
        for (double t = axisMin; t <= axisMax + 1e-6; t += tickStep) {
            float gx = valueToPlotX(t, axisMin, axisMax, plotX, plotW);
            String lab = isTScore
                    ? String.format(java.util.Locale.KOREA, "%.0f", t)
                    : (tickStep >= 1 ? String.format(java.util.Locale.KOREA, "%.0f", t) : String.format(java.util.Locale.KOREA, "%.1f", t));
            drawTextRotatedCcW(cs, font, lab, axisFont, gx, tickLabelY, -42);
        }
        cs.setNonStrokingColor(Color.BLACK);

        float capDrop = rowH < 13.5f ? 28f : 34f;
        float axisCaptionY = tickLabelY - capDrop;
        cs.setNonStrokingColor(new Color(100, 116, 139));
        if (isTScore) {
            String cap = String.format(java.util.Locale.KOREA, "T점수 (규준 평균 50). 표시 범위 %.0f ~ %.0f", axisMin, axisMax);
            int wrapLen = rowH < 13.5f ? 52 : 58;
            for (String line : wrap(cap, wrapLen)) {
                drawTextAt(cs, font, line, plotX, axisCaptionY, axisFont);
                axisCaptionY -= rowH < 13.5f ? 9 : 10;
            }
        } else {
            String cap = String.format(java.util.Locale.KOREA, "원점수 (0 ~ %.1f)", axisMax);
            drawTextAt(cs, font, cap, plotX, axisCaptionY, axisFont);
            axisCaptionY -= 10;
        }
        cs.setNonStrokingColor(Color.BLACK);

        float panelBottomY = axisCaptionY - (rowH < 13.5f ? 8 : 12);
        float boxH = panelTopY - panelBottomY;
        float bx = originX - boxPad;
        float bw = totalW + 2 * boxPad;
        cs.setStrokingColor(new Color(203, 213, 225));
        cs.setLineWidth(1f);
        strokeRoundedRect(cs, bx, panelBottomY, bw, boxH, cornerR);
        cs.setStrokingColor(Color.BLACK);

        return panelBottomY - 8;
    }

    private static float valueToPlotX(double value, double axisMin, double axisMax, float plotX, float plotW) {
        double span = axisMax - axisMin;
        if (span <= 0) return plotX;
        double f = (value - axisMin) / span;
        return plotX + (float) (f * plotW);
    }

    private static double pickNiceTickStep(double span) {
        if (span <= 0) return 1;
        if (span <= 15) return 2;
        if (span <= 40) return 5;
        if (span <= 100) return 10;
        return Math.pow(10, Math.floor(Math.log10(span)));
    }

    private void drawTextRightAligned(PDPageContentStream cs, PDFont font, String text, float rightX, float baselineY, float fontSize) throws Exception {
        if (text == null || text.isEmpty()) return;
        float w = font.getStringWidth(text) / 1000f * fontSize;
        drawTextAt(cs, font, text, rightX - w, baselineY, fontSize);
    }

    /** degCcW: 음수면 시계 방향 기울임(눈금 아래 숫자에 흔한 형태). */
    private void drawTextRotatedCcW(PDPageContentStream cs, PDFont font, String text, float fontSize, float cx, float cy, double degCcW) throws Exception {
        if (text == null || text.isEmpty()) return;
        float sw = font.getStringWidth(text) / 1000f * fontSize;
        double rad = Math.toRadians(degCcW);
        cs.beginText();
        cs.setFont(font, fontSize);
        Matrix m = Matrix.getTranslateInstance(cx, cy);
        m.concatenate(Matrix.getRotateInstance(rad, 0, 0));
        m.concatenate(Matrix.getTranslateInstance(-sw / 2f, -fontSize * 0.15f));
        cs.setTextMatrix(m);
        cs.showText(text);
        cs.endText();
    }

    /** (x,y)=좌하단, 높이 h는 위로. 둥근 사각형 테두리만 stroke. */
    private static void strokeRoundedRect(PDPageContentStream cs, float x, float y, float w, float h, float r) throws IOException {
        r = Math.min(r, Math.min(w, h) / 2f);
        float x0 = x;
        float y0 = y;
        float x1 = x + w;
        float y1 = y + h;
        float c = 0.5522847498f * r;
        cs.moveTo(x0 + r, y0);
        cs.lineTo(x1 - r, y0);
        cs.curveTo(x1 - r + c, y0, x1, y0 + r - c, x1, y0 + r);
        cs.lineTo(x1, y1 - r);
        cs.curveTo(x1, y1 - r + c, x1 - r + c, y1, x1 - r, y1);
        cs.lineTo(x0 + r, y1);
        cs.curveTo(x0 + r - c, y1, x0, y1 - r + c, x0, y1 - r);
        cs.lineTo(x0, y0 + r);
        cs.curveTo(x0, y0 + r - c, x0 + r - c, y0, x0 + r, y0);
        cs.closePath();
        cs.stroke();
    }

    /** PDF 폰트 기준으로 너비에 맞게 말줄임(…). */
    private static String truncateLabelToFit(PDFont font, String text, float maxWidthPt, float fontSize) throws IOException {
        if (text == null || text.isEmpty()) return "";
        float w = font.getStringWidth(text) / 1000f * fontSize;
        if (w <= maxWidthPt) return text;
        String ell = "…";
        for (int len = text.length() - 1; len >= 1; len--) {
            String s = text.substring(0, len) + ell;
            float sw = font.getStringWidth(s) / 1000f * fontSize;
            if (sw <= maxWidthPt) return s;
        }
        return ell;
    }

    private static Color tScoreColor(double t) {
        if (t < 40) return new Color(59, 130, 246);
        if (t > 60) return new Color(245, 158, 11);
        return new Color(107, 114, 128);
    }

    private Map<String, Double> readMap(String json) {
        if (json == null) {
            return Map.of();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Double>>() {});
        } catch (Exception e) {
            throw new IllegalStateException("결과를 읽는 중 오류가 발생했습니다.", e);
        }
    }

    private String nullSafe(Double value) {
        return value == null ? "-" : value.toString();
    }

    private String nullSafeOneDecimal(Double value) {
        return value == null ? "-" : String.format(java.util.Locale.KOREA, "%.1f", value);
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
     * 테이블을 그립니다. 헤더 행은 회색 배경, groupHeaderIndices에 있는 행은 연한 남색 배경·강조 텍스트.
     * @return 마지막 행 아래 y
     */
    private float drawTableStyled(PDPageContentStream cs, PDFont font,
                                  float startX, float startY, float rowHeight,
                                  float[] colWidths, String[] header,
                                  java.util.List<String[]> rows,
                                  Set<Integer> groupHeaderIndices) throws Exception {
        float tableWidth = 0;
        for (float w : colWidths) tableWidth += w;
        int totalRows = 1 + rows.size();
        float tableBottom = startY - rowHeight * totalRows;

        // 1) 배경 채우기: 헤더 행 연한 회색
        cs.setNonStrokingColor(new Color(243, 244, 246));
        cs.addRect(startX, startY - rowHeight, tableWidth, rowHeight);
        cs.fill();

        // 그룹 헤더 행 연한 남색 (결과 화면과 비슷하게)
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

        // 2) 격자선
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

        // 3) 헤더 텍스트
        float textY = startY - rowHeight + rowHeight * 0.35f;
        float cellX = startX;
        for (int i = 0; i < header.length; i++) {
            drawTextAt(cs, font, header[i], cellX + 4, textY, 10);
            cellX += colWidths[i];
        }

        // 4) 데이터 행 텍스트
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

    /** 한 줄당 최대 글자 수로 나누기. 공백·구두점에서 끊어 단어 중간에서 잘리지 않게 함 */
    private static java.util.List<String> wrap(String text, int maxCharsPerLine) {
        if (text == null || text.isEmpty()) return java.util.List.of();
        java.util.List<String> lines = new java.util.ArrayList<>();
        int len = text.length();
        int i = 0;
        while (i < len) {
            int end = Math.min(i + maxCharsPerLine, len);
            if (end < len) {
                // 단어 중간에서 끊기지 않도록, 끊기 좋은 위치(공백·구두점) 탐색
                int lastGood = -1;
                for (int j = end - 1; j >= i; j--) {
                    char c = text.charAt(j);
                    if (c == ' ' || c == '.' || c == '。' || c == '·' || c == ')' || c == ',') {
                        lastGood = j;
                        break;
                    }
                }
                if (lastGood >= i) {
                    end = lastGood + 1;
                }
            }
            lines.add(text.substring(i, end));
            i = end;
        }
        return lines;
    }
}

