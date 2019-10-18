package com.ascent.autobcm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AuditLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;

	@Column
	public String userId;

	@Column
	public String applicationName;

	@Column
	public String ComponentName;

	@Column
	public String EntityType;

	@Column
	public String subEntityType;

	@Column
	public String entity;

	@Column
	public String subEntity;

	@Column
	public String operation;

	@Column
	public String detail;

	@Column
	public String source;

	@Column
	public String userAgent;

	@Column
	public String ip;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getComponentName() {
		return ComponentName;
	}

	public void setComponentName(String componentName) {
		ComponentName = componentName;
	}

	public String getEntityType() {
		return EntityType;
	}

	public void setEntityType(String entityType) {
		EntityType = entityType;
	}

	public String getSubEntityType() {
		return subEntityType;
	}

	public void setSubEntityType(String subEntityType) {
		this.subEntityType = subEntityType;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getSubEntity() {
		return subEntity;
	}

	public void setSubEntity(String subEntity) {
		this.subEntity = subEntity;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
