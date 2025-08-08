package com.moodyclues.dto;

import java.time.LocalDateTime;

import com.moodyclues.model.LinkRequest.Status;

import jakarta.validation.constraints.NotBlank;

public class CounsellorLinkRequestDto {

	@NotBlank
	private String clientEmail;
	
	private LocalDateTime requestedAt;
	
	@NotBlank
	private Status status;
	
}
