package com.tst.psychAnalysis.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BoardPostCreateRequest(
        @NotBlank(message = "제목을 입력해 주세요.")
        @Size(max = 200, message = "제목은 200자 이하여야 합니다.")
        String title,
        @NotBlank(message = "내용을 입력해 주세요.")
        @Size(max = 8000, message = "내용은 8000자 이하여야 합니다.")
        String body,
        @Size(max = 64, message = "표시 이름은 64자 이하여야 합니다.")
        String authorDisplay,
        @NotBlank(message = "비밀번호를 입력해 주세요.")
        @Size(max = 128, message = "비밀번호는 128자 이하여야 합니다.")
        String password
) {
}
