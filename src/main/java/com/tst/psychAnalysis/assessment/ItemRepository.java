package com.tst.psychAnalysis.assessment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByAssessmentIdOrderBySortOrderAsc(Long assessmentId);
}

