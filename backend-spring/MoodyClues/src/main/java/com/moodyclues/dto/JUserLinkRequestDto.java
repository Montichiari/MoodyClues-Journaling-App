package com.moodyclues.dto;

import java.time.LocalDateTime;

import com.moodyclues.model.LinkRequest.Status;

import jakarta.validation.constraints.NotBlank;

public class JUserLinkRequestDto {

	@NotBlank
	private String counsellorEmail;
	
	private LocalDateTime requestedAt;
	
	@NotBlank
	private Status status;
	
	public JUserLinkRequestDto() {
		
	}

	public String getCounsellorEmail() {
		return counsellorEmail;
	}

	public void setCounsellorEmail(String counsellorEmail) {
		this.counsellorEmail = counsellorEmail;
	}

	public LocalDateTime getRequestedAt() {
		return requestedAt;
	}

	public void setRequestedAt(LocalDateTime requestedAt) {
		this.requestedAt = requestedAt;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	
	
}
