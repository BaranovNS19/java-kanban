package com.yandex.kanban.service;

import com.yandex.kanban.model.Task;

import java.util.List;

public interface HistoryManager {

    void addTaskInHistory(Task task);

    List<Task> getHistory();

    void remove(int id);
}
