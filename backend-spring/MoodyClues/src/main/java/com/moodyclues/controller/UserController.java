package com.moodyclues.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moodyclues.dto.LoginRequestDto;
import com.moodyclues.dto.LoginResponseDto;
import com.moodyclues.dto.RegisterRequestDto;
import com.moodyclues.model.JournalUser;
import com.moodyclues.service.JournalUserService;
import com.moodyclues.service.LinkRequestService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	JournalUserService juserService;
	
	@Autowired
	LinkRequestService linkService;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDto request, HttpSession session) {
		
		if (juserService.loginAttempt(request)) {
			String userEmail = request.getEmail();
			JournalUser user = juserService.findJournalUserByEmail(userEmail);
			
		    var authorities = List.of(new SimpleGrantedAuthority("ROLE_JOURNAL"));
		    var auth = new UsernamePasswordAuthenticationToken(user.getId().toString(), null, authorities);
			
		    SecurityContext context = SecurityContextHolder.createEmptyContext();
		    context.setAuthentication(auth);
		    SecurityContextHolder.setContext(context);
		    session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
		    
			LoginResponseDto response = new LoginResponseDto();
			response.setUserId(user.getId());
			response.setShowEmotion(user.isShowEmotion());
			
			
			session.setAttribute("id", user.getId());
			session.setAttribute("showEmotion", user.isShowEmotion());

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
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequestDto request) {
		
		juserService.registerUser(request);
		
		return new ResponseEntity<>("You have registered successfully.", HttpStatus.OK);
		
		
	}
	
	@PutMapping("/toggle-emotion")
	public ResponseEntity<?> toggleEmotion(HttpSession session) {
		String userId = session.getId();
		JournalUser user = juserService.findJournalUserById(userId);
		
		if (user.isShowEmotion()) {
			user.setShowEmotion(false);
			return new ResponseEntity<>("Emotions set to hidden", HttpStatus.OK);

		}
		
		if (!(user.isShowEmotion())) {
			user.setShowEmotion(true);
			return new ResponseEntity<>("Emotions set to shown", HttpStatus.OK);

		}
		
		return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
		
	}
	
	@PutMapping("/toggle-emotion/{userId}")
	public ResponseEntity<?> toggleEmotionAndroid(@PathVariable String userId) {
		
		String outcome = juserService.toggleEmotion(userId);
		
		return new ResponseEntity<>(outcome, HttpStatus.OK);
	}
	
}
