package com.tst.psychAnalysis.response;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ResultRepository extends JpaRepository<Result, UUID> {

    /** 제출 시각 기준 최신순 */
    List<Result> findAllByOrderByResponseSession_SubmittedAtDesc();
}

