package com.moodyclues.projection;

import java.time.LocalDate;

public interface JournalDailyAgg {

	LocalDate getDay();
	Double getAvgMood();
	String getEntries();
	
}
