package com.yandex.kanban.service;

public class Managers<T extends TaskManager> {
    private final T manager;

    public Managers(T manager) {
        this.manager = manager;
    }

    public TaskManager getDefault() {
        return manager;
    }

    public HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
