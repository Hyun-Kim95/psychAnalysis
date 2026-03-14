package com.tst.psychAnalysis.assessment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssessmentRepository extends JpaRepository<Assessment, Long> {

    Optional<Assessment> findFirstByIsActiveTrue();

    List<Assessment> findByIsActiveTrueOrderByIdAsc();
}

