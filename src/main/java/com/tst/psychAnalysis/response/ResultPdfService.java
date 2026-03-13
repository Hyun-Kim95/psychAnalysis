package com.tst.psychAnalysis.response;

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
import java.util.Map;
import java.util.Objects;

@Service
public class ResultPdfService {

    private final ObjectMapper objectMapper;

    public ResultPdfService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public byte[] generate(Result result) {
        Map<String, Double> scaleRaw = readMap(result.getScaleRawScoresJson());
        Map<String, Double> scaleT = readMap(result.getScaleTScoresJson());

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
            float pageWidth = box.getWidth();
            float pageHeight = box.getHeight();

            try (PDPageContentStream cs = new PDPageContentStream(document, page)) {
                float margin = 45;
                float y = pageHeight - margin;

                // 제목
                cs.setNonStrokingColor(Color.BLACK);
                drawTextAt(cs, font, "심리검사 결과 리포트", margin, y, 16);
                y -= 28;

                // 총점 요약
                String summary = "총점: " + nullSafe(result.getTotalRawScore())
                        + " / 총 T점수: " + nullSafeOneDecimal(result.getTotalTScore());
                drawTextAt(cs, font, summary, margin, y, 12);
                y -= 24;

                // 하위척도 표 헤더
                String[] headers = { "척도", "Raw 점수", "T 점수" };
                float[] colWidths = { 80, 120, 120 };
                float rowHeight = 18;

                cs.setNonStrokingColor(new Color(75, 83, 99));

                float x = margin;
                for (String h : headers) {
                    drawTextAt(cs, font, h, x + 4, y, 11);
                    x += colWidths[x == margin ? 0 : x == margin + colWidths[0] ? 1 : 2];
                }
                y -= rowHeight;

                // 하위척도 값
                cs.setNonStrokingColor(Color.BLACK);
                for (Map.Entry<String, Double> e : scaleRaw.entrySet()) {
                    String code = e.getKey();
                    Double raw = e.getValue();
                    Double t = scaleT.get(code);

                    x = margin;
                    drawTextAt(cs, font, code, x + 4, y, 10);
                    x += colWidths[0];
                    drawTextAt(cs, font, nullSafeOneDecimal(raw), x + 4, y, 10);
                    x += colWidths[1];
                    drawTextAt(cs, font, nullSafeOneDecimal(t), x + 4, y, 10);
                    y -= rowHeight;
                }

                y -= 12;
                String note = "본 리포트는 프로토타입으로, 실제 임상적 해석이 아닌 연구/테스트 목적의 예시입니다.";
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
}

