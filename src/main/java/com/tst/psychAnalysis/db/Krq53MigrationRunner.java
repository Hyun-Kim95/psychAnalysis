package com.tst.psychAnalysis.db;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 회복탄력성 검사: 간단 27문항(척도당 3문항), 상세 53문항(KRQ-53 전체). 약 2배 차이.
 * 9개 하위척도, 5점 리커트, 역채점 반영.
 */
@Component
@Order(Integer.MAX_VALUE - 1)
public class Krq53MigrationRunner implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;

    public Krq53MigrationRunner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (alreadyApplied()) {
            return;
        }
        runMigration();
    }

    private boolean alreadyApplied() {
        try {
            Long shortCount = jdbcTemplate.query(
                "SELECT COUNT(*) FROM item i JOIN assessment a ON a.id = i.assessment_id WHERE a.name = '회복탄력성 검사'",
                (rs, rn) -> rs.getLong(1)).stream().findFirst().orElse(0L);
            Long detailCount = jdbcTemplate.query(
                "SELECT COUNT(*) FROM item i JOIN assessment a ON a.id = i.assessment_id WHERE a.name = '회복탄력성 검사 (상세)'",
                (rs, rn) -> rs.getLong(1)).stream().findFirst().orElse(0L);
            return shortCount == 27 && detailCount == 53;
        } catch (BadSqlGrammarException ex) {
            // item/assessment 테이블이 아직 없는 초기 상태에서는 마이그레이션을 수행하도록 처리
            return false;
        }
    }

    private void runMigration() {
        Long shortId = getAssessmentId("회복탄력성 검사");
        Long detailId = getAssessmentId("회복탄력성 검사 (상세)");
        if (shortId == null) return;

        // 간단: 27문항 (척도당 3문항, KRQ-53 앞쪽 3문항씩)
        deleteExistingItemsAndScales(shortId);
        insertScales(shortId);
        insertShortItemsAndChoices(shortId);
        insertShortNorms(shortId);
        jdbcTemplate.update("UPDATE assessment SET description = '한국인 회복탄력성 척도(KRQ) 간단 버전. 27문항(척도당 3문항), 5점 리커트. 약 8~10분.' WHERE id = ?", shortId);

        // 상세: 53문항 (KRQ-53 전체). 상세 검사가 없으면 생성
        if (detailId == null) {
            jdbcTemplate.update("INSERT INTO assessment (name, description, is_active) VALUES ('회복탄력성 검사 (상세)', '한국인 회복탄력성 척도(KRQ-53). 53문항. 약 15~20분.', TRUE)");
            detailId = getAssessmentId("회복탄력성 검사 (상세)");
        }
        if (detailId != null) {
            deleteExistingItemsAndScales(detailId);
            insertScales(detailId);
            insertFullItemsAndChoices(detailId);
            insertFullNorms(detailId);
            jdbcTemplate.update("UPDATE assessment SET description = '한국인 회복탄력성 척도(KRQ-53). 53문항, 9개 하위척도, 5점 리커트. 약 15~20분.' WHERE id = ?", detailId);
        }
    }

    private Long getAssessmentId(String name) {
        try {
            List<Long> list = jdbcTemplate.query(
                "SELECT id FROM assessment WHERE name = ? LIMIT 1",
                (rs, rowNum) -> rs.getLong("id"),
                name
            );
            return list.isEmpty() ? null : list.get(0);
        } catch (BadSqlGrammarException ex) {
            // assessment 테이블이 아직 없는 초기 상태에서는 null을 반환
            return null;
        }
    }

    private void deleteExistingItemsAndScales(Long assessmentId) {
        jdbcTemplate.update("DELETE FROM item_response WHERE item_id IN (SELECT id FROM item WHERE assessment_id = ?)", assessmentId);
        jdbcTemplate.update("DELETE FROM choice WHERE item_id IN (SELECT id FROM item WHERE assessment_id = ?)", assessmentId);
        jdbcTemplate.update("DELETE FROM item WHERE assessment_id = ?", assessmentId);
        jdbcTemplate.update("DELETE FROM norm WHERE assessment_id = ?", assessmentId);
        jdbcTemplate.update("DELETE FROM scale WHERE assessment_id = ?", assessmentId);
    }

    private void insertScales(Long assessmentId) {
        jdbcTemplate.update("INSERT INTO scale (assessment_id, code, name, description) VALUES (?, 'ER', '감정조절력', '감정 인식·통제')", assessmentId);
        jdbcTemplate.update("INSERT INTO scale (assessment_id, code, name, description) VALUES (?, 'IC', '충동통제력', '유혹·방해 극복')", assessmentId);
        jdbcTemplate.update("INSERT INTO scale (assessment_id, code, name, description) VALUES (?, 'CA', '원인분석력', '원인 분석·해결')", assessmentId);
        jdbcTemplate.update("INSERT INTO scale (assessment_id, code, name, description) VALUES (?, 'CO', '소통능력', '대화·표현')", assessmentId);
        jdbcTemplate.update("INSERT INTO scale (assessment_id, code, name, description) VALUES (?, 'EM', '공감능력', '타인 이해·공감')", assessmentId);
        jdbcTemplate.update("INSERT INTO scale (assessment_id, code, name, description) VALUES (?, 'SE', '자아확장력', '관계·지지')", assessmentId);
        jdbcTemplate.update("INSERT INTO scale (assessment_id, code, name, description) VALUES (?, 'OP', '자아낙관성', '낙관·통제신념')", assessmentId);
        jdbcTemplate.update("INSERT INTO scale (assessment_id, code, name, description) VALUES (?, 'LS', '생활만족도', '삶 만족')", assessmentId);
        jdbcTemplate.update("INSERT INTO scale (assessment_id, code, name, description) VALUES (?, 'GR', '감사하는 태도', '감사')", assessmentId);
    }

    /** 간단: 27문항 (척도당 3문항 = KRQ-53 각 척도 앞 3문항, 역채점 문항 없음) */
    private void insertShortItemsAndChoices(Long assessmentId) {
        String[] scaleCodes = {"ER","ER","ER","IC","IC","IC","CA","CA","CA","CO","CO","CO","EM","EM","EM","SE","SE","SE","OP","OP","OP","LS","LS","LS","GR","GR","GR"};
        int[] krqIndices = {0,1,2, 6,7,8, 12,13,14, 18,19,20, 24,25,26, 30,31,32, 36,37,38, 42,43,44, 47,48,49};
        String[] texts = getKrq53Texts();
        for (int i = 0; i < 27; i++) {
            Long scaleId = jdbcTemplate.queryForObject("SELECT id FROM scale WHERE assessment_id = ? AND code = ? LIMIT 1", Long.class, assessmentId, scaleCodes[i]);
            jdbcTemplate.update(
                "INSERT INTO item (assessment_id, scale_id, item_number, \"text\", is_required, is_reverse_scored, weight, sort_order) VALUES (?, ?, ?, ?, TRUE, FALSE, 1.0, ?)",
                assessmentId, scaleId, i + 1, texts[krqIndices[i]], i + 1);
        }
        jdbcTemplate.update("""
            INSERT INTO choice (item_id, label, value, sort_order)
            SELECT i.id, c.label, c.val, c.ord FROM item i
            CROSS JOIN (VALUES (1,'전혀 그렇지 않다',1),(2,'별로 그렇지 않다',2),(3,'보통이다',3),(4,'조금 그렇다',4),(5,'매우 그렇다',5)) AS c(ord, label, val)
            WHERE i.assessment_id = ?
            """, assessmentId);
    }

    private void insertShortNorms(Long assessmentId) {
        for (String code : List.of("ER","IC","CA","CO","EM","SE","OP","LS","GR")) {
            Long scaleId = jdbcTemplate.queryForObject("SELECT id FROM scale WHERE assessment_id = ? AND code = ? LIMIT 1", Long.class, assessmentId, code);
            jdbcTemplate.update("INSERT INTO norm (assessment_id, scale_id, group_code, mean, sd) VALUES (?, ?, 'default', 9.0, 2.5)", assessmentId, scaleId);
        }
        jdbcTemplate.update("INSERT INTO norm (assessment_id, scale_id, group_code, mean, sd) VALUES (?, NULL, 'default', 81.0, 8.0)", assessmentId);
    }

    /** 상세: 53문항 전체 KRQ-53 */
    private void insertFullItemsAndChoices(Long assessmentId) {
        String[] scaleCodes = {"ER","ER","ER","ER","ER","ER","IC","IC","IC","IC","IC","IC","CA","CA","CA","CA","CA","CA","CO","CO","CO","CO","CO","CO","EM","EM","EM","EM","EM","EM","SE","SE","SE","SE","SE","SE","OP","OP","OP","OP","OP","OP","LS","LS","LS","LS","LS","GR","GR","GR","GR","GR","GR"};
        boolean[] reverse = {false,false,false,true,true,true, false,false,false,true,true,true, false,false,false,true,true,true, false,false,false,true,true,true, false,false,false,true,true,true, false,false,false,true,true,true, false,false,false,true,true,true, false,false,false,false,false, false,false,false,true,true,true};
        String[] texts = getKrq53Texts();
        for (int n = 1; n <= 53; n++) {
            int idx = n - 1;
            String scaleCode = scaleCodes[idx];
            Long scaleId = jdbcTemplate.queryForObject("SELECT id FROM scale WHERE assessment_id = ? AND code = ? LIMIT 1", Long.class, assessmentId, scaleCode);
            jdbcTemplate.update(
                "INSERT INTO item (assessment_id, scale_id, item_number, \"text\", is_required, is_reverse_scored, weight, sort_order) VALUES (?, ?, ?, ?, TRUE, ?, 1.0, ?)",
                assessmentId, scaleId, n, texts[idx], reverse[idx], n);
        }
        jdbcTemplate.update("""
            INSERT INTO choice (item_id, label, value, sort_order)
            SELECT i.id, c.label, c.val, c.ord FROM item i
            CROSS JOIN (VALUES (1,'전혀 그렇지 않다',1),(2,'별로 그렇지 않다',2),(3,'보통이다',3),(4,'조금 그렇다',4),(5,'매우 그렇다',5)) AS c(ord, label, val)
            WHERE i.assessment_id = ?
            """, assessmentId);
    }

    private void insertFullNorms(Long assessmentId) {
        for (String code : List.of("ER","IC","CA","CO","EM","SE","OP","GR")) {
            Long scaleId = jdbcTemplate.queryForObject("SELECT id FROM scale WHERE assessment_id = ? AND code = ? LIMIT 1", Long.class, assessmentId, code);
            jdbcTemplate.update("INSERT INTO norm (assessment_id, scale_id, group_code, mean, sd) VALUES (?, ?, 'default', 18.0, 5.0)", assessmentId, scaleId);
        }
        Long lsId = jdbcTemplate.queryForObject("SELECT id FROM scale WHERE assessment_id = ? AND code = 'LS' LIMIT 1", Long.class, assessmentId);
        jdbcTemplate.update("INSERT INTO norm (assessment_id, scale_id, group_code, mean, sd) VALUES (?, ?, 'default', 15.0, 4.0)", assessmentId, lsId);
        jdbcTemplate.update("INSERT INTO norm (assessment_id, scale_id, group_code, mean, sd) VALUES (?, NULL, 'default', 159.0, 25.0)", assessmentId);
    }

    private String[] getKrq53Texts() {
        return new String[]{
            "나는 어려운 일이 닥쳤을 때 감정을 통제할 수 있다.",
            "내가 무슨 생각을 하면, 그 생각이 내 기분에 어떤 영향을 미칠지 잘 알아챈다.",
            "논쟁거리가 되는 문제를 가족이나 친구들과 토론할 때 내 감정을 잘 통제할 수 있다.",
            "집중해야 할 중요한 일이 생기면 신바람이 나기보다는 더 스트레스를 받는 편이다.",
            "나는 내 감정에 잘 휘말린다.",
            "때때로 내 감정적인 문제 때문에 학교나 직장에서 공부하거나 일할 때 집중하기 힘들다.",
            "당장 해야 할 일이 있으면 나는 어떠한 유혹이나 방해도 잘 이겨내고 할 일을 한다.",
            "아무리 당황스럽고 어려운 상황이 닥쳐도, 나는 내가 어떤 생각을 하고 있는지 스스로 잘 안다.",
            "누군가가 나에게 화를 낼 경우 나는 우선 그 사람의 의견을 잘 듣는다.",
            "일이 생각대로 잘 안 풀리면 쉽게 포기하는 편이다.",
            "평소 경제적인 소비나 지출 규모에 대해 별다른 계획 없이 지낸다.",
            "미리 계획을 세우기보다는 즉흥적으로 일을 처리하는 편이다.",
            "문제가 생기면 여러 가지 가능한 해결 방안에 대해 먼저 생각한 후에 해결하려고 노력한다.",
            "어려운 일이 생기면 그 원인이 무엇인지 신중하게 생각한 후에 그 문제를 해결하려고 노력한다.",
            "나는 대부분의 상황에서 문제의 원인을 잘 알고 있다고 믿는다.",
            "나는 사건이나 상황을 잘 파악하지 못한다는 이야기를 종종 듣는다.",
            "문제가 생기면 나는 성급하게 결론을 내린다는 이야기를 종종 듣는다.",
            "어려운 일이 생기면 그 원인을 완전히 이해하지 못했다 하더라도 일단 빨리 해결하는 것이 좋다고 생각한다.",
            "나는 분위기나 대화 상대에 따라 대화를 잘 이끌어 갈 수 있다.",
            "나는 재치 있는 농담을 잘한다.",
            "나는 내가 표현하고자 하는 바에 대한 적절한 문구나 단어를 잘 찾아낸다.",
            "나는 윗사람과 대화하는 것이 부담스럽다.",
            "나는 대화중에 다른 생각을 하느라 대화 내용을 놓칠 때가 종종 있다.",
            "대화를 할 때 하고 싶은 말을 다 하지 못하고 주저할 때가 종종 있다.",
            "사람들의 얼굴 표정을 보면 어떤 감정인지 알 수 있다.",
            "슬퍼하거나 화를 내거나 당황하는 사람을 보면 그들이 어떤 생각을 하는지 잘 알 수 있다.",
            "동료가 화를 낼 경우 나는 그 이유를 꽤 잘 아는 편이다.",
            "나는 사람들의 행동방식을 때로 이해하기 힘들다.",
            "친한 친구나 애인 혹은 배우자로부터 \"당신은 나를 이해 못해\"라는 말을 종종 듣는다.",
            "동료와 친구들은 내가 자기 말을 잘 듣지 않는다고 한다.",
            "나는 내 주변 사람들로부터 사랑과 관심을 받고 있다.",
            "나는 내 친구들을 정말로 좋아한다.",
            "내 주변 사람들은 내 기분을 잘 이해한다.",
            "서로 도움을 주고받는 친구가 별로 없는 편이다.",
            "나와 정기적으로 만나는 사람들은 대부분 나를 싫어하게 된다.",
            "서로 마음을 터놓고 얘기할 수 있는 친구가 거의 없다.",
            "열심히 일하면 언제나 보답이 있으리라고 생각한다.",
            "맞든 아니든 \"아무리 어려운 문제라도 나는 해결할 수 있다\"고 일단 믿는 것이 좋다고 생각한다.",
            "어려운 상황이 닥쳐도 나는 모든 일이 다 잘 해결될 거라고 확신한다.",
            "내가 어떤 일을 마치고 나면, 주변 사람들이 부정적인 평가를 할까봐 걱정한다.",
            "나에게 일어나는 대부분의 문제들은 나로서는 어쩔 수 없는 상황에 의해 발생한다고 믿는다.",
            "누가 나의 미래에 대해 물어보면 성공한 나의 모습을 상상하기 힘들다.",
            "내 삶은 내가 생각하는 이상적인 삶에 가깝다.",
            "내 인생의 여러 가지 조건들은 만족스럽다.",
            "나는 내 삶에 만족한다.",
            "나는 내 삶에서 중요하다고 생각한 것들을 다 갖고 있다.",
            "나는 다시 태어나도 나의 현재 삶을 다시 살고 싶다.",
            "나는 다양한 종류의 많은 사람들에게 고마움을 느낀다.",
            "내가 고맙게 여기는 것들을 모두 적는다면, 아주 긴 목록이 될 것이다.",
            "나이가 들어갈수록 내 삶의 일부가 된 사람, 사건, 생활에 대해 감사하는 마음이 더 커져간다.",
            "나는 감사해야 할 것이 별로 없다.",
            "세상을 둘러볼 때, 내가 고마워 할 것은 별로 없다.",
            "사람이나 일에 대한 고마움을 한참 시간이 지난 후에야 겨우 느낀다."
        };
    }
}
