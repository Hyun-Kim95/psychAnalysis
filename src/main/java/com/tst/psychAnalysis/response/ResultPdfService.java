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
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
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

                // T점수 설명
                cs.setNonStrokingColor(new Color(71, 85, 105));
                drawTextAt(cs, font, "T점수: 평균 50, 표준편차 10의 표준화 점수입니다. 50에 가까우면 평균 수준, 60 이상은 높은 편, 40 미만은 낮은 편으로 해석합니다.", margin, y, 9);
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

