package com.tst.psychAnalysis.db;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.flyway.autoconfigure.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Flyway가 이미 적용된 마이그레이션과 체크섬만 어긋난 경우(예: 수동으로 flyway_schema_history에 넣은 행)
 * 기동 시 {@code repair()}로 히스토리 체크섬을 현재 SQL과 맞춘 뒤 {@code migrate()}를 실행한다.
 * <p>히스토리에 버전 행 자체가 빠져 있는 경우에는 repair로 해결되지 않으며,
 * DB에서 {@code flyway_schema_history} 정리 후 {@code baseline-on-migrate}로 복구해야 한다.
 */
@Configuration
@ConditionalOnProperty(name = "psychanalysis.flyway.repair-before-migrate", havingValue = "true")
public class FlywayRepairConfig {

    @Bean
    public FlywayMigrationStrategy flywayRepairThenMigrate() {
        return flyway -> {
            flyway.repair();
            flyway.migrate();
        };
    }
}
