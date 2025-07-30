package com.moodyclues.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moodyclues.model.Emotion;

public interface EmotionRepository extends JpaRepository<Emotion, String> {

	
	@Query("SELECT e FROM Emotion e WHERE e.emotionLabel = :label")
	public Optional<Emotion> findEmotionByLabel(@Param("label")String label);
}
