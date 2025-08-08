package com.moodyclues.dto;

import java.time.LocalDateTime;

public class HabitsEntryUpdateRequestDto {
	
	private double sleep;
	
	private double water;
	
	private double workHours;

	
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