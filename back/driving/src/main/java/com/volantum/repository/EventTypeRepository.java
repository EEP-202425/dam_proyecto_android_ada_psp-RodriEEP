package com.volantum.repository;

import org.springframework.data.repository.CrudRepository;

import com.volantum.domain.EventType;

public interface EventTypeRepository extends CrudRepository<EventType, Integer> {

}
