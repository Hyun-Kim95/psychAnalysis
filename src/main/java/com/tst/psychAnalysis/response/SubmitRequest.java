package com.tst.psychAnalysis.response;

import java.util.List;

public record SubmitRequest(List<Answer> answers) {

    public record Answer(Long itemId, int value) {
    }
}

