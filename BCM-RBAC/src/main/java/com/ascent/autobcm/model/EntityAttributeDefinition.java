package com.ascent.autobcm.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "entity_attribute_definition")
public class EntityAttributeDefinition {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@OneToOne
	private EntitiesType entityType;

	@OneToOne
	private EntitiesAttribute entityAttribute;

	@Column
	private String active;
	
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

	public EntitiesType getEntityType() {
		return entityType;
	}

	public void setEntityType(EntitiesType entityType) {
		this.entityType = entityType;
	}

	public EntitiesAttribute getEntityAttributeValues() {
		return entityAttribute;
	}

	public void setEntityAttributeValues(EntitiesAttribute entityAttributeValues) {
		this.entityAttribute = entityAttributeValues;
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
