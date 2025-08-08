package com.moodyclues.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moodyclues.dto.LoginRequestDto;
import com.moodyclues.dto.LoginResponseDto;
import com.moodyclues.model.CounsellorUser;
import com.moodyclues.model.LinkRequest;
import com.moodyclues.service.CounsellorService;
import com.moodyclues.service.JournalUserService;
import com.moodyclues.service.LinkRequestService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/counsellor")
public class CounsellorController {

	@Autowired
	JournalUserService juserService;
	
	@Autowired
	CounsellorService cService;
	
	@Autowired
	LinkRequestService linkService;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDto request, HttpSession session) {
		
		if (cService.loginAttempt(request)) {
			String userEmail = request.getEmail();
			CounsellorUser user = cService.findCounsellorByEmail(userEmail);
			
			LoginResponseDto response = new LoginResponseDto();
			response.setUserId(user.getId());
			
			session.setAttribute("id", user.getId());
			return new ResponseEntity<LoginResponseDto>(response, HttpStatus.OK);

		}
		
		return new ResponseEntity<>("Invalid email or password.", HttpStatus.UNAUTHORIZED);
	}
	
	
	@GetMapping("/logout")
	public ResponseEntity<?> logout(HttpSession session) {

		// Deletes all information in current session, and locks users out of
		// application till next login
		session.invalidate();

		return new ResponseEntity<>("You have logged out successfully", HttpStatus.OK);
	}
	
	@PostMapping("/link-request")
	public ResponseEntity<?> linkRequest(@RequestBody String email, String senderId) {

		cService.linkRequest(email, senderId);
		
	    return new ResponseEntity<>("Request sent successfully.", HttpStatus.OK);
	}
	
	@GetMapping("/all-link-requests")
	public ResponseEntity<?> allLinkRequests(HttpSession session) {
		
		String id = (String) session.getAttribute("id");
		
		List<LinkRequest> linkRequests = linkService.getAllLinkRequestsByCounsellorId(id);
		
		return new ResponseEntity<>(linkRequests, HttpStatus.OK);
		
	}
	
}
