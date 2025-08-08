package com.moodyclues.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class HabitsEntryRequestDto {

	@NotBlank
	private String userId;
	
	@NotNull
	private double sleep;
	
	@NotNull
	private double water;
	
	@NotNull
	private double workHours;

	
	
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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
	
	
	
}
