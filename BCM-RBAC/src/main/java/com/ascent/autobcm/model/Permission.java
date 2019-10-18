package com.ascent.autobcm.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class Permission {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@OneToOne
	private Role role;

	@OneToOne
	private EntitiesType entityType;

	@OneToOne
	private Operations operation;

	@OneToOne
	private Entities entity;

	@Column
	private String createriaBased;

	@Column
	private String entityBased;

	@Column
	private Long userAttributes;

	@Column
	private Long condition;

	@Column
	private Long userValues;

	@Column
	public String active;

	@CreationTimestamp
	private LocalDateTime createDateTime;

	@UpdateTimestamp
	private LocalDateTime updateDateTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public EntitiesType getEntityType() {
		return entityType;
	}

	public void setEntityType(EntitiesType entityType) {
		this.entityType = entityType;
	}

	public Operations getOperation() {
		return operation;
	}

	public void setOperation(Operations operation) {
		this.operation = operation;
	}

	public Entities getEntity() {
		return entity;
	}

	public void setEntity(Entities entity) {
		this.entity = entity;
	}

	public String getCreateriaBased() {
		return createriaBased;
	}

	public void setCreateriaBased(String createriaBased) {
		this.createriaBased = createriaBased;
	}

	public String getEntityBased() {
		return entityBased;
	}

	public void setEntityBased(String entityBased) {
		this.entityBased = entityBased;
	}

	public Long getUserAttributes() {
		return userAttributes;
	}

	public void setUserAttributes(Long userAttributes) {
		this.userAttributes = userAttributes;
	}

	public Long getCondition() {
		return condition;
	}

	public void setCondition(Long condition) {
		this.condition = condition;
	}

	public Long getUserValues() {
		return userValues;
	}

	public void setUserValues(Long userValues) {
		this.userValues = userValues;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public LocalDateTime getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(LocalDateTime createDateTime) {
		this.createDateTime = createDateTime;
	}

	public LocalDateTime getUpdateDateTime() {
		return updateDateTime;
	}

	public void setUpdateDateTime(LocalDateTime updateDateTime) {
		this.updateDateTime = updateDateTime;
	}

}
