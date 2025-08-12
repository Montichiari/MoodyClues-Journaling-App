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
import com.moodyclues.service.DashboardService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

	@Autowired
	DashboardService dashService;
	
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
	        return new ResponseEntity<>("No userId provided", HttpStatus.UNAUTHORIZED);
	    }
	    
	    Map<String,Object> payload = dashService.getDashboardPayload(effectiveUserId, days, from, to);

	    return new ResponseEntity<>(payload, HttpStatus.OK);
	}
}
