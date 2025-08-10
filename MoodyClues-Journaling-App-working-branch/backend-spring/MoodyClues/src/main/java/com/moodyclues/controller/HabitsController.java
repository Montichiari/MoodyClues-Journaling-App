package com.moodyclues.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> submitHabits(@RequestBody HabitsEntryRequestDto request) {
        try {
            entryService.submitHabits(request);
            return new ResponseEntity<>("Habits entry submitted successfully.", HttpStatus.OK);
        } catch (Exception e) {
        }
        return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<?> getAllHabitsEntries(@PathVariable String userId) {
        try {
            List<HabitsEntry> hentries = entryService.getAllHabitsEntriesByUserId(userId);
            return new ResponseEntity<List<HabitsEntry>>(hentries, HttpStatus.OK);
        } catch (Exception e) {
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{entryId}/{userId}")
    public ResponseEntity<?> getHabitsEntryById(@PathVariable String entryId,
                                                @PathVariable String userId) {
        try {
            HabitsEntry hentry = entryService.getHabitsEntryById(entryId);
            if (hentry == null || hentry.getUser() == null || !userId.equals(hentry.getUser().getId())) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity<HabitsEntry>(hentry, HttpStatus.OK);
        } catch (Exception e) {
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{entryId}/{userId}/edit")
    public ResponseEntity<?> editHabitsEntry(@PathVariable String entryId,
                                             @PathVariable String userId,
                                             @RequestBody HabitsEntryUpdateRequestDto request) {
        try {
            HabitsEntry existing = entryService.getHabitsEntryById(entryId);
            if (existing == null || existing.getUser() == null || !userId.equals(existing.getUser().getId())) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            entryService.updateHabitsEntry(request, entryId);
            return new ResponseEntity<>("Entry successfully edited.", HttpStatus.OK);
        } catch (Exception e) {
        }
        return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
    }

    @PutMapping("/{entryId}/{userId}/archive")
    public ResponseEntity<?> archiveHabitsEntry(@PathVariable String entryId,
                                                @PathVariable String userId) {
        try {
            HabitsEntry existing = entryService.getHabitsEntryById(entryId);
            if (existing == null || existing.getUser() == null || !userId.equals(existing.getUser().getId())) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            entryService.archiveHabitsEntry(entryId);
            return new ResponseEntity<>("Entry successfully deleted.", HttpStatus.OK);
        } catch (Exception e) {
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
