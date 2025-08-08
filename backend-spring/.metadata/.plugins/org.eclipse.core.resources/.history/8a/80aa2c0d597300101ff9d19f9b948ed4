package com.moodyclues.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moodyclues.dto.JournalEntryRequestDto;
import com.moodyclues.model.JournalEntry;
import com.moodyclues.service.EntryService;

@RestController
@RequestMapping("/api/journal")
public class JournalController {

	@Autowired
	EntryService entryService;
	
	@PostMapping("/submit")
	public ResponseEntity<?> submitJournalEntry(JournalEntryRequestDto request) {
		
		try {
			entryService.submitEntry(request);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			
		}
		
		return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
		
	}
	
	
	@GetMapping("/all")
	public ResponseEntity<?> getAllJournalEntries(String userId) {
		
		try {
			List<JournalEntry> jentries = entryService.getAllJournalEntriesByUserId(userId);
			return new ResponseEntity<List<JournalEntry>>(jentries, HttpStatus.OK);
		} catch (Exception e) {
			
		}
		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	
	@GetMapping("/{entryId}")
	public ResponseEntity<?> getJournalEntryById(@PathVariable String entryId) {
		
		try {
			JournalEntry jentry = entryService.getJournalEntryById(entryId);
			return new ResponseEntity<JournalEntry>(jentry, HttpStatus.OK);
		} catch (Exception e) {
			
		}
		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	
	// "Delete"
	@PutMapping("/{entryId}/archive")
	public ResponseEntity<?> archiveJournalEntry(@PathVariable String entryId) {
		
		try {
			entryService.archiveJournalEntry(entryId);
			return new ResponseEntity<>("Entry successfully deleted.", HttpStatus.OK);
		} catch (Exception e) {
			
		}
		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	
}
