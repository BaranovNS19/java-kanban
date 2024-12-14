package com.yandex.kanban.service;

public class Managers<T extends TaskManager> {
    public TaskManager getDefault() {
        return null;
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
