package com.yandex.kanban.service;

import java.time.LocalDateTime;

public class TimeInterval {
    private final LocalDateTime start;
    private final LocalDateTime end;

    public TimeInterval(LocalDateTime start) {
        this.start = start;
        end = start.plusMinutes(15);
    }

    public TimeInterval(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "TimeInterval{startTime=" + start + ", endTime=" + end + "}";
    }

    public boolean intersect(TimeInterval taskInterval) {
        return taskInterval.start.isAfter(this.start) && taskInterval.start.isBefore(this.end)
                || taskInterval.end.isAfter(this.start) && taskInterval.end.isBefore(this.end)
                || (this.start.isAfter(taskInterval.start) && this.end.isBefore(taskInterval.end));
    }


}
