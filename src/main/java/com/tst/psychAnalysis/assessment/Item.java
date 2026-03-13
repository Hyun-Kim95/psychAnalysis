package com.tst.psychAnalysis.assessment;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Assessment assessment;

    @ManyToOne
    private Scale scale;

    private int itemNumber;

    private String text;

    private boolean isRequired;

    private boolean isReverseScored;

    private double weight;

    private int sortOrder;

    public Long getId() {
        return id;
    }

    public Assessment getAssessment() {
        return assessment;
    }

    public Scale getScale() {
        return scale;
    }

    public int getItemNumber() {
        return itemNumber;
    }

    public String getText() {
        return text;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public boolean isReverseScored() {
        return isReverseScored;
    }

    public double getWeight() {
        return weight;
    }

    public int getSortOrder() {
        return sortOrder;
    }
}

