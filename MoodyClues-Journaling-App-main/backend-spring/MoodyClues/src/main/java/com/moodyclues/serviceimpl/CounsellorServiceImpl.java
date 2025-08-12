package com.moodyclues.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.moodyclues.dto.LoginRequestDto;
import com.moodyclues.dto.RegisterRequestDto;
import com.moodyclues.model.CounsellorUser;
import com.moodyclues.model.HabitsEntry;
import com.moodyclues.model.JournalEntry;
import com.moodyclues.model.JournalUser;
import com.moodyclues.model.LinkRequest;
import com.moodyclues.model.LinkRequest.Status;
import com.moodyclues.repository.CounsellorRepository;
import com.moodyclues.repository.HabitsEntryRepository;
import com.moodyclues.repository.JournalEntryRepository;
import com.moodyclues.repository.LinkRequestRepository;
import com.moodyclues.service.CounsellorService;
import com.moodyclues.service.JournalUserService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class CounsellorServiceImpl implements CounsellorService {

	@Autowired
	CounsellorRepository cRepo;
	
	@Autowired
	JournalUserService juserService;
	
	@Autowired
	LinkRequestRepository linkRepo;
	
	@Autowired
	JournalEntryRepository jRepo;
	
	@Autowired
	HabitsEntryRepository hRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public CounsellorUser findCounsellorById(String id) {
		CounsellorUser user = cRepo.findCounsellorById(id).orElseThrow(
				() -> new EntityNotFoundException("Customer with id " + id + " not found"));
		
		
		return user;
	}

	@Override
	public CounsellorUser findCounsellorByEmail(String email) {
		CounsellorUser user = cRepo.findCounsellorByEmail(email).orElseThrow(
				() -> new EntityNotFoundException("Customer with email " + email + " not found"));
		
		return user;
	}

	@Override
	public boolean loginAttempt(LoginRequestDto request) {
		String email = request.getEmail();
		String passwordInput = request.getPassword();
		
		CounsellorUser user = this.findCounsellorByEmail(email);

		if (!passwordEncoder.matches(passwordInput, user.getPassword())) {
			return false;
		}
		
		
		return true;
	}

	@Override
	public boolean loginAttempt(String email, String passwordInput) {
		
		CounsellorUser user = this.findCounsellorByEmail(email);
		
		if (!passwordEncoder.matches(passwordInput, user.getPassword())) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public void registerCounsellor(RegisterRequestDto request) {

		String email = request.getEmail();
		if (cRepo.findCounsellorByEmail(email).isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request");
		}
		
		CounsellorUser newUser = new CounsellorUser();
		newUser.setEmail(request.getEmail());
		
		// Encrypted password
		newUser.setPassword(passwordEncoder.encode(request.getPassword()));
		newUser.setFirstName(request.getFirstName());
		newUser.setLastName(request.getLastName());
		
		cRepo.save(newUser);
		
	}

	@Override
	public void linkRequest(String email, String senderId) {

	    JournalUser target = juserService.findJournalUserByEmail(email);
	    CounsellorUser sender = this.findCounsellorById(senderId);

		    // check if already exists
		    Optional<LinkRequest> existing = linkRepo.findAllByCounsellorAndJournalUser(sender, target);
		    if (existing.isPresent()) {
		    	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request");
		    }

		    LinkRequest req = new LinkRequest();
		    req.setCounsellorUser(sender);
		    req.setJournalUser(target);
		    req.setStatus(Status.PENDING);
		    req.setRequestedAt(LocalDateTime.now());

		    linkRepo.save(req);
	
	}

    @Override
    public List<JournalUser> listClients(String counsellorId) {
        return cRepo.findClients(counsellorId);
    }

    @Override
    public List<JournalEntry> listClientJournalEntries(String counsellorId, String journalUserId) {
        ensureLinked(counsellorId, journalUserId);
        return jRepo.findVisibleByUserId(journalUserId);
    }

    @Override
    public List<HabitsEntry> listClientHabitsEntries(String counsellorId, String journalUserId) {
        ensureLinked(counsellorId, journalUserId);
        return hRepo.findVisibleByUserId(journalUserId);
    }

    @Override
    public JournalEntry getJournalEntry(String counsellorId, String journalUserId, String entryId) {
        ensureLinked(counsellorId, journalUserId);
        return jRepo.findByIdAndUserId(entryId, journalUserId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entry not found"));
    }

    @Override
    public HabitsEntry getHabitsEntry(String counsellorId, String journalUserId, String entryId) {
        ensureLinked(counsellorId, journalUserId);
        return hRepo.findByIdAndUserId(entryId, journalUserId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entry not found"));
    }

    private void ensureLinked(String counsellorId, String journalUserId) {
        if (!cRepo.isLinkedTo(counsellorId, journalUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not linked to this user");
        }
    }	
	
	
	
}
