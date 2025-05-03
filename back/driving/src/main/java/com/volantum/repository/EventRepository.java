package com.volantum.repository;

import org.springframework.data.repository.CrudRepository;
import java.util.List;
import com.volantum.domain.Event;

public interface EventRepository extends CrudRepository<Event, Integer> {
    List<Event> findByDrivingSessionId(int drivingSessionId);
}
