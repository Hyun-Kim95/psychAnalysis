package com.tst.psychAnalysis.assessment;

import com.tst.psychAnalysis.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping
    public ApiResponse<List<AssessmentSummaryResponse>> listAssessments(
            @RequestHeader(value = "Accept-Language", required = false) String language
    ) {
        boolean english = LocalizationTexts.isEnglish(language);
        List<Assessment> list = assessmentRepository.findByIsActiveTrueOrderByIdAsc();
        List<AssessmentSummaryResponse> response = list.stream()
                .map(a -> new AssessmentSummaryResponse(
                        a.getId(),
                        LocalizationTexts.assessmentName(a.getName(), english),
                        LocalizationTexts.assessmentDescription(a.getName(), a.getDescription(), english),
                        itemRepository.countByAssessmentId(a.getId())
                ))
                .toList();
        return ApiResponse.success(response);
    }

    @GetMapping("/current")
    public ApiResponse<AssessmentSummaryResponse> getCurrentAssessment(
            @RequestHeader(value = "Accept-Language", required = false) String language
    ) {
        boolean english = LocalizationTexts.isEnglish(language);
        Assessment assessment = assessmentRepository.findFirstByIsActiveTrue()
                .orElseThrow(() -> new IllegalStateException("활성화된 검사가 없습니다."));

        AssessmentSummaryResponse response = new AssessmentSummaryResponse(
                assessment.getId(),
                LocalizationTexts.assessmentName(assessment.getName(), english),
                LocalizationTexts.assessmentDescription(assessment.getName(), assessment.getDescription(), english),
                itemRepository.countByAssessmentId(assessment.getId())
        );

        return ApiResponse.success(response);
    }

    @GetMapping("/{id}")
    public ApiResponse<AssessmentSummaryResponse> getAssessment(
            @PathVariable("id") Long id,
            @RequestHeader(value = "Accept-Language", required = false) String language
    ) {
        boolean english = LocalizationTexts.isEnglish(language);
        Assessment assessment = assessmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("검사를 찾을 수 없습니다."));
        return ApiResponse.success(new AssessmentSummaryResponse(
                assessment.getId(),
                LocalizationTexts.assessmentName(assessment.getName(), english),
                LocalizationTexts.assessmentDescription(assessment.getName(), assessment.getDescription(), english),
                itemRepository.countByAssessmentId(assessment.getId())
        ));
    }

    @GetMapping("/current/items")
    public ApiResponse<AssessmentItemsResponse> getCurrentAssessmentItems(
            @RequestHeader(value = "Accept-Language", required = false) String language
    ) {
        boolean english = LocalizationTexts.isEnglish(language);
        Assessment assessment = assessmentRepository.findFirstByIsActiveTrue()
                .orElseThrow(() -> new IllegalStateException("활성화된 검사가 없습니다."));
        String assessmentName = assessment.getName();

        List<Item> items = itemRepository.findByAssessmentIdOrderBySortOrderAsc(assessment.getId());

        List<AssessmentItemsResponse.ItemDto> itemDtos = items.stream()
                .map(item -> {
                    List<Choice> choices = choiceRepository.findByItemIdOrderBySortOrderAsc(item.getId());
                    List<AssessmentItemsResponse.ChoiceDto> choiceDtos = choices.stream()
                            .map(c -> new AssessmentItemsResponse.ChoiceDto(c.getValue(), LocalizationTexts.choiceLabel(c.getLabel(), english)))
                            .toList();
                    return new AssessmentItemsResponse.ItemDto(
                            item.getId(),
                            item.getItemNumber(),
                            LocalizationTexts.itemText(assessmentName, item.getScale().getCode(), item.getItemNumber(), item.getText(), english),
                            item.getScale().getCode(),
                            choiceDtos
                    );
                })
                .toList();

        AssessmentItemsResponse response = new AssessmentItemsResponse(assessment.getId(), itemDtos);

        return ApiResponse.success(response);
    }

    @GetMapping("/{id}/items")
    public ApiResponse<AssessmentItemsResponse> getAssessmentItems(
            @PathVariable("id") Long id,
            @RequestHeader(value = "Accept-Language", required = false) String language
    ) {
        boolean english = LocalizationTexts.isEnglish(language);
        Assessment assessment = assessmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("검사를 찾을 수 없습니다."));
        String assessmentName = assessment.getName();

        List<Item> items = itemRepository.findByAssessmentIdOrderBySortOrderAsc(assessment.getId());
        List<AssessmentItemsResponse.ItemDto> itemDtos = items.stream()
                .map(item -> {
                    List<Choice> choices = choiceRepository.findByItemIdOrderBySortOrderAsc(item.getId());
                    List<AssessmentItemsResponse.ChoiceDto> choiceDtos = choices.stream()
                            .map(c -> new AssessmentItemsResponse.ChoiceDto(c.getValue(), LocalizationTexts.choiceLabel(c.getLabel(), english)))
                            .toList();
                    return new AssessmentItemsResponse.ItemDto(
                            item.getId(),
                            item.getItemNumber(),
                            LocalizationTexts.itemText(assessmentName, item.getScale().getCode(), item.getItemNumber(), item.getText(), english),
                            item.getScale().getCode(),
                            choiceDtos
                    );
                })
                .toList();

        return ApiResponse.success(new AssessmentItemsResponse(assessment.getId(), itemDtos));
    }
}

