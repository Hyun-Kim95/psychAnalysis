package com.tst.psychAnalysis.board;

import com.tst.psychAnalysis.board.dto.BoardAdminPostDetailDto;
import com.tst.psychAnalysis.board.dto.BoardAdminPostPageResponse;
import com.tst.psychAnalysis.board.dto.BoardAdminPostRowDto;
import com.tst.psychAnalysis.board.dto.BoardPostCreateRequest;
import com.tst.psychAnalysis.board.dto.BoardPostDetailDto;
import com.tst.psychAnalysis.board.dto.BoardPostListItemDto;
import com.tst.psychAnalysis.board.dto.BoardPostPageResponse;
import com.tst.psychAnalysis.board.dto.BoardPostReplyPatchRequest;
import com.tst.psychAnalysis.board.dto.BoardPostSummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class BoardService {

    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final String BOARD_UNLOCK_FAIL = "글을 찾을 수 없거나 비밀번호가 올바르지 않습니다.";

    private final FeedbackPostRepository feedbackPostRepository;
    private final PasswordEncoder passwordEncoder;

    public BoardService(FeedbackPostRepository feedbackPostRepository, PasswordEncoder passwordEncoder) {
        this.feedbackPostRepository = feedbackPostRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public BoardPostPageResponse listPublic(Pageable pageable) {
        Page<FeedbackPost> page = feedbackPostRepository.findByHiddenFalseOrderByCreatedAtDesc(pageable);
        return new BoardPostPageResponse(
                page.getContent().stream().map(this::toListItem).toList(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize()
        );
    }

    @Transactional(readOnly = true)
    public BoardPostSummaryDto getPublicSummary(UUID id) {
        FeedbackPost post = feedbackPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("글을 찾을 수 없습니다."));
        if (post.isHidden()) {
            throw new IllegalArgumentException("글을 찾을 수 없습니다.");
        }
        boolean requiresPassword = post.getPasswordHash() != null && !post.getPasswordHash().isBlank();
        return new BoardPostSummaryDto(
                post.getId(),
                post.getTitle(),
                post.getAuthorDisplay(),
                formatTime(post.getCreatedAt()),
                requiresPassword
        );
    }

    @Transactional(readOnly = true)
    public BoardPostDetailDto unlockPublic(UUID id, String password) {
        FeedbackPost post = feedbackPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(BOARD_UNLOCK_FAIL));
        if (post.isHidden()) {
            throw new IllegalArgumentException(BOARD_UNLOCK_FAIL);
        }
        if (post.getPasswordHash() == null || post.getPasswordHash().isBlank()) {
            throw new IllegalArgumentException(BOARD_UNLOCK_FAIL);
        }
        if (password == null || !passwordEncoder.matches(password.trim(), post.getPasswordHash())) {
            throw new IllegalArgumentException(BOARD_UNLOCK_FAIL);
        }
        return toDetail(post);
    }

    @Transactional
    public BoardPostDetailDto create(BoardPostCreateRequest request, String clientIp) {
        String title = normalizeRequired(request.title(), 200, "제목");
        String body = normalizeRequired(request.body(), 8000, "내용");
        String author = normalizeOptional(request.authorDisplay(), 64);
        String rawPassword = normalizeRequired(request.password(), 128, "비밀번호").trim();
        if (rawPassword.length() < 8) {
            throw new IllegalArgumentException("비밀번호는 8자 이상이어야 합니다.");
        }

        FeedbackPost post = new FeedbackPost();
        post.setId(UUID.randomUUID());
        post.setTitle(title);
        post.setBody(body);
        post.setAuthorDisplay(author);
        post.setCreatedAt(LocalDateTime.now());
        post.setHidden(false);
        post.setPasswordHash(passwordEncoder.encode(rawPassword));
        post.setSubmitterIp(blankToNull(clientIp));
        feedbackPostRepository.save(post);
        return toDetail(post);
    }

    @Transactional(readOnly = true)
    public BoardAdminPostPageResponse listAllForAdmin(Pageable pageable) {
        Page<FeedbackPost> page = feedbackPostRepository.findAllByOrderByCreatedAtDesc(pageable);
        return new BoardAdminPostPageResponse(
                page.getContent().stream().map(this::toAdminRow).toList(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize()
        );
    }

    @Transactional(readOnly = true)
    public BoardAdminPostDetailDto getForAdmin(UUID id) {
        FeedbackPost post = feedbackPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("글을 찾을 수 없습니다."));
        return toAdminDetail(post);
    }

    @Transactional
    public void setHidden(UUID id, boolean hidden) {
        FeedbackPost post = feedbackPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("글을 찾을 수 없습니다."));
        post.setHidden(hidden);
    }

    @Transactional
    public void setAdminReply(UUID id, BoardPostReplyPatchRequest body) {
        FeedbackPost post = feedbackPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("글을 찾을 수 없습니다."));
        String reply = body != null && body.reply() != null ? body.reply().trim() : "";
        if (reply.isEmpty()) {
            post.setAdminReply(null);
            post.setAdminRepliedAt(null);
        } else {
            if (reply.length() > 8000) {
                throw new IllegalArgumentException("답변은 8000자 이하여야 합니다.");
            }
            post.setAdminReply(reply);
            post.setAdminRepliedAt(LocalDateTime.now());
        }
    }

    @Transactional
    public void deleteById(UUID id) {
        if (!feedbackPostRepository.existsById(id)) {
            throw new IllegalArgumentException("글을 찾을 수 없습니다.");
        }
        feedbackPostRepository.deleteById(id);
    }

    private BoardPostListItemDto toListItem(FeedbackPost p) {
        boolean hasReply = p.getAdminReply() != null && !p.getAdminReply().isBlank();
        return new BoardPostListItemDto(
                p.getId(),
                p.getTitle(),
                p.getAuthorDisplay(),
                formatTime(p.getCreatedAt()),
                hasReply
        );
    }

    private BoardAdminPostRowDto toAdminRow(FeedbackPost p) {
        String ip = p.getSubmitterIp() != null ? p.getSubmitterIp() : "";
        boolean hasReply = p.getAdminReply() != null && !p.getAdminReply().isBlank();
        return new BoardAdminPostRowDto(
                p.getId(),
                p.getTitle(),
                p.getAuthorDisplay(),
                formatTime(p.getCreatedAt()),
                p.isHidden(),
                ip,
                hasReply
        );
    }

    private BoardPostDetailDto toDetail(FeedbackPost p) {
        return new BoardPostDetailDto(
                p.getId(),
                p.getTitle(),
                p.getBody(),
                p.getAuthorDisplay(),
                formatTime(p.getCreatedAt()),
                p.getAdminReply(),
                p.getAdminRepliedAt() != null ? formatTime(p.getAdminRepliedAt()) : null
        );
    }

    private BoardAdminPostDetailDto toAdminDetail(FeedbackPost p) {
        return new BoardAdminPostDetailDto(
                p.getId(),
                p.getTitle(),
                p.getBody(),
                p.getAuthorDisplay(),
                formatTime(p.getCreatedAt()),
                p.isHidden(),
                p.getSubmitterIp() != null ? p.getSubmitterIp() : "",
                p.getAdminReply(),
                p.getAdminRepliedAt() != null ? formatTime(p.getAdminRepliedAt()) : null
        );
    }

    private static String formatTime(LocalDateTime t) {
        return t != null ? ISO.format(t) : "";
    }

    private static String normalizeRequired(String raw, int maxLen, String label) {
        if (raw == null) {
            throw new IllegalArgumentException(label + "을(를) 입력해 주세요.");
        }
        String s = raw.trim();
        if (s.isEmpty()) {
            throw new IllegalArgumentException(label + "을(를) 입력해 주세요.");
        }
        if (s.length() > maxLen) {
            throw new IllegalArgumentException(label + "은(는) " + maxLen + "자 이하여야 합니다.");
        }
        return s;
    }

    private static String normalizeOptional(String raw, int maxLen) {
        if (raw == null) {
            return null;
        }
        String s = raw.trim();
        if (s.isEmpty()) {
            return null;
        }
        if (s.length() > maxLen) {
            throw new IllegalArgumentException("표시 이름은 " + maxLen + "자 이하여야 합니다.");
        }
        return s;
    }

    private static String blankToNull(String s) {
        if (s == null || s.isBlank()) {
            return null;
        }
        return s;
    }
}
