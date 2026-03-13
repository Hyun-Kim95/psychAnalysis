package com.tst.psychAnalysis.assessment;

import java.util.List;

public class AssessmentItemsResponse {

    private final Long assessmentId;
    private final List<ItemDto> items;

    public AssessmentItemsResponse(Long assessmentId, List<ItemDto> items) {
        this.assessmentId = assessmentId;
        this.items = items;
    }

    public Long getAssessmentId() {
        return assessmentId;
    }

    public List<ItemDto> getItems() {
        return items;
    }

    public static class ItemDto {
        private final Long id;
        private final int itemNumber;
        private final String text;
        private final String scaleCode;
        private final List<ChoiceDto> choices;

        public ItemDto(Long id, int itemNumber, String text, String scaleCode, List<ChoiceDto> choices) {
            this.id = id;
            this.itemNumber = itemNumber;
            this.text = text;
            this.scaleCode = scaleCode;
            this.choices = choices;
        }

        public Long getId() {
            return id;
        }

        public int getItemNumber() {
            return itemNumber;
        }

        public String getText() {
            return text;
        }

        public String getScaleCode() {
            return scaleCode;
        }

        public List<ChoiceDto> getChoices() {
            return choices;
        }
    }

    public static class ChoiceDto {
        private final int value;
        private final String label;

        public ChoiceDto(int value, String label) {
            this.value = value;
            this.label = label;
        }

        public int getValue() {
            return value;
        }

        public String getLabel() {
            return label;
        }
    }
}

