package com.tst.psychAnalysis.db;

import com.tst.psychAnalysis.assessment.NeoScaleInterpretation;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * NEO 성격검사 구성을 공식(대학·성인용)에 맞춤: 29개 하위척도(N1~N10, E1~E4, O1~O4, A1~A5, C1~C6).
 * 간단 29문항(하위척도당 1문항), 상세 58문항(하위척도당 2문항). 5점 리커트.
 */
@Component
@Order(Integer.MAX_VALUE - 3)
public class NeoMigrationRunner implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;

    /** 하위척도별 1문항용 문장 (공식 구성 설명 기반) */
    private static final Map<String, String> FACET_ITEM_TEXT = Map.ofEntries(
        Map.entry("N1", "나는 자주 불안하고 초조하며 긴장되고 두려움과 걱정이 많은 편이다."),
        Map.entry("N2", "타인의 비판에 예민하고 불만이나 좌절감을 자주 느끼며 화를 잘 내는 편이다."),
        Map.entry("N3", "매사에 무기력하고 일이 잘 안 되면 죄책감을 느끼고 쉽게 낙심·포기하며 갑자기 슬퍼질 때가 있다."),
        Map.entry("N4", "성미가 급하고 자신을 통제하지 못하며 생각 없이 말이나 행동이 먼저 나가 후회할 때가 많다."),
        Map.entry("N5", "자신에 대한 확신이 부족하여 사람들 사이에서 놀림이나 조롱을 당할까 크게 두려워하는 편이다."),
        Map.entry("N6", "과거 경험한 충격적인 사건에 대한 심상과 기억이 반복적으로 떠올라 불안을 느끼고 회피하려는 편이다."),
        Map.entry("N7", "조그마한 스트레스에도 잘 견디지 못하고 절망하고 갈등을 느끼며 자신을 도와줄 사람을 먼저 찾게 된다."),
        Map.entry("N8", "사람들의 행위와 감정에 무관심하고 어울리기보다 뒤로 물러서며, 사고 과정이 모호한 편이다."),
        Map.entry("N9", "법이나 규칙을 반하고 기존 사회적·정치적·종교적 가치를 인정하지 않으며 지도에 비판적인 편이다."),
        Map.entry("N10", "자신감이 부족하고 열등감이 있으며 기운이 없고 자신을 허약하게 생각하는 편이다."),
        Map.entry("E1", "사람들과의 교제보다 혼자가 편하고 주변 관계가 단조로운 편이다."),
        Map.entry("E2", "자기주장이 강하고 설득력 있으며 사람들에게 말을 잘 하는 지도자 역할을 하는 편이다."),
        Map.entry("E3", "복잡하고 모험적인 환경을 선호하며 거친 운동, 자극적인 놀이를 즐기는 편이다."),
        Map.entry("E4", "매사에 바쁘고 활동적이며 융통성이 있고 적극적이며 재빠른 편이다."),
        Map.entry("O1", "공상을 즐기고 상상력과 창의력이 풍부하며 기발하고 독특한 의견이 많은 편이다."),
        Map.entry("O2", "순수 예술과 자연의 아름다움을 즐기며 추구하고 경험을 중시하는 편이다."),
        Map.entry("O3", "섬세하고 깊은 감정을 풍부하게 느끼고 자신과 타인의 감정을 중요하게 생각하는 편이다."),
        Map.entry("O4", "새로운 행동을 시도하고 쉽게 적응하며 다양한 취미 활동을 원하는 편이다."),
        Map.entry("A1", "자상하고 따뜻하며 친절한 관계를 유지하고 타인을 적극적으로 돕고 싶어 하는 편이다."),
        Map.entry("A2", "사람은 선의를 가지고 접근한다고 생각하고 솔직하며 타인에 대한 의심이 적은 편이다."),
        Map.entry("A3", "타인의 행복에 적극적으로 관심을 보이고 협동하고 헌신적이며 봉사적인 편이다."),
        Map.entry("A4", "사람들의 실수와 잘못을 잘 용서하며 너그럽게 이해하고 화를 참는 편이다."),
        Map.entry("A5", "자신을 자랑하지 않고 뽐내지 않으며 다른 사람을 칭찬하고 인정하는 겸손함이 있는 편이다."),
        Map.entry("C1", "스스로를 능력 있고 효율적으로 생각하며 자신감이 넘치는 편이다."),
        Map.entry("C2", "성취 욕구가 강하고 포부 수준이 높으며 늘 바르게 목표 달성을 위해 노력하는 편이다."),
        Map.entry("C3", "깔끔하고 정리 정돈된 생활을 하고 계획을 세우고 꼼꼼하게 지켜 나가는 편이다."),
        Map.entry("C4", "자신의 윤리적 원칙을 고수하고 끈기 있게 마무리하는 경향이 있는 편이다."),
        Map.entry("C5", "유혹에 잘 넘어가지 않고 충동적인 행동을 억제하며 목표를 향해 꾸준히 노력하는 편이다."),
        Map.entry("C6", "결정을 내릴 때 신중하고 면밀하게 검토하며 미래의 위험을 미리 예상하고 대비하는 편이다.")
    );

    public NeoMigrationRunner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (alreadyApplied()) return;
        runMigration();
    }

    private boolean alreadyApplied() {
        Long shortCount = jdbcTemplate.query(
            "SELECT COUNT(*) FROM item i JOIN assessment a ON a.id = i.assessment_id WHERE a.name = 'NEO 성격검사'",
            (rs, rn) -> rs.getLong(1)).stream().findFirst().orElse(0L);
        Long detailCount = jdbcTemplate.query(
            "SELECT COUNT(*) FROM item i JOIN assessment a ON a.id = i.assessment_id WHERE a.name = 'NEO 성격검사 (상세)'",
            (rs, rn) -> rs.getLong(1)).stream().findFirst().orElse(0L);
        return shortCount == 29 && detailCount == 58;
    }

    private void runMigration() {
        Long shortId = getAssessmentId("NEO 성격검사");
        Long detailId = getAssessmentId("NEO 성격검사 (상세)");
        if (shortId == null) return;

        // 상세: 없으면 생성
        if (detailId == null) {
            jdbcTemplate.update("INSERT INTO assessment (name, description, is_active) VALUES ('NEO 성격검사 (상세)', 'NEO 네오 성격검사(대학·성인용) 공식 구성. 29개 하위척도, 58문항. 약 15~20분.', TRUE)");
            detailId = getAssessmentId("NEO 성격검사 (상세)");
        }

        for (Long assessmentId : List.of(shortId, detailId)) {
            int itemsPerFacet = assessmentId.equals(shortId) ? 1 : 2;
            deleteItemsAndNormsAndScales(assessmentId);
            insertScales(assessmentId);
            insertItemsAndChoices(assessmentId, itemsPerFacet);
            insertNorms(assessmentId, itemsPerFacet);
        }

        jdbcTemplate.update("UPDATE assessment SET description = 'NEO 네오 성격검사(대학·성인용). 29개 하위척도, 29문항. 약 7~10분.' WHERE id = ?", shortId);
        jdbcTemplate.update("UPDATE assessment SET description = 'NEO 네오 성격검사(대학·성인용) 공식 구성. 29개 하위척도, 58문항. 약 15~20분.' WHERE id = ?", detailId);
    }

    private Long getAssessmentId(String name) {
        List<Long> list = jdbcTemplate.query("SELECT id FROM assessment WHERE name = ? LIMIT 1", (rs, rowNum) -> rs.getLong("id"), name);
        return list.isEmpty() ? null : list.get(0);
    }

    private void deleteItemsAndNormsAndScales(Long assessmentId) {
        jdbcTemplate.update("DELETE FROM item_response WHERE item_id IN (SELECT id FROM item WHERE assessment_id = ?)", assessmentId);
        jdbcTemplate.update("DELETE FROM choice WHERE item_id IN (SELECT id FROM item WHERE assessment_id = ?)", assessmentId);
        jdbcTemplate.update("DELETE FROM item WHERE assessment_id = ?", assessmentId);
        jdbcTemplate.update("DELETE FROM norm WHERE assessment_id = ?", assessmentId);
        jdbcTemplate.update("DELETE FROM scale WHERE assessment_id = ?", assessmentId);
    }

    private void insertScales(Long assessmentId) {
        for (String facetCode : NeoScaleInterpretation.FACET_ORDER) {
            String facetName = NeoScaleInterpretation.getFacetDisplayName(facetCode);
            jdbcTemplate.update(
                "INSERT INTO scale (assessment_id, code, name, description) VALUES (?, ?, ?, ?)",
                assessmentId, facetCode, facetName, facetCode + " " + facetName);
        }
    }

    private void insertItemsAndChoices(Long assessmentId, int itemsPerFacet) {
        int sortOrder = 0;
        for (String facetCode : NeoScaleInterpretation.FACET_ORDER) {
            Long scaleId = jdbcTemplate.queryForObject("SELECT id FROM scale WHERE assessment_id = ? AND code = ? LIMIT 1", Long.class, assessmentId, facetCode);
            String baseText = FACET_ITEM_TEXT.getOrDefault(facetCode, facetCode + " 문항입니다.");
            for (int k = 0; k < itemsPerFacet; k++) {
                sortOrder++;
                String text = itemsPerFacet == 1 ? baseText : (k == 0 ? baseText : baseText.replace("이다.", " 편이다."));
                jdbcTemplate.update(
                    "INSERT INTO item (assessment_id, scale_id, item_number, \"text\", is_required, is_reverse_scored, weight, sort_order) VALUES (?, ?, ?, ?, TRUE, FALSE, 1.0, ?)",
                    assessmentId, scaleId, sortOrder, text, sortOrder);
            }
        }
        jdbcTemplate.update("""
            INSERT INTO choice (item_id, label, value, sort_order)
            SELECT i.id, c.label, c.val, c.ord FROM item i
            CROSS JOIN (VALUES (1,'전혀 그렇지 않다',1),(2,'그렇지 않다',2),(3,'보통이다',3),(4,'그렇다',4),(5,'매우 그렇다',5)) AS c(ord, label, val)
            WHERE i.assessment_id = ?
            """, assessmentId);
    }

    private void insertNorms(Long assessmentId, int itemsPerFacet) {
        double meanPerItem = 3.0;
        double sdPerItem = 1.2;
        double mean = 29 * itemsPerFacet * meanPerItem;
        double sd = Math.sqrt(29 * itemsPerFacet) * sdPerItem;
        for (String facetCode : NeoScaleInterpretation.FACET_ORDER) {
            Long scaleId = jdbcTemplate.queryForObject("SELECT id FROM scale WHERE assessment_id = ? AND code = ? LIMIT 1", Long.class, assessmentId, facetCode);
            double scaleMean = itemsPerFacet * meanPerItem;
            double scaleSd = Math.sqrt(itemsPerFacet) * sdPerItem;
            jdbcTemplate.update("INSERT INTO norm (assessment_id, scale_id, group_code, mean, sd) VALUES (?, ?, 'default', ?, ?)", assessmentId, scaleId, scaleMean, scaleSd);
        }
        jdbcTemplate.update("INSERT INTO norm (assessment_id, scale_id, group_code, mean, sd) VALUES (?, NULL, 'default', ?, ?)", assessmentId, mean, sd);
    }
}
