package com.javainuse.db;

import java.util.Optional;

import com.javainuse.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import com.javainuse.model.Event;

public interface imageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findTopById(Long id);
}
