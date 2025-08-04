package com.moodyclues.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moodyclues.dto.HabitsEntryRequestDto;
import com.moodyclues.dto.HabitsEntryUpdateRequestDto;
import com.moodyclues.model.HabitsEntry;
import com.moodyclues.service.EntryService;

@RestController
@RequestMapping("/api/habits")
public class HabitsController {

	@Autowired
	EntryService entryService;

	@PostMapping("/submit")
	public ResponseEntity<?> submitHabits(HabitsEntryRequestDto request) {
		
		try {
			entryService.submitHabits(request);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			
		}
		
		return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
		
	}
	
	@GetMapping("/all")
	public ResponseEntity<?> getAllHabitsEntries(String userId) {
		
		try {
			List<HabitsEntry> hentries = entryService.getAllHabitsEntriesByUserId(userId);
			return new ResponseEntity<List<HabitsEntry>>(hentries, HttpStatus.OK);
		} catch (Exception e) {
			
		}
		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	
	@GetMapping("/{entryId}")
	public ResponseEntity<?> getJournalEntryById(@PathVariable String entryId) {
		
		try {
			HabitsEntry hentry = entryService.getHabitsEntryById(entryId);
			return new ResponseEntity<HabitsEntry>(hentry, HttpStatus.OK);
		} catch (Exception e) {
			
		}
		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@PutMapping("/{entryId}/edit")
	public ResponseEntity<?> editHabitsEntry(@PathVariable String entryId, @RequestBody HabitsEntryUpdateRequestDto request	) {
	
		try {
			entryService.updateHabitsEntry(request, entryId);
			return new ResponseEntity<>("Entry successfully edited.", HttpStatus.OK);
		} catch (Exception e) {
			
		}
		
		return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		
	}
	
	
	// This is to "delete" an entry
	@PutMapping("/{entryId}/archive")
	public ResponseEntity<?> archiveHabitsEntry(@PathVariable String entryId) {
		
		try {
			entryService.archiveHabitsEntry(entryId);
			return new ResponseEntity<>("Entry successfully deleted.", HttpStatus.OK);
		} catch (Exception e) {
			
		}
		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
