package com.moodyclues.service;

import java.util.List;

import com.moodyclues.dto.LoginRequestDto;
import com.moodyclues.dto.RegisterRequestDto;
import com.moodyclues.model.CounsellorUser;
import com.moodyclues.model.HabitsEntry;
import com.moodyclues.model.JournalEntry;
import com.moodyclues.model.JournalUser;

public interface CounsellorService {

	public CounsellorUser findCounsellorById(String id);

	public CounsellorUser findCounsellorByEmail(String email);

	public boolean loginAttempt(LoginRequestDto request);

	public boolean loginAttempt(String email, String passwordInput);

	public void linkRequest(String email, String senderId);

	public void registerCounsellor(RegisterRequestDto request);

	public List<JournalUser> listClients(String counsellorId);
	
	public List<JournalEntry> listClientJournalEntries(String counsellorId, String journalUserId);
	
	public List<HabitsEntry> listClientHabitsEntries(String counsellorId, String journalUserId);
	
	public JournalEntry getJournalEntry(String counsellorId, String journalUserId, String entryId);
	
	public HabitsEntry getHabitsEntry(String counsellorId, String journalUserId, String entryId);
}
