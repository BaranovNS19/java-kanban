package com.yandex.kanban.service;

import com.yandex.kanban.model.Task;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;

public class Calendar {
    private final LocalDateTime START_CALENDAR = LocalDateTime.now();
    private final LocalDateTime END_CALENDAR = LocalDateTime.now().plusYears(1);
    private final LinkedHashMap<TimeInterval, Boolean> calendar = new LinkedHashMap<>();

    public Calendar() {
        createCalendar();
    }

    public LocalDateTime getSTART_CALENDAR() {
        return START_CALENDAR;
    }

    public LocalDateTime getEND_CALENDAR() {
        return END_CALENDAR;
    }

    private void createCalendar() {
        LocalDateTime startFirstInterval = START_CALENDAR;
        while (startFirstInterval.isBefore(END_CALENDAR)) {
            TimeInterval timeInterval = new TimeInterval(startFirstInterval);
            calendar.put(timeInterval, true);
            startFirstInterval = startFirstInterval.plusMinutes(15);
        }
    }

    public LinkedHashMap<TimeInterval, Boolean> getCalendar() {
        return calendar;
    }

    public void updateCalendar(Task task){
        for (TimeInterval t : calendar.keySet()){
            if (t.intersect(new TimeInterval(task.getStartTime(), task.getEndTime())) && !calendar.get(t)){
                calendar.put(t, true);
            }
        }
    }
}
