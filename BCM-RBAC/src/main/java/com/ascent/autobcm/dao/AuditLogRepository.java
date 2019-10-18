package com.ascent.autobcm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ascent.autobcm.model.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

}
