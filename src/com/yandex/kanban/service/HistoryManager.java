package com.yandex.kanban.service;

import com.yandex.kanban.model.Task;

import java.util.List;

public interface HistoryManager {

    void addTask(Task task);

    List<Task> getHistory();
}
