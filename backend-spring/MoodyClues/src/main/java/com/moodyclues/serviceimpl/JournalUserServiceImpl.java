package com.moodyclues.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.moodyclues.dto.LoginRequestDto;
import com.moodyclues.dto.RegisterRequestDto;
import com.moodyclues.model.JournalUser;
import com.moodyclues.model.User;
import com.moodyclues.repository.JournalUserRepository;
import com.moodyclues.service.JournalUserService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class JournalUserServiceImpl implements JournalUserService {

	@Autowired
	private JournalUserRepository userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public JournalUser findJournalUserByEmail(String email) {
		
		JournalUser user = userRepo.findJournalUserByEmail(email).orElseThrow(
				() -> new EntityNotFoundException("Customer with email " + email + " not found"));
		
		
		return user;
	}
	
	@Override
	public JournalUser findJournalUserById(String id) {

		JournalUser user = userRepo.findJournalUserById(id).orElseThrow(
				() -> new EntityNotFoundException("Customer with id " + id + " not found"));
		
		return user;
	}

	@Override
	public boolean loginAttempt(LoginRequestDto request) {
		
		String email = request.getEmail();
		String passwordInput = request.getPassword();
		
		JournalUser user = this.findJournalUserByEmail(email);

		if (!passwordEncoder.matches(passwordInput, user.getPassword())) {
			return false;
		}
		
		
		return true;
	}

	@Override
	public boolean loginAttempt(String email, String passwordInput) {
		
		JournalUser user = this.findJournalUserByEmail(email);
		
		if (!passwordEncoder.matches(passwordInput, user.getPassword())) {
			return false;
		}
		
		return true;
	}

	@Override
	public void deleteUser(String email, String password) {
		
		JournalUser user = this.findJournalUserByEmail(email);
		
		user.setArchived(true);

	}

	
	// For dev testing only
	@Override
	public void deleteUser(String id) {
		
		JournalUser user = this.findJournalUserById(id);
		
		user.setArchived(true);
		
	}

	
	@Override
	public void registerUser(RegisterRequestDto request) {

		String email = request.getEmail();
		if (userRepo.findJournalUserByEmail(email).isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request");
		}
		
		JournalUser newUser = new JournalUser();
		newUser.setEmail(request.getEmail());
		
		// Encrypted password
		newUser.setPassword(passwordEncoder.encode(request.getPassword()));
		newUser.setFirstName(request.getFirstName());
		newUser.setLastName(request.getLastName());
		
		userRepo.save(newUser);
		
	}




	
}
