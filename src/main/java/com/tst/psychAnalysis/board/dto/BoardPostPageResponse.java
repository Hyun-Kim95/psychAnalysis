package com.tst.psychAnalysis.board.dto;

import java.util.List;

public record BoardPostPageResponse(
        List<BoardPostListItemDto> content,
        long totalElements,
        int totalPages,
        int number,
        int size
) {
}
