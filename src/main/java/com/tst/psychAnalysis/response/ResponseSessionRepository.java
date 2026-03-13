package com.tst.psychAnalysis.response;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ResponseSessionRepository extends JpaRepository<ResponseSession, UUID> {
}

