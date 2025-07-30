package com.moodyclues.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "entries")
public class JournalEntry {

	@Id
	@GeneratedValue
	@UuidGenerator
	private String id;
	
	@ManyToOne
	private Journal journal;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime lastSavedAt;
	
	private int moodMorning;
	
	private int moodAfternoon;
	
	private int moodEvening;
	
	private double sleep;
	
	private double water;
	
	private double workHours;
	
	private String entryTitle;
	
	private String entryText;
	
	@ManyToMany
	@JoinTable(name = "entry_emotions",
	joinColumns = @JoinColumn(name = "entry_id"),
	inverseJoinColumns = @JoinColumn(name = "emotion_id"))
	private List<Emotion> emotions = new ArrayList<>();
	
	private boolean emotionFeedback;
	
	
	// EMPTY CONSTRUCTOR
	public JournalEntry() {
		this.createdAt = LocalDateTime.now();
	}
	
	// OTHER CONSTRUCTOR/s
	public JournalEntry(Journal journal) {
		this.journal = journal;
		this.createdAt = LocalDateTime.now();
	}

	
	//GETTERS AND SETTERS BELOW
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Journal getJournal() {
		return journal;
	}

	public void setJournal(Journal journal) {
		this.journal = journal;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getLastSavedAt() {
		return lastSavedAt;
	}

	public void setLastSavedAt(LocalDateTime lastSavedAt) {
		this.lastSavedAt = lastSavedAt;
	}

	public int getMoodMorning() {
		return moodMorning;
	}

	public void setMoodMorning(int moodMorning) {
		this.moodMorning = moodMorning;
	}

	public int getMoodAfternoon() {
		return moodAfternoon;
	}

	public void setMoodAfternoon(int moodAfternoon) {
		this.moodAfternoon = moodAfternoon;
	}

	public int getMoodEvening() {
		return moodEvening;
	}

	public void setMoodEvening(int moodEvening) {
		this.moodEvening = moodEvening;
	}

	public double getSleep() {
		return sleep;
	}

	public void setSleep(double sleep) {
		this.sleep = sleep;
	}

	public double getWater() {
		return water;
	}

	public void setWater(double water) {
		this.water = water;
	}

	public double getWorkHours() {
		return workHours;
	}

	public void setWorkHours(double workHours) {
		this.workHours = workHours;
	}

	public String getEntryTitle() {
		return entryTitle;
	}

	public void setEntryTitle(String entryTitle) {
		this.entryTitle = entryTitle;
	}

	public String getEntryText() {
		return entryText;
	}

	public void setEntryText(String entryText) {
		this.entryText = entryText;
	}

	public List<Emotion> getEmotions() {
		return emotions;
	}

	public void setEmotions(List<Emotion> emotions) {
		this.emotions = emotions;
	}

	public boolean isEmotionFeedback() {
		return emotionFeedback;
	}

	public void setEmotionFeedback(boolean emotionFeedback) {
		this.emotionFeedback = emotionFeedback;
	}
	
	
	
	
	
}
