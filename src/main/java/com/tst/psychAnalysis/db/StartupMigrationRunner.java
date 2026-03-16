package com.tst.psychAnalysis.db;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * 기동 시 BAI 선택지·문항 반영.
 * - BAI 불안검사(간단): 11문항 (공식 1~11번, 상세와 약 2배 차이)
 * - BAI 불안검사 (상세): 21문항 (공식 전체)
 * Flyway SQL(V29~V32) 미적용 시 재시작 시 자동 실행 후 flyway_schema_history에 기록합니다.
 */
@Component
@Order(Integer.MAX_VALUE)
public class StartupMigrationRunner implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;

    public StartupMigrationRunner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            if (alreadyApplied()) {
                return;
            }
            runMigration();
            recordInFlywayHistory();
        } catch (BadSqlGrammarException ex) {
            // 초기 스키마(assessment/item/choice/norm/flyway_schema_history 등)가 없는 경우에는
            // BAI 마이그레이션을 건너뛰고 애플리케이션 기동을 계속 진행
        }
    }

    private boolean alreadyApplied() {
        try {
            Long shortCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM item i JOIN assessment a ON a.id = i.assessment_id WHERE a.name = 'BAI 불안검사'",
                Long.class);
            Long detailCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM item i JOIN assessment a ON a.id = i.assessment_id WHERE a.name = 'BAI 불안검사 (상세)'",
                Long.class);
            return shortCount != null && shortCount == 11 && detailCount != null && detailCount == 21;
        } catch (BadSqlGrammarException ex) {
            // item/assessment 테이블이 아직 없는 초기 상태에서는 마이그레이션을 수행하도록 처리
            return false;
        }
    }

    private void runMigration() {
        jdbcTemplate.update("UPDATE choice SET label = '거의 항상' WHERE label = '거의 매일'");

        jdbcTemplate.update("""
            UPDATE choice SET label = CASE value
              WHEN 0 THEN '전혀 느끼지 않았다'
              WHEN 1 THEN '조금 느꼈다'
              WHEN 2 THEN '상당히 느꼈다'
              WHEN 3 THEN '심하게 느꼈다'
              ELSE label END
            WHERE item_id IN (
              SELECT i.id FROM item i
              WHERE i.assessment_id IN (SELECT id FROM assessment WHERE name = 'BAI 불안검사' OR name = 'BAI 불안검사 (상세)')
            )
            """);

        updateBaiItems("BAI 불안검사", 1, 11);
        insertBaiItems10To11("BAI 불안검사");
        insertBaiChoicesForNewItems("BAI 불안검사", 10);
        deleteBaiItemsFrom12("BAI 불안검사");

        updateBaiItems("BAI 불안검사 (상세)", 1, 18);
        insertBaiItems19To21("BAI 불안검사 (상세)");
        insertBaiChoicesForNewItems("BAI 불안검사 (상세)", 19);

        jdbcTemplate.update("""
            UPDATE norm SET mean = 11.0, sd = 7.0
            WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BAI 불안검사' LIMIT 1)
            """);
        jdbcTemplate.update("""
            UPDATE norm SET mean = 18.0, sd = 12.0
            WHERE assessment_id = (SELECT id FROM assessment WHERE name = 'BAI 불안검사 (상세)' LIMIT 1)
            """);
        jdbcTemplate.update("""
            UPDATE assessment SET description = 'Beck Anxiety Inventory(벡 불안척도) 간단 버전. 11문항, 0~3점. 지난 한 주간 경험 기준. 약 3~4분.'
            WHERE name = 'BAI 불안검사'
            """);
        jdbcTemplate.update("""
            UPDATE assessment SET description = 'Beck Anxiety Inventory(벡 불안척도). 21문항, 0~3점. 지난 한 주간 경험 기준. 약 5~7분.'
            WHERE name = 'BAI 불안검사 (상세)'
            """);
    }

    private void updateBaiItems(String assessmentName, int from, int to) {
        String[] texts = {
            "가끔씩 몸이 저리고 쑤시며 감각이 마비된 느낌을 받는다.",
            "흥분된 느낌을 받는다.",
            "가끔씩 다리가 떨리곤 한다.",
            "편안하게 쉴 수가 없다.",
            "매우 나쁜 일이 일어날 것 같은 두려움을 느낀다.",
            "어지러움(현기증)을 느낀다.",
            "가끔씩 심장이 두근거리고 빨리 뛴다.",
            "침착하지 못한다.",
            "자주 겁을 먹고 무서움을 느낀다.",
            "신경이 과민 되어 왔다.",
            "가끔씩 숨이 막히고 질식할 것 같다.",
            "자주 손이 떨린다.",
            "안절부절못해 한다.",
            "미칠 것 같은 두려움을 느낀다.",
            "가끔씩 숨쉬기 곤란할 때가 있다.",
            "죽을 것 같은 두려움을 느낀다.",
            "불안한 상태에 있다.",
            "자주 소화가 잘 안되고 뱃속이 불편하다.",
        };
        for (int n = from; n <= to; n++) {
            int idx = n - 1;
            if (idx < texts.length) {
                jdbcTemplate.update(
                    "UPDATE item SET \"text\" = ? WHERE assessment_id = (SELECT id FROM assessment WHERE name = ? LIMIT 1) AND item_number = ?",
                    texts[idx], assessmentName, n);
            }
        }
    }

    /** 간단 버전용: 10·11번 문항이 없으면 추가 (기존 9문항만 있을 때 11문항으로 확장) */
    private void insertBaiItems10To11(String assessmentName) {
        String sql = """
            INSERT INTO item (assessment_id, scale_id, item_number, \"text\", is_required, is_reverse_scored, weight, sort_order)
            SELECT a.id, s.id, v.n, v.txt, TRUE, FALSE, 1.0, v.n
            FROM assessment a
            JOIN scale s ON s.assessment_id = a.id AND s.code = 'A'
            CROSS JOIN (VALUES
              (10, '신경이 과민 되어 왔다.'),
              (11, '가끔씩 숨이 막히고 질식할 것 같다.')
            ) AS v(n, txt)
            WHERE a.name = ?
              AND NOT EXISTS (SELECT 1 FROM item i WHERE i.assessment_id = a.id AND i.item_number = v.n)
            """;
        jdbcTemplate.update(sql, assessmentName);
    }

    /** 간단 버전은 11문항만 유지. 12번 이상 문항·선택지 삭제 (참조하는 item_response 먼저 삭제) */
    private void deleteBaiItemsFrom12(String assessmentName) {
        jdbcTemplate.update("""
            DELETE FROM item_response WHERE item_id IN (
              SELECT i.id FROM item i JOIN assessment a ON a.id = i.assessment_id
              WHERE a.name = ? AND i.item_number >= 12
            )
            """, assessmentName);
        jdbcTemplate.update("""
            DELETE FROM choice WHERE item_id IN (
              SELECT i.id FROM item i JOIN assessment a ON a.id = i.assessment_id
              WHERE a.name = ? AND i.item_number >= 12
            )
            """, assessmentName);
        jdbcTemplate.update("""
            DELETE FROM item WHERE assessment_id = (SELECT id FROM assessment WHERE name = ? LIMIT 1) AND item_number >= 12
            """, assessmentName);
    }

    private void insertBaiItems19To21(String assessmentName) {
        String sql = """
            INSERT INTO item (assessment_id, scale_id, item_number, \"text\", is_required, is_reverse_scored, weight, sort_order)
            SELECT a.id, s.id, v.n, v.txt, TRUE, FALSE, 1.0, v.n
            FROM assessment a
            JOIN scale s ON s.assessment_id = a.id AND s.code = 'A'
            CROSS JOIN (VALUES
              (19, '가끔씩 기절할 것 같다.'),
              (20, '자주 얼굴이 붉어지곤 한다.'),
              (21, '땀을 많이 흘린다. (더위로 인한 경우는 제외)')
            ) AS v(n, txt)
            WHERE a.name = ?
              AND NOT EXISTS (SELECT 1 FROM item i WHERE i.assessment_id = a.id AND i.item_number = v.n)
            """;
        jdbcTemplate.update(sql, assessmentName);
    }

    private void insertBaiChoicesForNewItems(String assessmentName, int minItemNumber) {
        String sql = """
            INSERT INTO choice (item_id, label, value, sort_order)
            SELECT i.id, c.label, c.value, c.ord
            FROM item i
            JOIN assessment a ON a.id = i.assessment_id
            CROSS JOIN (VALUES (1,'전혀 느끼지 않았다',0),(2,'조금 느꼈다',1),(3,'상당히 느꼈다',2),(4,'심하게 느꼈다',3)) AS c(ord, label, value)
            WHERE a.name = ? AND i.item_number >= ?
              AND NOT EXISTS (SELECT 1 FROM choice ch WHERE ch.item_id = i.id)
            """;
        jdbcTemplate.update(sql, assessmentName, minItemNumber);
    }

    private void recordInFlywayHistory() {
        String[] versions = {"29", "30", "31", "32"};
        String[] descriptions = {"choice label almost always", "bai official 21 items", "bai official 21 items repair", "choice and bai official"};
        String[] scripts = {"V29__choice_label_almost_always.sql", "V30__bai_official_21_items.sql", "V31__bai_official_21_items_repair.sql", "V32__choice_and_bai_official.sql"};
        for (int i = 0; i < versions.length; i++) {
            if (alreadyInHistory(versions[i])) continue;
            Integer nextRank = jdbcTemplate.queryForObject("SELECT COALESCE(MAX(installed_rank), 0) + 1 FROM flyway_schema_history", Integer.class);
            jdbcTemplate.update("""
                INSERT INTO flyway_schema_history (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success)
                VALUES (?, ?, ?, 'SQL', ?, 0, current_user, NOW(), 0, true)
                """, nextRank, versions[i], descriptions[i], scripts[i]);
        }
    }

    private boolean alreadyInHistory(String version) {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM flyway_schema_history WHERE version = ?", Integer.class, version);
        return count != null && count > 0;
    }
}
