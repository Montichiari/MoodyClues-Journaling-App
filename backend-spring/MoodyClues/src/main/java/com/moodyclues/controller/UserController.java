package com.moodyclues.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moodyclues.dto.LoginRequestDto;
import com.moodyclues.model.JournalUser;
import com.moodyclues.service.JournalUserService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	JournalUserService juserService;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDto request, HttpSession session) {
		
		if (juserService.loginAttempt(request)) {
			String userEmail = request.getEmail();
			JournalUser user = juserService.findJournalUserByEmail(userEmail);
			
			session.setAttribute("id", user.getId());
			return new ResponseEntity<JournalUser>(user, HttpStatus.OK);

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

	
}
