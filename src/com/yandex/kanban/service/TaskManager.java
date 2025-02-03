package com.yandex.kanban.service;

import com.yandex.kanban.model.Epic;
import com.yandex.kanban.model.Status;
import com.yandex.kanban.model.Subtask;
import com.yandex.kanban.model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    int addTask(Task task);

    int addEpic(Epic epic);

    void addStatusEpic(Epic epic);

    ArrayList<Status> getAllStatus();

    int addSubtasks(Subtask subtask, Epic epic);

    boolean removeAllTasks();

    boolean removeAllEpics();

    boolean removeAllSubtasks();

    boolean removeTaskById(int id);

    boolean removeEpicById(int id);

    boolean removeSubtasksById(int id);

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtasks();

    boolean updateTask(int id, Task task);

    boolean updateEpic(int id, Epic epic);

    boolean updateSubtask(int id, Subtask subtask, Epic epic);

    ArrayList<Subtask> getSubtaskByEpic(Epic epic);

    Calendar getCalendar();

    HistoryManager getInMemoryHistoryManager();

    Object getPrioritizedTasks();

    // List<Task> getHistory();
}
