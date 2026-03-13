package com.tst.psychAnalysis.assessment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChoiceRepository extends JpaRepository<Choice, Long> {

    List<Choice> findByItemIdOrderBySortOrderAsc(Long itemId);
}

