package com.moodyclues.dto;

public class LoginResponseDto {

	private String userId;
	private boolean showEmotion;
	
	
	public LoginResponseDto() {
		
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public boolean isShowEmotion() {
		return showEmotion;
	}


	public void setShowEmotion(boolean showEmotion) {
		this.showEmotion = showEmotion;
	}
	
	
	
	
}
