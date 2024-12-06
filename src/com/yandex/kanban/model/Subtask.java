package com.yandex.kanban.model;

public class Subtask extends Task {

    protected int epicId;

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Status status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "com.yandex.kanban.model.Subtask{name=" + name + ", description=" + description + ", id=" + id + ", status=" + status + ", " +
                "epicId=" + epicId + "}";
    }

}
