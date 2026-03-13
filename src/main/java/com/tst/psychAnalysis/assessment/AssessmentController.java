package com.tst.psychAnalysis.assessment;

import com.tst.psychAnalysis.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/assessments")
public class AssessmentController {

    private final AssessmentRepository assessmentRepository;
    private final ItemRepository itemRepository;
    private final ChoiceRepository choiceRepository;

    public AssessmentController(AssessmentRepository assessmentRepository,
                                ItemRepository itemRepository,
                                ChoiceRepository choiceRepository) {
        this.assessmentRepository = assessmentRepository;
        this.itemRepository = itemRepository;
        this.choiceRepository = choiceRepository;
    }

    @GetMapping("/current")
    public ApiResponse<AssessmentSummaryResponse> getCurrentAssessment() {
        Assessment assessment = assessmentRepository.findFirstByIsActiveTrue()
                .orElseThrow(() -> new IllegalStateException("활성화된 검사가 없습니다."));

        AssessmentSummaryResponse response = new AssessmentSummaryResponse(
                assessment.getId(),
                assessment.getName(),
                assessment.getDescription()
        );

        return ApiResponse.success(response);
    }

    @GetMapping("/current/items")
    public ApiResponse<AssessmentItemsResponse> getCurrentAssessmentItems() {
        Assessment assessment = assessmentRepository.findFirstByIsActiveTrue()
                .orElseThrow(() -> new IllegalStateException("활성화된 검사가 없습니다."));

        List<Item> items = itemRepository.findByAssessmentIdOrderBySortOrderAsc(assessment.getId());

        List<AssessmentItemsResponse.ItemDto> itemDtos = items.stream()
                .map(item -> {
                    List<Choice> choices = choiceRepository.findByItemIdOrderBySortOrderAsc(item.getId());
                    List<AssessmentItemsResponse.ChoiceDto> choiceDtos = choices.stream()
                            .map(c -> new AssessmentItemsResponse.ChoiceDto(c.getValue(), c.getLabel()))
                            .toList();
                    return new AssessmentItemsResponse.ItemDto(
                            item.getId(),
                            item.getItemNumber(),
                            item.getText(),
                            item.getScale().getCode(),
                            choiceDtos
                    );
                })
                .toList();

        AssessmentItemsResponse response = new AssessmentItemsResponse(assessment.getId(), itemDtos);

        return ApiResponse.success(response);
    }
}

