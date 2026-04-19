package com.tst.psychAnalysis.board;

import com.tst.psychAnalysis.board.dto.BoardPostCreateRequest;
import com.tst.psychAnalysis.board.dto.BoardPostReplyPatchRequest;
import com.tst.psychAnalysis.board.dto.BoardPostSummaryDto;
import com.tst.psychAnalysis.board.dto.BoardPostDetailDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doAnswer;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    private FeedbackPostRepository feedbackPostRepository;

    private PasswordEncoder passwordEncoder;
    private BoardService boardService;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        boardService = new BoardService(feedbackPostRepository, passwordEncoder);
    }

    @Test
    void create_storesBcryptHashAndSubmitterIp() {
        UUID id = UUID.randomUUID();
        doAnswer(invocation -> {
            FeedbackPost p = invocation.getArgument(0);
            p.setId(id);
            return p;
        }).when(feedbackPostRepository).save(any(FeedbackPost.class));

        boardService.create(
                new BoardPostCreateRequest("제목", "본문입니다.", null, "secret123"),
                "203.0.113.10"
        );

        ArgumentCaptor<FeedbackPost> cap = ArgumentCaptor.forClass(FeedbackPost.class);
        verify(feedbackPostRepository).save(cap.capture());
        FeedbackPost saved = cap.getValue();
        assertThat(saved.getPasswordHash()).isNotBlank();
        assertThat(passwordEncoder.matches("secret123", saved.getPasswordHash())).isTrue();
        assertThat(saved.getSubmitterIp()).isEqualTo("203.0.113.10");
        assertThat(saved.getTitle()).isEqualTo("제목");
        assertThat(saved.getBody()).isEqualTo("본문입니다.");
    }

    @Test
    void create_rejectsShortPassword() {
        assertThatThrownBy(() -> boardService.create(
                new BoardPostCreateRequest("제목", "본문", null, "short"),
                "127.0.0.1"
        )).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("8자");
    }

    @Test
    void unlockPublic_succeedsWhenPasswordMatches() {
        UUID id = UUID.randomUUID();
        FeedbackPost post = new FeedbackPost();
        post.setId(id);
        post.setTitle("t");
        post.setBody("b");
        post.setAuthorDisplay(null);
        post.setCreatedAt(LocalDateTime.now());
        post.setHidden(false);
        post.setPasswordHash(passwordEncoder.encode("mypass999"));
        post.setSubmitterIp("10.0.0.1");
        when(feedbackPostRepository.findById(id)).thenReturn(Optional.of(post));

        BoardPostDetailDto dto = boardService.unlockPublic(id, "mypass999");
        assertThat(dto.body()).isEqualTo("b");
    }

    @Test
    void unlockPublic_sameMessageWhenWrongPassword() {
        UUID id = UUID.randomUUID();
        FeedbackPost post = new FeedbackPost();
        post.setId(id);
        post.setTitle("t");
        post.setBody("b");
        post.setHidden(false);
        post.setPasswordHash(passwordEncoder.encode("right"));
        when(feedbackPostRepository.findById(id)).thenReturn(Optional.of(post));

        assertThatThrownBy(() -> boardService.unlockPublic(id, "wrong"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비밀번호");
    }

    @Test
    void getPublicSummary_requiresPasswordWhenHashPresent() {
        UUID id = UUID.randomUUID();
        FeedbackPost post = new FeedbackPost();
        post.setId(id);
        post.setTitle("t");
        post.setBody("secret body");
        post.setAuthorDisplay("a");
        post.setCreatedAt(LocalDateTime.parse("2024-01-02T12:00:00"));
        post.setHidden(false);
        post.setPasswordHash("hash");
        when(feedbackPostRepository.findById(id)).thenReturn(Optional.of(post));

        BoardPostSummaryDto s = boardService.getPublicSummary(id);
        assertThat(s.requiresPassword()).isTrue();
        assertThat(s.title()).isEqualTo("t");
    }

    @Test
    void setAdminReply_clearsWhenEmpty() {
        UUID id = UUID.randomUUID();
        FeedbackPost post = new FeedbackPost();
        post.setId(id);
        post.setAdminReply("old");
        post.setAdminRepliedAt(LocalDateTime.now());
        when(feedbackPostRepository.findById(id)).thenReturn(Optional.of(post));

        boardService.setAdminReply(id, new BoardPostReplyPatchRequest("  "));
        assertThat(post.getAdminReply()).isNull();
        assertThat(post.getAdminRepliedAt()).isNull();
    }
}
