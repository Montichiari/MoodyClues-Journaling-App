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
		    @RequestParam(required = false) String userId,
		    @RequestParam(required = false) Integer days,
		    @RequestParam(required = false)
		    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
		    @RequestParam(required = false)
		    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
		) {
		
		    String effectiveUserId = (String) session.getAttribute("id");
		    if (effectiveUserId == null) effectiveUserId = userId;
		    if (effectiveUserId == null || effectiveUserId.isBlank()) {
		        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No userId provided");
		    }

		    ZoneId zone = ZoneId.of("Asia/Singapore");
		    LocalDate today = (to != null) ? to : LocalDate.now(zone);
		    LocalDate startDate = (from != null && to != null)
		        ? from
		        : today.minusDays((days == null ? 7 : Math.max(1, days)) - 1);

		    LocalDateTime startTs = startDate.atStartOfDay();
		    LocalDateTime endTs   = today.plusDays(1).atStartOfDay().minusNanos(1);

		    var journalDaily = journalEntryRepo.findDailyAggBetween(effectiveUserId, startTs, endTs);
		    var habitsDays   = habitsEntryRepo.findDaysBetween(effectiveUserId, startTs, endTs);
		    var emotionCounts = journalEntryRepo.countEmotionsBetween(effectiveUserId, startTs, endTs);

		    Map<String,Object> payload = new LinkedHashMap<>();
		    payload.put("window", Map.of("from", startDate, "to", today));
		    payload.put("series", Map.of(
		        "journalDaily", journalDaily,
		        "habitsByDay",  habitsDays,
		        "emotionCounts", emotionCounts
		    ));
		    
		    return ResponseEntity.ok(payload);
	}
	
}
