package com.moodyclues.model;

import org.hibernate.annotations.UuidGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "emotions")
public class Emotion {

	@Id
	@GeneratedValue
	@UuidGenerator
	private String id;
	
	private String emotionLabel;
	
	@JsonIgnore
	private String iconAddress;
	
	
	// EMPTY CONSTRUCTOR
	public Emotion() {
		
	}


	// GETTERS AND SETTERS BELOW
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getEmotionLabel() {
		return emotionLabel;
	}


	public void setEmotionLabel(String emotionLabel) {
		this.emotionLabel = emotionLabel;
	}


	public String getIconAddress() {
		return iconAddress;
	}


	public void setIconAddress(String iconAddress) {
		this.iconAddress = iconAddress;
	}
	
	
	
}

