package com.volantum.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.volantum.domain.DrivingSession;
import com.volantum.domain.Event;

@Service
public class ScoreCalculatorService implements ScoreCalculatorServiceInterface {
    @Override
    public float calculateSessionScore(List<Event> events) {
        if (events.isEmpty()) return 5.0f;
        
        float totalPenalty = 0;
        for (Event event : events) {
            totalPenalty += switch (event.getType().getSeverity()) {
                case LOW -> 0.2;
                case MEDIUM -> 0.6;
                case HIGH -> 1.2;
                default -> 0.0;
            };
        }

        float score = 5.0f - totalPenalty;
        return Math.max(1.0f, Math.min(5.0f, score));
    }

    @Override
    public float calculateUserScore(List<DrivingSession> sessions) {
        if (sessions.isEmpty()) return 5.0f;
        float total = 0;
        for (DrivingSession session : sessions) {
            total += session.getScore();
        }
        return total / sessions.size();
    }
}
