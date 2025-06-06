package com.volantum.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.FetchType;

@Entity
@Table(name = "driving_sessions")
public class DrivingSession extends AuditableEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String description;

	@Column(name = "start_time", nullable = false)
	private LocalDateTime startTime;

	@Column(name = "end_time")
	private LocalDateTime endTime;

	private String duration;

	private float distance;

	private float averageSpeed;

	private float score;

	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonIgnoreProperties({ "drivingSessions" })
	private User user;

	@ManyToOne
	@JoinColumn(name = "car_id")
	@JsonIgnoreProperties({ "drivingSessions" })
	private Car car;

	@OneToMany(mappedBy = "drivingSession", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Event> events = new ArrayList<>();

	public DrivingSession() {
	}
	
	public DrivingSession(User user, Car car) {
		this.startTime = LocalDateTime.now();
		this.user = user;
		this.car = car;
		this.events = new ArrayList<>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public float getAverageSpeed() {
		return averageSpeed;
	}

	public void setAverageSpeed(float averageSpeed) {
		this.averageSpeed = averageSpeed;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public void addEvent(Event event) {
		events.add(event);
		event.setDrivingSession(this);
	}

	public List<Event> getEvents() {
		return events;
	}

}
