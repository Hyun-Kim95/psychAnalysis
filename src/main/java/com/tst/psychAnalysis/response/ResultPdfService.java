package com.tst.psychAnalysis.response;

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
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

            try (PDPageContentStream cs = new PDPageContentStream(document, page)) {
                float margin = 45;
                float y = pageHeight - margin;

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

                // 하위척도 표 헤더 (척도 열 너비를 넉넉히 해 Raw 점수와 겹치지 않도록)
                String[] headers = { "척도", "Raw 점수", "T 점수" };
                float[] colWidths = { 110, 100, 100 };
                float rowHeight = 18;

                cs.setNonStrokingColor(new Color(75, 83, 99));

                float x = margin;
                for (String h : headers) {
                    drawTextAt(cs, font, h, x + 4, y, 11);
                    x += colWidths[x == margin ? 0 : x == margin + colWidths[0] ? 1 : 2];
                }
                y -= rowHeight;

                // 하위척도 값 (검사별 척도 순서·한글명)
                cs.setNonStrokingColor(Color.BLACK);
                for (String code : scaleOrder) {
                    Double raw = scaleRaw.get(code);
                    Double t = scaleT.get(code);
                    if (raw == null && t == null) continue;

                    String label = displayNames.getOrDefault(code, code);
                    x = margin;
                    drawTextAt(cs, font, label + "(" + code + ")", x + 4, y, 10);
                    x += colWidths[0];
                    drawTextAt(cs, font, raw != null ? nullSafeOneDecimal(raw) : "-", x + 4, y, 10);
                    x += colWidths[1];
                    drawTextAt(cs, font, t != null ? nullSafeOneDecimal(t) : "-", x + 4, y, 10);
                    y -= rowHeight;
                }

                y -= 16;
                cs.setNonStrokingColor(new Color(55, 65, 81));
                drawTextAt(cs, font, "■ 척도별 해석", margin, y, 11);
                y -= 18;

                cs.setNonStrokingColor(Color.BLACK);
                float lineHeight = 14;
                for (String code : scaleOrder) {
                    String label = displayNames.getOrDefault(code, code);
                    String interp = interpretations.get(code);
                    if (interp == null || interp.isEmpty()) continue;
                    if (y < margin + 40) break;
                    drawTextAt(cs, font, label + " (" + code + "):", margin, y, 9);
                    y -= lineHeight;
                    for (String line : wrap(interp, 42)) {
                        if (y < margin + 30) break;
                        drawTextAt(cs, font, "  " + line, margin, y, 8);
                        y -= lineHeight - 2;
                    }
                    y -= 4;
                }

                y -= 8;
                String note = "위 해석은 검사별 기준(총점 또는 T점수 구간)에 따른 참고 설명입니다. 심리상담·임상적 판단이 필요할 경우 전문가와 상담하시기 바랍니다.";
                cs.setNonStrokingColor(new Color(107, 114, 128));
                drawTextAt(cs, font, note, margin, y, 9);
            }

            document.save(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("PDF 생성 중 오류가 발생했습니다.", e);
        }
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

    /** 한 줄당 최대 글자 수로 나누기 (한글 1글자 = 1단위) */
    private static java.util.List<String> wrap(String text, int maxCharsPerLine) {
        if (text == null || text.isEmpty()) return java.util.List.of();
        java.util.List<String> lines = new java.util.ArrayList<>();
        int len = text.length();
        for (int i = 0; i < len; i += maxCharsPerLine) {
            int end = Math.min(i + maxCharsPerLine, len);
            lines.add(text.substring(i, end));
        }
        return lines;
    }
}

