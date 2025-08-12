package com.moodyclues.controller;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moodyclues.service.CounsellorDashboardService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/counsellor/dashboard")
public class CounsellorDashboardController {

	@Autowired
	CounsellorDashboardService cDashService;

	@GetMapping("/window")
	public ResponseEntity<?> getDashboardWindow(
			HttpSession session,
			@RequestParam(required = false) String counsellorId,
			@RequestParam(required = false) Integer days,
			@RequestParam(required = false)
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam(required = false)
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
			) {
		
		
		String effectiveCounsellorId = (String) session.getAttribute("id");
		if (effectiveCounsellorId == null) effectiveCounsellorId = counsellorId;
		if (effectiveCounsellorId == null || effectiveCounsellorId.isBlank()) {
			return new ResponseEntity<>("No counsellor Id provided.", HttpStatus.UNAUTHORIZED);
		}

		Map<String, Object> payload = cDashService.getCounsellorDashboardPayload(effectiveCounsellorId, days, from, to);

		return new ResponseEntity<>(payload, HttpStatus.OK);

	}
}
