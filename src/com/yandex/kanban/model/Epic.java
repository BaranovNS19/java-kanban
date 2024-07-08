package com.yandex.kanban.model;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subtasksId;

    public Epic(String name, String description) {
        super(name, description);
        subtasksId = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "com.yandex.kanban.model.Epic{name=" + name + ", description=" + description + ", id=" + id + ", status=" + status + "," +
                " subtasksId=" + subtasksId + "}";
    }

    public ArrayList<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void addSubtaskId(int id) {
        subtasksId.add(id);
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
}
