package com.yandex.kanban.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {

    private ArrayList<Integer> subtasksId;

    private LocalDateTime endTime;

    private final HashMap<Integer, LocalDateTime> subtasksIdAndCreateTime;

    public Epic(String name, String description) {
        super(name, description);
        subtasksId = new ArrayList<>();
        subtasksIdAndCreateTime = new HashMap<>();
    }

    @Override
    public String toString() {
        return "com.yandex.kanban.model.Epic{name=" + name + ", description=" + description + ", id=" + id
                + ", status=" + status + "," + " subtasksId=" + subtasksId + ", startTime=" + startTime
                + ", duration=" + duration + ", endTime=" + endTime + "}";
    }

    public ArrayList<Integer> getSubtasksId() {
        return subtasksId;
    }

    public HashMap<Integer, LocalDateTime> getSubtasksIdAndCreateTime() {
        return subtasksIdAndCreateTime;
    }

    public void addSubtaskId(int id) {
        subtasksId.add(id);
    }

    public void addSubtaskIdAndStartTime(int id, LocalDateTime localDateTime) {
        subtasksIdAndCreateTime.put(id, localDateTime);
    }

    public void clearSubtasksId() {
        subtasksId.clear();
    }

    public void removeSubtaskId(int id) {
        subtasksId.remove(id);
    }

    public void setSubtasksId(ArrayList<Integer> subtasksId) {
        this.subtasksId = subtasksId;
    }

    public LocalDateTime getStartTimeForEpic() {
        return subtasksIdAndCreateTime.values().stream()
                .min(LocalDateTime::compareTo)
                .orElse(null);
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
