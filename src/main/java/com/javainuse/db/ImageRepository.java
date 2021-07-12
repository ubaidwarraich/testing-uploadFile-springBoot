package com.javainuse.db;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javainuse.model.ImageModel;

public interface ImageRepository extends JpaRepository<ImageModel, Long> {
	Optional<ImageModel> findTopByName(String name);
}
