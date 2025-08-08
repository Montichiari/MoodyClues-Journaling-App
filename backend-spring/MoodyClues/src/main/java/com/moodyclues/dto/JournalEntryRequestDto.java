package com.moodyclues.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class JournalEntryRequestDto {

	@NotBlank
	private String userId;
	
	@NotNull
	private int mood;
	
	@NotBlank
	private String entryTitle;
	
	@NotBlank
	private String entryText;
	
	@NotBlank
	private List<String> emotions;

	

	public int getMood() {
		return mood;
	}

	public void setMood(int mood) {
		this.mood = mood;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public List<String> getEmotions() {
		return emotions;
	}

	public void setEmotions(List<String> emotions) {
		this.emotions = emotions;
	}
	
	
	
}
