package com.moodyclues.projection;

import java.time.LocalDate;

public interface HabitsDayFlat {
    LocalDate getDay();
    Double getSleep();       
    Double getWater();      
    Double getWorkHours();  
}
