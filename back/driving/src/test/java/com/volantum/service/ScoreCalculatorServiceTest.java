package com.volantum.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import com.volantum.domain.*;
import com.volantum.enums.EventSeverity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ScoreCalculatorServiceTest {

    private ScoreCalculatorService scoreCalculator;

    private EventType lowSeverityType;
    private EventType mediumSeverityType;
    private EventType highSeverityType;

    @BeforeEach
    void setUp() {
        scoreCalculator = new ScoreCalculatorService();

        lowSeverityType = new EventType("Low Event", EventSeverity.LOW);
        mediumSeverityType = new EventType("Medium Event", EventSeverity.MEDIUM);
        highSeverityType = new EventType("High Event", EventSeverity.HIGH);
    }

    private Event buildEvent(EventType type) {
        Event event = new Event();
        event.setType(type);
        event.setTimestamp(LocalDateTime.now());
        return event;
    }

    @Test
    void shouldCalculateSessionScoreMaximumWithNoEvents_ReturnsMaxScore() {
        float score = scoreCalculator.calculateSessionScore(List.of());
        assertEquals(5.0f, score);
    }

    @Test
    void shouldCalculateSessionScoreWithLowSeverityEvents() {
        List<Event> events = List.of(buildEvent(lowSeverityType), buildEvent(lowSeverityType));
        float score = scoreCalculator.calculateSessionScore(events);
        assertEquals(4.6f, score);
    }

    @Test
    void shouldCalculateSessionScoreWithMixedSeverityEvents() {
        List<Event> events = List.of(
                buildEvent(lowSeverityType),
                buildEvent(mediumSeverityType),
                buildEvent(highSeverityType)
        );
        float score = scoreCalculator.calculateSessionScore(events);
        assertEquals(3.0f, score);
    }

    @Test
    void shouldCalculateSessionScoreWithTooManySevereEventsToMinimum() {
        List<Event> events = List.of(
                buildEvent(highSeverityType),
                buildEvent(highSeverityType),
                buildEvent(highSeverityType),
                buildEvent(highSeverityType),
                buildEvent(highSeverityType)
        );
        float score = scoreCalculator.calculateSessionScore(events);
        assertEquals(1.0f, score);
    }

    @Test
    void shouldCalculateUserScoreWithMultipleSessions() {
        DrivingSession session1 = new DrivingSession();
        session1.setScore(4.5f);

        DrivingSession session2 = new DrivingSession();
        session2.setScore(3.0f);

        DrivingSession session3 = new DrivingSession();
        session3.setScore(5.0f);

        float score = scoreCalculator.calculateUserScore(List.of(session1, session2, session3));
        assertEquals(4.17f, score, 0.01f);
    }

    @Test
    void shouldCalculateUserScoreWithNoSessions_ReturnsMax() {
        float score = scoreCalculator.calculateUserScore(List.of());
        assertEquals(5.0f, score);
    }
}
