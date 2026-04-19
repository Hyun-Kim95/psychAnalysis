package com.tst.psychAnalysis.board;

import com.tst.psychAnalysis.admin.AdminAuthService;
import com.tst.psychAnalysis.board.dto.BoardAdminPostDetailDto;
import com.tst.psychAnalysis.board.dto.BoardAdminPostPageResponse;
import com.tst.psychAnalysis.board.dto.BoardPostHiddenPatchRequest;
import com.tst.psychAnalysis.board.dto.BoardPostReplyPatchRequest;
import com.tst.psychAnalysis.common.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/board")
public class BoardAdminController {

    private final AdminAuthService authService;
    private final BoardService boardService;

    public BoardAdminController(AdminAuthService authService, BoardService boardService) {
        this.authService = authService;
        this.boardService = boardService;
    }

    @GetMapping("/posts")
    public ApiResponse<BoardAdminPostPageResponse> listAll(
            @RequestHeader("X-Admin-Token") String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        requireAdmin(token);
        int safeSize = Math.min(Math.max(size, 1), 100);
        int safePage = Math.max(page, 0);
        return ApiResponse.success(boardService.listAllForAdmin(PageRequest.of(safePage, safeSize)));
    }

    @GetMapping("/posts/{id}")
    public ApiResponse<BoardAdminPostDetailDto> getOne(
            @RequestHeader("X-Admin-Token") String token,
            @PathVariable UUID id
    ) {
        requireAdmin(token);
        return ApiResponse.success(boardService.getForAdmin(id));
    }

    @PatchMapping("/posts/{id}")
    public ApiResponse<Void> patchHidden(
            @RequestHeader("X-Admin-Token") String token,
            @PathVariable UUID id,
            @RequestBody BoardPostHiddenPatchRequest body
    ) {
        requireAdmin(token);
        boardService.setHidden(id, body.hidden());
        return ApiResponse.success(null);
    }

    @PatchMapping("/posts/{id}/reply")
    public ApiResponse<Void> patchReply(
            @RequestHeader("X-Admin-Token") String token,
            @PathVariable UUID id,
            @Valid @RequestBody BoardPostReplyPatchRequest body
    ) {
        requireAdmin(token);
        boardService.setAdminReply(id, body);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/posts/{id}")
    public ApiResponse<Void> deletePost(
            @RequestHeader("X-Admin-Token") String token,
            @PathVariable UUID id
    ) {
        requireAdmin(token);
        boardService.deleteById(id);
        return ApiResponse.success(null);
    }

    private void requireAdmin(String token) {
        if (!authService.isValidToken(token)) {
            throw new IllegalArgumentException("인증이 필요합니다.");
        }
    }
}
