package com.moodyclues.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moodyclues.projection.HabitsDayFlat;
import com.moodyclues.projection.JournalDailyAgg;
import com.moodyclues.repository.HabitsEntryRepository;
import com.moodyclues.repository.JournalEntryRepository;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

	@Autowired
	JournalEntryRepository journalEntryRepo;
	
	@Autowired
	HabitsEntryRepository habitsEntryRepo;
	
	@GetMapping("/window")
	public ResponseEntity<?> getDashboardWindow(
	    HttpSession session,
	    @RequestParam(required = false) Integer days,
	    @RequestParam(required = false)
	    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
	    @RequestParam(required = false)
	    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
	) {
	    String userId = (String) session.getAttribute("id");
	    if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");

	    ZoneId zone = ZoneId.of("Asia/Singapore");
	    LocalDate today = (to != null) ? to : LocalDate.now(zone);
	    LocalDate startDate = (from != null && to != null)
	        ? from
	        : today.minusDays((days == null ? 7 : Math.max(1, days)) - 1);

	    LocalDateTime startTs = startDate.atStartOfDay();
	    LocalDateTime endTs   = today.plusDays(1).atStartOfDay().minusNanos(1);

	    var journalDaily = journalEntryRepo.findDailyAggBetween(userId, startTs, endTs);
	    var habitsDays   = habitsEntryRepo.findDaysBetween(userId, startTs, endTs);

	    // Summary:
	    Double avgMoodSelected = journalDaily.stream()
	        .map(JournalDailyAgg::getAvgMood).filter(Objects::nonNull)
	        .mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
	    avgMoodSelected = Double.isNaN(avgMoodSelected) ? null : avgMoodSelected;

	    Double avgSleepHoursSelected = habitsDays.stream()
	        .map(HabitsDayFlat::getSleep).filter(Objects::nonNull)
	        .mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
	    avgSleepHoursSelected = Double.isNaN(avgSleepHoursSelected) ? null : avgSleepHoursSelected;

	    Double avgWaterLitresSelected = habitsDays.stream()
	        .map(HabitsDayFlat::getWater).filter(Objects::nonNull)
	        .mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
	    avgWaterLitresSelected = Double.isNaN(avgWaterLitresSelected) ? null : avgWaterLitresSelected;

	    Double avgWorkHoursSelected = habitsDays.stream()
	        .map(HabitsDayFlat::getWorkHours).filter(Objects::nonNull)
	        .mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
	    avgWorkHoursSelected = Double.isNaN(avgWorkHoursSelected) ? null : avgWorkHoursSelected;

	    Map<String, Object> payload = new LinkedHashMap<>();
	    payload.put("window", Map.of("from", startDate, "to", today));
	    payload.put("series", Map.of(
	        "journalDaily", journalDaily,
	        "habitsByDay",  habitsDays 
	    ));
	    payload.put("summary", Map.of(
	        "avgMoodSelected",       avgMoodSelected,
	        "avgSleepHoursSelected", avgSleepHoursSelected,
	        "avgWaterLitresSelected",avgWaterLitresSelected,
	        "avgWorkHoursSelected",  avgWorkHoursSelected,
	        "daysWithJournal",       journalDaily.size(),
	        "daysWithHabits",        habitsDays.size()
	    ));
	    
	    var emotionCounts = journalEntryRepo.countEmotionsBetween(userId, startTs, endTs);

	    payload.put("series", Map.of(
	        "journalDaily", journalDaily,
	        "habitsByDay",  habitsDays,
	        "emotionCounts", emotionCounts
	    ));

	    return ResponseEntity.ok(payload);
	}
	
}
