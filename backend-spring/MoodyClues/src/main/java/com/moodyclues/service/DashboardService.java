package com.moodyclues.service;

import java.time.LocalDate;
import java.util.Map;

public interface DashboardService {

	public Map<String,Object> getDashboardPayload(String userId, Integer days, LocalDate from, LocalDate to);
	
}
