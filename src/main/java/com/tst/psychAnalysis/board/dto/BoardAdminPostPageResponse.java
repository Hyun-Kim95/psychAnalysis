package com.tst.psychAnalysis.board.dto;

import java.util.List;

public record BoardAdminPostPageResponse(
        List<BoardAdminPostRowDto> content,
        long totalElements,
        int totalPages,
        int number,
        int size
) {
}
