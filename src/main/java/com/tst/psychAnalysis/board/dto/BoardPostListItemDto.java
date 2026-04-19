package com.tst.psychAnalysis.board.dto;

import java.util.UUID;

public record BoardPostListItemDto(
        UUID id,
        String title,
        String authorDisplay,
        String createdAt,
        boolean hasAdminReply
) {
}
