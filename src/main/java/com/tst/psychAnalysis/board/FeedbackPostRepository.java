package com.tst.psychAnalysis.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FeedbackPostRepository extends JpaRepository<FeedbackPost, UUID> {

    Page<FeedbackPost> findByHiddenFalseOrderByCreatedAtDesc(Pageable pageable);

    Page<FeedbackPost> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
