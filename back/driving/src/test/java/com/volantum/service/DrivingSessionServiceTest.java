package com.volantum.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import com.volantum.domain.Car;
import com.volantum.domain.DrivingSession;
import com.volantum.domain.User;
import com.volantum.driving.VolantumApplication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = VolantumApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class DrivingSessionServiceTest {
    private User userTest;
    private Car carTest;

    @Autowired
    private DrivingSessionService drivingSessionService;

    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    @BeforeEach
    void setUp() {
        userTest = userService.register(
            new User("Laura", "Perez", "laura@volantum.com", "abc123")
        );
        carTest = new Car("ABC123");
        userTest.addCar(carTest);
        carTest = carService.save(carTest);
    }

    private DrivingSession saveSession(Car car, float distance) {
        DrivingSession session = new DrivingSession(
           userTest, car
        );
        session.setDistance(distance);
        return drivingSessionService.save(session);
    }

    private Car saveNewCar(String plate) {
        Car newCar = new Car(plate);
        userTest.addCar(newCar);
        return carService.save(newCar);
    }

    @Test
    void shouldSaveAndRetrieveDrivingSession() {
        DrivingSession saved = saveSession(carTest, 14.3f);

        assertThat(saved).isNotNull();
        Optional<DrivingSession> session = drivingSessionService.findById(saved.getId());
        assertThat(session)
            .isPresent()
            .hasValueSatisfying(s -> assertThat(s.getDistance()).isEqualTo(14.3f));
    }

    @Test
    void shouldFindAllSessionsByUserId() {
        saveSession(carTest, 14.3f);
        saveSession(carTest, 20.5f);

        assertThat(drivingSessionService.findByUserId(userTest.getId()))
            .hasSize(2)
            .extracting(DrivingSession::getDistance)
            .containsExactlyInAnyOrder(14.3f, 20.5f);
    }

    @Test
    void shouldFindAllSessionsByCarId() {
        saveSession(carTest, 14.3f);
        Car otherCar = saveNewCar("XYZ789");
        saveSession(otherCar, 20.5f);

        assertThat(drivingSessionService.findByCarId(otherCar.getId()))
            .hasSize(1)
            .extracting(DrivingSession::getDistance)
            .containsExactly(20.5f);
    }

    @Test
    void shouldFindAllSessionsByUserIdAndCarId() {
        saveSession(carTest, 14.3f);
        Car otherCar = saveNewCar("XYZ789");
        saveSession(otherCar, 20.5f);

        assertThat(drivingSessionService.findByUserIdAndCarId(
            userTest.getId(), otherCar.getId()
        ))
        .hasSize(1)
        .extracting(DrivingSession::getDistance)
        .containsExactly(20.5f);
    }

    @Test
    void shouldUpdateDrivingSession() {
        DrivingSession savedSession = saveSession(carTest, 14.3f);
        savedSession.setDistance(20.5f);
        DrivingSession updatedSession = drivingSessionService.update(savedSession.getId(), savedSession);
        assertThat(updatedSession).isNotNull();
        assertThat(updatedSession.getDistance()).isEqualTo(20.5f);
    }



}
