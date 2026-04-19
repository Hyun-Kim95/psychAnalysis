package com.tst.psychAnalysis.board;

import com.tst.psychAnalysis.board.dto.BoardPostCreateRequest;
import com.tst.psychAnalysis.board.dto.BoardPostDetailDto;
import com.tst.psychAnalysis.board.dto.BoardPostPageResponse;
import com.tst.psychAnalysis.board.dto.BoardPostSummaryDto;
import com.tst.psychAnalysis.board.dto.BoardPostUnlockRequest;
import com.tst.psychAnalysis.common.ApiResponse;
import com.tst.psychAnalysis.common.ClientIpResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/posts")
    public ApiResponse<BoardPostPageResponse> listPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        int safeSize = Math.min(Math.max(size, 1), 50);
        int safePage = Math.max(page, 0);
        return ApiResponse.success(boardService.listPublic(PageRequest.of(safePage, safeSize)));
    }

    @GetMapping("/posts/{id}/summary")
    public ApiResponse<BoardPostSummaryDto> getPostSummary(@PathVariable UUID id) {
        return ApiResponse.success(boardService.getPublicSummary(id));
    }

    @PostMapping("/posts/{id}/unlock")
    public ApiResponse<BoardPostDetailDto> unlockPost(
            @PathVariable UUID id,
            @Valid @RequestBody BoardPostUnlockRequest body
    ) {
        return ApiResponse.success(boardService.unlockPublic(id, body.password()));
    }

    @PostMapping("/posts")
    public ApiResponse<BoardPostDetailDto> createPost(
            @Valid @RequestBody BoardPostCreateRequest request,
            HttpServletRequest httpRequest
    ) {
        return ApiResponse.success(boardService.create(request, ClientIpResolver.resolve(httpRequest)));
    }
}
