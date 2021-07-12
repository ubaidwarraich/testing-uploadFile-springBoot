package com.javainuse.db;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javainuse.model.Event;

public interface eventRepository extends JpaRepository<Event, Long> {
	Optional<Event> findTopByName(String name);
}
