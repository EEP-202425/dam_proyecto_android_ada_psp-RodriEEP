package com.volantum.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.Date;

import com.volantum.enums.AlertPriority;
import com.volantum.enums.AlertStatus;

import jakarta.persistence.Column;

@Entity
@Table(name = "maintenance_alerts")
public class MaintenanceAlert extends AuditableEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false)
	private String title;

	private String description;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private AlertStatus status;
	
	@Enumerated(EnumType.STRING)
	private AlertPriority priority;

	// RELATIONS

	@ManyToOne
	@JoinColumn(name = "car_id")
	private Car car;
	
	public MaintenanceAlert() {
	}

	public MaintenanceAlert(String title, AlertPriority priority) {
		this.title = title;
		this.priority = priority;
		this.status = AlertStatus.PENDING;
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public AlertStatus getStatus() {
		return status;
	}

	public void setStatus(AlertStatus status) {
		this.status = status;
	}

	public AlertPriority getPriority() {
		return priority;
	}

	public void setPriority(AlertPriority priority) {
		this.priority = priority;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

}
