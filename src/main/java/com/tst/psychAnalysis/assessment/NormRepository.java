package com.tst.psychAnalysis.assessment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NormRepository extends JpaRepository<Norm, Long> {

    List<Norm> findByAssessmentId(Long assessmentId);
}

