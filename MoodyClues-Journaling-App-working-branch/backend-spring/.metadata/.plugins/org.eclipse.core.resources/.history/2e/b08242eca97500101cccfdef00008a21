package com.moodyclues.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moodyclues.model.CounsellorUser;
import com.moodyclues.model.JournalUser;

public interface CounsellorRepository extends JpaRepository<CounsellorUser, String> {

	@Query("SELECT c FROM CounsellorUser c WHERE c.email = :email")
	public Optional<CounsellorUser> findCounsellorByEmail(@Param("email") String email);
	
	@Query("SELECT c FROM CounsellorUser c WHERE c.id = :id")
	public Optional<CounsellorUser> findCounsellorById(@Param("id") String id);
}
