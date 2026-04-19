package com.tst.psychAnalysis.board.dto;

import java.util.UUID;

public record BoardAdminPostDetailDto(
        UUID id,
        String title,
        String body,
        String authorDisplay,
        String createdAt,
        boolean hidden,
        String submitterIp,
        String adminReply,
        String adminRepliedAt
) {
}
