package com.tst.psychAnalysis.assessment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScaleRepository extends JpaRepository<Scale, Long> {

    List<Scale> findByAssessmentIdOrderByIdAsc(Long assessmentId);
}
