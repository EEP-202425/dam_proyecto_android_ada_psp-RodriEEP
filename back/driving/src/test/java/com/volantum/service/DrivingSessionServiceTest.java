package com.volantum.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.volantum.domain.Car;
import com.volantum.domain.DrivingSession;
import com.volantum.domain.Event;
import com.volantum.domain.EventType;
import com.volantum.domain.User;
import com.volantum.driving.VolantumApplication;
import com.volantum.enums.EventSeverity;
import com.volantum.repository.EventTypeRepository;

@ActiveProfiles("test")
@SpringBootTest(classes = VolantumApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class DrivingSessionServiceTest {
    private User userTest;
    private Car carTest;
    private EventType hardBreakType;
    private EventType mediumBreakType;

    @Autowired
    private DrivingSessionService drivingSessionService;

    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    @Autowired
    private EventTypeRepository eventTypeRepository;

    @BeforeEach
    void setUp() {
        userTest = userService.register(
            new User("Laura", "Perez", "laura@volantum.com", "abc123")
        );
        carTest = new Car("ABC123");
        userTest.addCar(carTest);
        carTest = carService.save(carTest);

        hardBreakType = new EventType("Hard Brake", EventSeverity.MEDIUM);
		eventTypeRepository.save(hardBreakType);

        mediumBreakType = new EventType("Medium Brake", EventSeverity.MEDIUM);
		eventTypeRepository.save(mediumBreakType);
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

    @Test
    void shouldSetEventsWhenFinishingSession() {
        // 1. Create a session (when starting the drive)
        DrivingSession initialSession = saveSession(carTest, 0f);
        assertThat(initialSession.getEvents()).isEmpty();
        
        // 2. Prepare the session update with final data (simulating ending the drive)
        DrivingSession sessionUpdate = new DrivingSession();
        sessionUpdate.setDistance(14.3f);
        
        // 3. Create events that happened during the drive
        Event hardBreak1 = new Event(hardBreakType, initialSession, 
            LocalDateTime.now().minusMinutes(5), 20.5f, 10.0f);
        Event hardBreak2 = new Event(hardBreakType, initialSession, 
            LocalDateTime.now().minusMinutes(2), 21.0f, 11.0f);
        
        // 4. Add events to the session update
        sessionUpdate.addEvent(hardBreak1);
        sessionUpdate.addEvent(hardBreak2);
        
        // 5. End session
        DrivingSession finishedSession = drivingSessionService.update(initialSession.getId(), sessionUpdate);
        
        assertThat(finishedSession.getEvents())
            .hasSize(2)
            .allMatch(event -> event.getType().equals(hardBreakType));
        assertThat(finishedSession.getDistance()).isEqualTo(14.3f);
        assertThat(finishedSession.getEndTime()).isNotNull();
    }

    @Test
    void shoudSetDrivingSessionScoreWhenIsFinished() {
        // 1. Create a session (when starting the drive)
        DrivingSession initialSession = saveSession(carTest, 0f);
        assertThat(initialSession.getEvents()).isEmpty();

        // 2. Create a session with a hard break and a medium break
        DrivingSession sessionUpdate = new DrivingSession();
        sessionUpdate.setDistance(14.3f);
        
        Event hardBreak = new Event(hardBreakType, sessionUpdate, 
            LocalDateTime.now().minusMinutes(5), 20.5f, 10.0f);
        Event mediumBreak = new Event(mediumBreakType, sessionUpdate, 
            LocalDateTime.now().minusMinutes(2), 21.0f, 11.0f);
        sessionUpdate.addEvent(hardBreak);
        sessionUpdate.addEvent(mediumBreak);

        // 3. End session
        DrivingSession finishedSession = drivingSessionService.update(initialSession.getId(), sessionUpdate);
        
        // 4. Assert that the session score has been updated
        assertThat(finishedSession.getScore()).isEqualTo(3.8f);
    }

    @Test
    void shouldUpdateUserScoreWhenSessionIsFinished() {
        // 1. Create a session (when starting the drive)
        DrivingSession initialSession = saveSession(carTest, 0f);
        assertThat(initialSession.getEvents()).isEmpty();

        // 2. Create a session with a hard break
        DrivingSession sessionUpdate = new DrivingSession();
        sessionUpdate.setDistance(14.3f);
        
        Event hardBreak = new Event(hardBreakType, sessionUpdate, 
            LocalDateTime.now().minusMinutes(5), 20.5f, 10.0f);
        
        sessionUpdate.addEvent(hardBreak);

        // 3. End session
        drivingSessionService.update(initialSession.getId(), sessionUpdate);

        // 4. Assert that the user score has been updated
        assertThat(userTest.getScore()).isEqualTo(4.4f);
    }

}
