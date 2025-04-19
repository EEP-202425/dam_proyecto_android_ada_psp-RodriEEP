package com.volantum.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "events")
public class Event extends AuditableEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false)
	private LocalDateTime timestamp;

	private float latitude;

	private float longitude;

	@ManyToOne
	@JoinColumn(name = "driving_session_id", nullable = false)
	private DrivingSession drivingSession;

	@ManyToOne
	@JoinColumn(name = "event_type_id", nullable = false)
	private EventType type;

	public Event() {
	}

	public int getId() {
		return id;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public DrivingSession getDrivingSession() {
		return drivingSession;
	}

	public void setDrivingSession(DrivingSession drivingSession) {
		this.drivingSession = drivingSession;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

}
