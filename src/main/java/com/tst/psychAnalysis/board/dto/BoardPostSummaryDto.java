package com.tst.psychAnalysis.board.dto;

import java.util.UUID;

/**
 * @param requiresPassword 비밀번호로 본문 열람. false인 행은 구버전(비밀번호 없음)으로 공개 본문 열람 불가.
 */
public record BoardPostSummaryDto(
        UUID id,
        String title,
        String authorDisplay,
        String createdAt,
        boolean requiresPassword
) {
}
