package com.javainuse.db;

import java.util.List;
import java.util.Optional;

import com.javainuse.model.News;
import org.springframework.data.jpa.repository.JpaRepository;

import com.javainuse.model.Event;
import org.springframework.data.jpa.repository.Query;

public interface eventRepository extends JpaRepository<Event, Long> {
	@Query(value="select *from event order by date desc limit 10",nativeQuery = true)
	List<Event> findRecent();
}
