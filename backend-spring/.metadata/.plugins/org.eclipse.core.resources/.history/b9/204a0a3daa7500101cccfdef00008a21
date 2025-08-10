package com.moodyclues.service;

import com.moodyclues.dto.LoginRequestDto;
import com.moodyclues.dto.RegisterRequestDto;
import com.moodyclues.model.CounsellorUser;

public interface CounsellorService {

	public CounsellorUser findCounsellorById(String id);
	
	public CounsellorUser findCounsellorByEmail(String email);
	
	public boolean loginAttempt(LoginRequestDto request);
	
	public boolean loginAttempt(String email, String passwordInput);
	
	public void linkRequest(String email, String senderId);
	
	public void registerCounsellor(RegisterRequestDto request);
}
