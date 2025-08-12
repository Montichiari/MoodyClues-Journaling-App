package com.moodyclues.dto;

import java.time.LocalDateTime;

import com.moodyclues.model.LinkRequest.Status;

import jakarta.validation.constraints.NotBlank;

public class CounsellorLinkRequestDto {

	@NotBlank
	private String clientEmail;

	public String getClientEmail() {
		return clientEmail;
	}

	public void setClientEmail(String clientEmail) {
		this.clientEmail = clientEmail;
	}
		
	
}
