package com.tst.psychAnalysis.board.dto;

import java.util.UUID;

public record BoardPostDetailDto(
        UUID id,
        String title,
        String body,
        String authorDisplay,
        String createdAt,
        String adminReply,
        String adminRepliedAt
) {
}
