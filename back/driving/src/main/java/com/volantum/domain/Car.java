package com.volantum.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

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

@Entity
@Table(name = "cars")
public class Car extends AuditableEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(unique = true, length = 6, nullable = false)
	private String plate;

	private String brand;
	private String model;
	private int yearModel;
	private String image = "";
	private Double mileage = 0.0;

	// RELATIONS

	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonBackReference
	private User user;

	@OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DrivingSession> drivingSessions = new ArrayList<>();

	@OneToMany(mappedBy = "car")
	private List<MaintenanceAlert> maintenanceAlerts = new ArrayList<MaintenanceAlert>();
	
	public Car() {
	}
	
	public Car(String plate) {
		super();
		this.plate = plate;
	}

	public Car(String plate, String brand, String model, int yearModel) {
		super();
		this.plate = plate;
		this.brand = brand;
		this.model = model;
		this.yearModel = yearModel;
	}

	public int getId() {
		return id;
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getYearModel() {
		return yearModel;
	}

	public void setYearModel(int year) {
		this.yearModel = year;
	}

	public Double getMileage() {
		return mileage;
	}

	public void setMileage(Double mileage) {
		this.mileage = mileage;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

	public List<DrivingSession> getDrivingSessions() {
		return drivingSessions;
	}

	public List<MaintenanceAlert> getMaintenanceAlerts() {
		return maintenanceAlerts;
	}
	
	public void addDrivingSession(DrivingSession drivingSession) {
		drivingSession.setCar(this);
	    this.drivingSessions.add(drivingSession);
	}
	
	public void addMaintenanceAlert(MaintenanceAlert maintenanceAlert) {
		maintenanceAlert.setCar(this);
	    this.maintenanceAlerts.add(maintenanceAlert);
	}
	
}
