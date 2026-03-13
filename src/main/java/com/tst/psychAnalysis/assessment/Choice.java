package com.tst.psychAnalysis.assessment;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Choice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Item item;

    private String label;

    private int value;

    private int sortOrder;

    public Long getId() {
        return id;
    }

    public Item getItem() {
        return item;
    }

    public String getLabel() {
        return label;
    }

    public int getValue() {
        return value;
    }

    public int getSortOrder() {
        return sortOrder;
    }
}

