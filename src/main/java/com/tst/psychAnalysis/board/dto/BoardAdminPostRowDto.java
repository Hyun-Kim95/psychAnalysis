package com.tst.psychAnalysis.board.dto;

import java.util.UUID;

public record BoardAdminPostRowDto(
        UUID id,
        String title,
        String authorDisplay,
        String createdAt,
        boolean hidden,
        String submitterIp,
        boolean hasAdminReply
) {
}
