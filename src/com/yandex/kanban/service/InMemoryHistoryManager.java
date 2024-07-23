package com.yandex.kanban.service;

import com.yandex.kanban.model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    private final List<Task> history = new ArrayList<>();

    private static final int MAX_SIZE_HISTORY = 10;

    @Override
    public void addTask(Task task) {
        updateHistory(history);
        history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }

    public void updateHistory(List<Task> tasksHistory) {
        if (tasksHistory.size() >= MAX_SIZE_HISTORY) {
            tasksHistory.remove(0);
        }
    }

    @Override
    public String toString(){
        return "История просмотра: " + history;
    }
}
