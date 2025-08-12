package com.moodyclues.serviceimpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moodyclues.projection.HabitsDayFlat;
import com.moodyclues.projection.JournalDailyAgg;
import com.moodyclues.repository.HabitsEntryRepository;
import com.moodyclues.repository.JournalEntryRepository;
import com.moodyclues.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {

	@Autowired
	JournalEntryRepository journalEntryRepo;
	
	@Autowired
	HabitsEntryRepository habitsEntryRepo;
	
	@Override
	public Map<String, Object> getDashboardPayload(String userId, Integer days, LocalDate from, LocalDate to) {
		
	    ZoneId zone = ZoneId.of("Asia/Singapore");
	    LocalDate today = (to != null) ? to : LocalDate.now(zone);
	    LocalDate startDate = (from != null && to != null)
	            ? from
	            : today.minusDays((days == null ? 7 : Math.max(1, days)) - 1);

	    LocalDateTime startTs = startDate.atStartOfDay();
	    LocalDateTime endTs   = today.plusDays(1).atStartOfDay().minusNanos(1);

	    var journalDaily   = journalEntryRepo.findDailyAggBetween(userId, startTs, endTs);
	    var habitsDays     = habitsEntryRepo.findDaysBetween(userId, startTs, endTs);
	    var emotionCounts  = journalEntryRepo.countEmotionsBetween(userId, startTs, endTs);


	    Double avgMoodSelected = journalDaily.stream()
	            .map(JournalDailyAgg::getAvgMood)
	            .filter(Objects::nonNull)
	            .mapToDouble(Double::doubleValue)
	            .average()
	            .orElse(Double.NaN);
	    avgMoodSelected = Double.isNaN(avgMoodSelected) ? null : Math.round(avgMoodSelected * 100.0) / 100.0;


	    Double avgSleepHoursSelected = habitsDays.stream()
	            .map(HabitsDayFlat::getSleep)
	            .filter(Objects::nonNull)
	            .mapToDouble(Double::doubleValue)
	            .average()
	            .orElse(Double.NaN);
	    avgSleepHoursSelected = Double.isNaN(avgSleepHoursSelected) ? null : Math.round(avgSleepHoursSelected * 100.0) / 100.0;

	    Double avgWaterLitresSelected = habitsDays.stream()
	            .map(HabitsDayFlat::getWater)
	            .filter(Objects::nonNull)
	            .mapToDouble(Double::doubleValue)
	            .average()
	            .orElse(Double.NaN);
	    avgWaterLitresSelected = Double.isNaN(avgWaterLitresSelected) ? null : Math.round(avgWaterLitresSelected * 100.0) / 100.0;

	    Double avgWorkHoursSelected = habitsDays.stream()
	            .map(HabitsDayFlat::getWorkHours)
	            .filter(Objects::nonNull)
	            .mapToDouble(Double::doubleValue)
	            .average()
	            .orElse(Double.NaN);
	    avgWorkHoursSelected = Double.isNaN(avgWorkHoursSelected) ? null : Math.round(avgWorkHoursSelected * 100.0) / 100.0;


	    Map<String,Object> window = new LinkedHashMap<>();
	    window.put("from", startDate);
	    window.put("to", today);

	    Map<String,Object> series = new LinkedHashMap<>();
	    series.put("journalDaily", journalDaily);
	    series.put("habitsByDay",  habitsDays);
	    series.put("emotionCounts", emotionCounts);

	    Map<String,Object> summary = new LinkedHashMap<>();
	    summary.put("avgMoodSelected",        avgMoodSelected);
	    summary.put("avgSleepHoursSelected",  avgSleepHoursSelected);
	    summary.put("avgWaterLitresSelected", avgWaterLitresSelected);
	    summary.put("avgWorkHoursSelected",   avgWorkHoursSelected);
	    summary.put("daysWithJournal",        journalDaily.size());
	    summary.put("daysWithHabits",         habitsDays.size());

	    Map<String,Object> payload = new LinkedHashMap<>();
	    payload.put("window", window);
	    payload.put("series", series);
	    payload.put("summary", summary);

	    return payload;
	}

}
