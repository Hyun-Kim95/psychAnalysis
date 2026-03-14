package com.tst.psychAnalysis.response;

import com.tst.psychAnalysis.assessment.Assessment;
import com.tst.psychAnalysis.assessment.AssessmentRepository;
import com.tst.psychAnalysis.assessment.Item;
import com.tst.psychAnalysis.assessment.ItemRepository;
import com.tst.psychAnalysis.admin.AccessLogService;
import com.tst.psychAnalysis.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/response-sessions")
public class ResponseSessionController {

    private final AssessmentRepository assessmentRepository;
    private final ResponseSessionRepository responseSessionRepository;
    private final ItemRepository itemRepository;
    private final ItemResponseRepository itemResponseRepository;
    private final ResultRepository resultRepository;
    private final ScoringService scoringService;
    private final AccessLogService accessLogService;

    public ResponseSessionController(AssessmentRepository assessmentRepository,
                                     ResponseSessionRepository responseSessionRepository,
                                     ItemRepository itemRepository,
                                     ItemResponseRepository itemResponseRepository,
                                     ResultRepository resultRepository,
                                     ScoringService scoringService,
                                     AccessLogService accessLogService) {
        this.assessmentRepository = assessmentRepository;
        this.responseSessionRepository = responseSessionRepository;
        this.itemRepository = itemRepository;
        this.itemResponseRepository = itemResponseRepository;
        this.resultRepository = resultRepository;
        this.scoringService = scoringService;
        this.accessLogService = accessLogService;
    }

    @PostMapping
    public ApiResponse<CreateSessionResponse> createSession(@RequestBody CreateSessionRequest request,
                                                            HttpServletRequest httpRequest) {
        Assessment assessment = request.assessmentId() != null
                ? assessmentRepository.findById(request.assessmentId())
                        .orElseThrow(() -> new IllegalArgumentException("검사를 찾을 수 없습니다."))
                : assessmentRepository.findFirstByIsActiveTrue()
                        .orElseThrow(() -> new IllegalStateException("활성화된 검사가 없습니다."));
        if (!assessment.isActive()) {
            throw new IllegalArgumentException("비활성화된 검사입니다.");
        }

        ResponseSession session = new ResponseSession();
        session.setId(UUID.randomUUID());
        session.setAssessment(assessment);
        session.setGroupCode(request.groupCode());
        session.setStartedAt(LocalDateTime.now());
        session.setCompleted(false);

        ResponseSession saved = responseSessionRepository.save(session);

        accessLogService.log(httpRequest, "검사 시작", saved.getId());

        return ApiResponse.success(new CreateSessionResponse(saved.getId()));
    }

    @PostMapping("/{id}/submit")
    public ApiResponse<SubmitResponse> submit(@PathVariable("id") UUID sessionId,
                                              @RequestBody SubmitRequest request,
                                              HttpServletRequest httpRequest) {
        ResponseSession session = responseSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("응답 세션을 찾을 수 없습니다."));

        if (session.isCompleted()) {
            throw new IllegalStateException("이미 완료된 세션입니다.");
        }

        List<Item> items = itemRepository.findByAssessmentIdOrderBySortOrderAsc(session.getAssessment().getId());

        for (SubmitRequest.Answer answer : request.answers()) {
            Item item = items.stream()
                    .filter(i -> i.getId().equals(answer.itemId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("문항을 찾을 수 없습니다."));

            ItemResponse ir = new ItemResponse();
            ir.setResponseSession(session);
            ir.setItem(item);
            ir.setRawValue(answer.value());
            itemResponseRepository.save(ir);
        }

        session.setCompleted(true);
        session.setSubmittedAt(LocalDateTime.now());
        responseSessionRepository.save(session);

        Result scoredResult = scoringService.score(session);
        Result savedResult = resultRepository.save(scoredResult);

        accessLogService.log(httpRequest, "검사 제출", session.getId());

        return ApiResponse.success(new SubmitResponse(savedResult.getId()));
    }
}

