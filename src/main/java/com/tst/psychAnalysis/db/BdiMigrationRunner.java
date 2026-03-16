package com.tst.psychAnalysis.db;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

/**
 * BDI 우울검사 공식 반영: 간단 11문항, 상세 21문항(Beck Depression Inventory 공식).
 * 지난 2주 기준, 문항별 0~3점, 해석 0~9 정상, 10~15 가벼운 우울, 16~25 중등도, 26~63 심한 우울.
 */
@Component
@Order(Integer.MAX_VALUE - 2)
public class BdiMigrationRunner implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;

    /** 공식 BDI 21문항 문항명 (이미지/공식 양식 기준) */
    private static final String[] BDI_21_TITLES = {
        "슬픈 기분",
        "비관적 사고",
        "실패감",
        "만족감 감소",
        "죄책감",
        "처벌감",
        "자기 실망",
        "자기 비난",
        "자살 사고",
        "울음",
        "짜증",
        "다른 사람에 대한 관심 감소",
        "결정 부담",
        "신체적인 상",
        "시작하기 어려움",
        "수면 장애",
        "피곤함",
        "식욕 저하",
        "체중 감소",
        "건강 염려",
        "성욕 감퇴",
    };

    public BdiMigrationRunner(JdbcTemplate jdbcTemplate) {
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
                "SELECT COUNT(*) FROM item i JOIN assessment a ON a.id = i.assessment_id WHERE a.name = 'BDI 우울검사'",
                (rs, rn) -> rs.getLong(1)).stream().findFirst().orElse(0L);
            Long detailCount = jdbcTemplate.query(
                "SELECT COUNT(*) FROM item i JOIN assessment a ON a.id = i.assessment_id WHERE a.name = 'BDI 우울검사 (상세)'",
                (rs, rn) -> rs.getLong(1)).stream().findFirst().orElse(0L);
            return shortCount == 11 && detailCount == 21;
        } catch (BadSqlGrammarException ex) {
            // item/assessment 테이블이 아직 없는 초기 상태에서는 마이그레이션을 수행하도록 처리
            return false;
        }
    }

    private void runMigration() {
        Long shortId = getAssessmentId("BDI 우울검사");
        Long detailId = getAssessmentId("BDI 우울검사 (상세)");
        if (shortId == null) return;

        // 상세: 21문항 (없으면 생성)
        if (detailId == null) {
            jdbcTemplate.update("INSERT INTO assessment (name, description, is_active) VALUES ('BDI 우울검사 (상세)', 'Beck Depression Inventory(벡 우울척도) 공식 21문항. 지난 2주 기준, 0~3점. 약 10~15분.', TRUE)");
            detailId = getAssessmentId("BDI 우울검사 (상세)");
            ensureScaleD(detailId);
        }
        if (detailId != null) {
            deleteItemsAndNormsOnly(detailId);
            insertBdiItemsAndChoices(detailId, 1, 21);
            insertBdiNorms(detailId, 21);
            jdbcTemplate.update("UPDATE assessment SET description = 'Beck Depression Inventory(벡 우울척도) 공식 21문항. 지난 2주 기준, 0~3점. 약 10~15분.' WHERE id = ?", detailId);
        }

        // 간단: 11문항
        ensureScaleD(shortId);
        deleteItemsAndNormsOnly(shortId);
        insertBdiItemsAndChoices(shortId, 1, 11);
        insertBdiNorms(shortId, 11);
        jdbcTemplate.update("UPDATE assessment SET description = 'Beck Depression Inventory(벡 우울척도) 간단 버전. 11문항, 지난 2주 기준, 0~3점. 약 5~7분.' WHERE id = ?", shortId);
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

    private void ensureScaleD(Long assessmentId) {
        Integer exists = null;
        try {
            exists = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM scale WHERE assessment_id = ? AND code = 'D'",
                Integer.class,
                assessmentId
            );
        } catch (BadSqlGrammarException ex) {
            // scale 테이블이 아직 없으면 아래 INSERT에서 함께 생성되므로 무시
        }
        if (exists == null || exists == 0) {
            jdbcTemplate.update("INSERT INTO scale (assessment_id, code, name, description) VALUES (?, 'D', '우울', '우울 기분·동기 등')", assessmentId);
        }
    }

    /** scale는 유지하고 item/choice/norm만 삭제 */
    private void deleteItemsAndNormsOnly(Long assessmentId) {
        jdbcTemplate.update("DELETE FROM item_response WHERE item_id IN (SELECT id FROM item WHERE assessment_id = ?)", assessmentId);
        jdbcTemplate.update("DELETE FROM choice WHERE item_id IN (SELECT id FROM item WHERE assessment_id = ?)", assessmentId);
        jdbcTemplate.update("DELETE FROM item WHERE assessment_id = ?", assessmentId);
        jdbcTemplate.update("DELETE FROM norm WHERE assessment_id = ?", assessmentId);
    }

    private void insertBdiItemsAndChoices(Long assessmentId, int from, int to) {
        Long scaleId;
        try {
            scaleId = jdbcTemplate.queryForObject(
                "SELECT id FROM scale WHERE assessment_id = ? AND code = 'D' LIMIT 1",
                Long.class,
                assessmentId
            );
        } catch (BadSqlGrammarException | EmptyResultDataAccessException ex) {
            // scale 테이블이 없거나 해당 assessment에 'D' 스케일이 아직 없으면 BDI 마이그레이션을 건너뜀
            return;
        }
        for (int n = from; n <= to; n++) {
            int idx = n - 1;
            String title = idx < BDI_21_TITLES.length ? BDI_21_TITLES[idx] : "문항 " + n;
            String itemText = n + ". " + title;
            jdbcTemplate.update(
                "INSERT INTO item (assessment_id, scale_id, item_number, \"text\", is_required, is_reverse_scored, weight, sort_order) VALUES (?, ?, ?, ?, TRUE, FALSE, 1.0, ?)",
                assessmentId, scaleId, n, itemText, n);
        }
        // 0~3점 공통 선택지 (공식 BDI 스타일)
        jdbcTemplate.update("""
            INSERT INTO choice (item_id, label, value, sort_order)
            SELECT i.id, c.label, c.val, c.ord FROM item i
            CROSS JOIN (VALUES (1,'전혀 없음',0),(2,'가끔',1),(3,'자주',2),(4,'거의 항상',3)) AS c(ord, label, val)
            WHERE i.assessment_id = ? AND i.item_number >= ? AND i.item_number <= ?
            """, assessmentId, from, to);
    }

    private void insertBdiNorms(Long assessmentId, int itemCount) {
        Long scaleId;
        try {
            scaleId = jdbcTemplate.queryForObject(
                "SELECT id FROM scale WHERE assessment_id = ? AND code = 'D' LIMIT 1",
                Long.class,
                assessmentId
            );
        } catch (BadSqlGrammarException | EmptyResultDataAccessException ex) {
            // scale 테이블이 없거나 해당 assessment에 'D' 스케일이 아직 없으면 norm 삽입을 건너뜀
            return;
        }
        double mean = itemCount * 1.5; // 0~3 평균 1.5 가정
        double sd = itemCount * 0.9;
        jdbcTemplate.update("INSERT INTO norm (assessment_id, scale_id, group_code, mean, sd) VALUES (?, ?, 'default', ?, ?)", assessmentId, scaleId, mean, sd);
        jdbcTemplate.update("INSERT INTO norm (assessment_id, scale_id, group_code, mean, sd) VALUES (?, NULL, 'default', ?, ?)", assessmentId, mean, sd);
    }
}
