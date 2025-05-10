package com.volantum.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
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
import com.volantum.dto.DrivingSessionRequestDTO;
import com.volantum.dto.EventRequestDTO;
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
    private DrivingSessionRequestDTO testSessionDTO;

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

        testSessionDTO = new DrivingSessionRequestDTO(
            0f,
            null,
            userTest.getId(),
            carTest.getId()
        );
    }

    private DrivingSession saveSession(Car car) {
        testSessionDTO.setCarId(car.getId());
        return drivingSessionService.save(testSessionDTO);
    }

    private Car saveNewCar(String plate) {
        Car newCar = new Car(plate);
        userTest.addCar(newCar);
        return carService.save(newCar);
    }

    @Test
    void shouldSaveAndRetrieveDrivingSession() {
        DrivingSession saved = saveSession(carTest);

        assertThat(saved).isNotNull();
        Optional<DrivingSession> session = drivingSessionService.findById(saved.getId());
        assertThat(session)
            .isPresent()
            .hasValueSatisfying(s -> assertThat(s.getDistance()).isEqualTo(0f));
    }

    @Test
    void shouldFindAllSessionsByUserId() {
        saveSession(carTest);
        saveSession(carTest);

        assertThat(drivingSessionService.findByUserId(userTest.getId()))
            .hasSize(2)
            .extracting(DrivingSession::getDistance)
            .containsExactlyInAnyOrder(0f, 0f);
    }

    @Test
    void shouldFindAllSessionsByCarId() {
        saveSession(carTest);
        Car otherCar = saveNewCar("XYZ789");
        saveSession(otherCar);

        assertThat(drivingSessionService.findByCarId(otherCar.getId()))
            .hasSize(1)
            .extracting(DrivingSession::getCar)
            .containsExactly(otherCar);
    }

    @Test
    void shouldFindAllSessionsByUserIdAndCarId() {
        saveSession(carTest);
        Car otherCar = saveNewCar("XYZ789");
        saveSession(otherCar);

        assertThat(drivingSessionService.findByUserIdAndCarId(
            userTest.getId(), otherCar.getId()
        ))
        .hasSize(1)
        .extracting(DrivingSession::getCar)
        .containsExactly(otherCar);
    }

    @Test
    void shouldUpdateDrivingSession() {
        DrivingSession savedSession = saveSession(carTest);
        
        testSessionDTO.setDistance(20.5f);
        
        DrivingSession updatedSession = drivingSessionService.update(savedSession.getId(), testSessionDTO);
        assertThat(updatedSession).isNotNull();
        assertThat(updatedSession.getDistance()).isEqualTo(20.5f);
    }

    @Test
    void shouldSetEventsWhenFinishingSession() {
        // 1. Create a session (when starting the drive)
        DrivingSession initialSession = saveSession(carTest);
        assertThat(initialSession.getEvents()).isEmpty();
        
        // 2. Prepare the session update with final data (simulating ending the drive)
        Event hardBreak1 = new Event(hardBreakType, initialSession, 
            LocalDateTime.now().minusMinutes(5), 20.5f, 10.0f);
            
        testSessionDTO.setEvents(List.of(new EventRequestDTO(
            hardBreak1.getTimestamp(),
            hardBreak1.getLatitude(),
            hardBreak1.getLongitude(),
            hardBreak1.getType().getId()
        )));
        testSessionDTO.setDistance(14.3f);
        
        // 3. End session
        DrivingSession finishedSession = drivingSessionService.update(initialSession.getId(), testSessionDTO);
        
        assertThat(finishedSession.getEvents())
            .hasSize(1)
            .allMatch(event -> event.getType().equals(hardBreakType));
        assertThat(finishedSession.getDistance()).isEqualTo(14.3f);
        assertThat(finishedSession.getEndTime()).isNotNull();
    }

    @Test
    void shoudSetDrivingSessionScoreWhenIsFinished() {
        // 1. Create a session (when starting the drive)
        DrivingSession initialSession = saveSession(carTest);
        assertThat(initialSession.getEvents()).isEmpty();

        // 2. Prepare the session update with final data (simulating ending the drive)
        testSessionDTO.setEvents(List.of(
            new EventRequestDTO(
                LocalDateTime.now().minusMinutes(5),
                20.5f,
                10.0f,
                mediumBreakType.getId()
            ),
            new EventRequestDTO(
                LocalDateTime.now().minusMinutes(2),
                21.0f,
                11.0f,
                hardBreakType.getId()
            )
        ));

        // 3. End session
        DrivingSession finishedSession = drivingSessionService.update(initialSession.getId(), testSessionDTO);
        
        // 4. Assert that the session score has been updated
        assertThat(finishedSession.getScore()).isEqualTo(3.8f);
    }

    @Test
    void shouldUpdateUserScoreWhenSessionIsFinished() {
        // 1. Create a session (when starting the drive)
        DrivingSession initialSession = saveSession(carTest);
        assertThat(initialSession.getEvents()).isEmpty();

        // 2. Create a session with a hard break
        Event hardBreak = new Event(hardBreakType, initialSession, 
            LocalDateTime.now().minusMinutes(5), 20.5f, 10.0f);
            
        testSessionDTO.setEvents(List.of(new EventRequestDTO(
            hardBreak.getTimestamp(),
            hardBreak.getLatitude(),
            hardBreak.getLongitude(),
            hardBreak.getType().getId()
        )));

        // 3. End session
        drivingSessionService.update(initialSession.getId(), testSessionDTO);

        // 4. Assert that the user score has been updated
        assertThat(userTest.getScore()).isEqualTo(4.4f);
    }

}
