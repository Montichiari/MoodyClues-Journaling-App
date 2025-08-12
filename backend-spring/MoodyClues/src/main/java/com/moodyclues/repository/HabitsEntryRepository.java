package com.moodyclues.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moodyclues.model.HabitsEntry;
import com.moodyclues.projection.HabitsDayFlat;

public interface HabitsEntryRepository extends JpaRepository<HabitsEntry, String> {

	@Query("SELECT h FROM HabitsEntry h JOIN h.user u WHERE u.id = :userId AND h.archived = false")
	public List<HabitsEntry> getAllHabitsEntriesByUserId(@Param("userId") String userId);

	@Query("SELECT h FROM HabitsEntry h WHERE h.id = :entryId AND h.archived = false")
	public HabitsEntry getHabitsEntryById(@Param("entryId") String entryId);

	@Query("""
			    SELECT h
			    FROM HabitsEntry h
			    WHERE h.user.id = :jid AND h.archived = false
			    ORDER BY h.createdAt DESC
			""")
	public List<HabitsEntry> findVisibleByUserId(@Param("jid") String journalUserId);

	@Query("""
			    SELECT h
			    FROM HabitsEntry h
			    WHERE h.id = :eid AND h.user.id = :jid
			""")
	public Optional<HabitsEntry> findByIdAndUserId(@Param("eid") String entryId, @Param("jid") String journalUserId);

	// FOR DASHBOARD

	@Query(
			value =
			"SELECT DATE(h.created_at) AS day, " +
					"       h.sleep            AS sleep, " +        // hours
					"       h.water            AS water, " +        // litres
					"       h.work_hours       AS workHours " +     // alias to match getWorkHours()
					"FROM habits_entries h " +
					"WHERE h.user_id = :userId " +
					"  AND h.created_at BETWEEN :start AND :end " +
					"ORDER BY day",
					nativeQuery = true
			)
	public List<HabitsDayFlat> findDaysBetween(
			@org.springframework.data.repository.query.Param("userId") String userId,
			@org.springframework.data.repository.query.Param("start")  java.time.LocalDateTime start,
			@org.springframework.data.repository.query.Param("end")    java.time.LocalDateTime end
			);

}
