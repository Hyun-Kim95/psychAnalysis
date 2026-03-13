package com.tst.psychAnalysis.response;

import com.tst.psychAnalysis.assessment.Item;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class ItemResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ResponseSession responseSession;

    @ManyToOne
    private Item item;

    private int rawValue;

    private Double scoredValue;

    private Double weightedScore;

    public Long getId() {
        return id;
    }

    public ResponseSession getResponseSession() {
        return responseSession;
    }

    public void setResponseSession(ResponseSession responseSession) {
        this.responseSession = responseSession;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getRawValue() {
        return rawValue;
    }

    public void setRawValue(int rawValue) {
        this.rawValue = rawValue;
    }

    public Double getScoredValue() {
        return scoredValue;
    }

    public void setScoredValue(Double scoredValue) {
        this.scoredValue = scoredValue;
    }

    public Double getWeightedScore() {
        return weightedScore;
    }

    public void setWeightedScore(Double weightedScore) {
        this.weightedScore = weightedScore;
    }
}

