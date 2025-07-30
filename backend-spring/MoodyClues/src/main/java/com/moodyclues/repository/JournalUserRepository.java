package com.moodyclues.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moodyclues.model.JournalUser;
import com.moodyclues.model.User;


public interface JournalUserRepository extends JpaRepository<JournalUser, String> {

	@Query("SELECT u FROM JournalUser u WHERE u.email = :email")
	public Optional<JournalUser> findJournalUserByEmail(@Param("email") String email);
	
	@Query("SELECT u FROM JournalUser u WHERE u.id = :id"	)
	public Optional<JournalUser> findJournalUserById(@Param("id") String id);
	
}
