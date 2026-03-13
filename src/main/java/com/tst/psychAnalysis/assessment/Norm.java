package com.tst.psychAnalysis.assessment;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Norm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Assessment assessment;

    @ManyToOne
    private Scale scale;

    private String groupCode;

    private double mean;

    private double sd;

    public Long getId() {
        return id;
    }

    public Assessment getAssessment() {
        return assessment;
    }

    public Scale getScale() {
        return scale;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public double getMean() {
        return mean;
    }

    public double getSd() {
        return sd;
    }
}

