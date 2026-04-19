package com.tst.psychAnalysis.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BoardPostUnlockRequest(
        @NotBlank(message = "비밀번호를 입력해 주세요.")
        @Size(max = 128, message = "비밀번호는 128자 이하여야 합니다.")
        String password
) {
}
