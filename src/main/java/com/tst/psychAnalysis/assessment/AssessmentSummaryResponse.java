package com.tst.psychAnalysis.assessment;

public class AssessmentSummaryResponse {

    private final Long id;
    private final String name;
    private final String description;
    private final Long itemCount;

    public AssessmentSummaryResponse(Long id, String name, String description, Long itemCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.itemCount = itemCount != null ? itemCount : 0L;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Long getItemCount() {
        return itemCount;
    }
}

