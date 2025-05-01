package com.volantum.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User extends AuditableEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "last_name", nullable = false)
	private String lastName;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;
	
	private float score;

	// RELATIONS

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<Car> cars;

	@OneToMany(mappedBy = "user")
	private List<DrivingSession> drivingSessions;
	
	public User() {
	}

	public User(String firstName, String lastName, String email, String password) {
		super();	
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.cars = new ArrayList<Car>();
		this.drivingSessions = new ArrayList<DrivingSession>();
	}

	public int getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}
	
	public void setCars(List<Car> cars) {
		this.cars = cars;
	}
	
	public List<Car> getCars() {
		return cars;
	}
	
	public List<DrivingSession> getDrivingSessions() {
		return drivingSessions;
	}

	public void setDrivingSessions(List<DrivingSession> drivingSessions) {
		this.drivingSessions = drivingSessions;
	}

	public void addCar(Car car) {
	    car.setUser(this);
	    this.cars.add(car);
	}
	
	public void addDrivingSession(DrivingSession drivingSession) {
		drivingSession.setUser(this);
	    this.drivingSessions.add(drivingSession);
	}
}
