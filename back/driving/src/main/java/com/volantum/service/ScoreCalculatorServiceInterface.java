package com.volantum.service;

import java.util.List;

import com.volantum.domain.DrivingSession;
import com.volantum.domain.Event;

public interface ScoreCalculatorServiceInterface {
    float calculateSessionScore(List<Event> events);
    float calculateUserScore(List<DrivingSession> sessions);
}
