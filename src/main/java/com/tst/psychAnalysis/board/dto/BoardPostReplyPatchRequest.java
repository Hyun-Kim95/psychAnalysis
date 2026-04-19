package com.tst.psychAnalysis.board.dto;

import jakarta.validation.constraints.Size;

public record BoardPostReplyPatchRequest(
        @Size(max = 8000, message = "답변은 8000자 이하여야 합니다.")
        String reply
) {
}
