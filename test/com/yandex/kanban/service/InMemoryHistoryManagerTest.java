package com.yandex.kanban.service;

import com.yandex.kanban.model.Epic;
import com.yandex.kanban.model.Subtask;
import com.yandex.kanban.model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InMemoryHistoryManagerTest {

    Task task;
    Epic epic;
    Subtask subtask;
    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @BeforeEach
    public void setUp() {
        task = new Task("testName", "testDescription");
        taskManager.addTask(task);
        Assertions.assertTrue(inMemoryHistoryManager.getHistory().isEmpty());
        inMemoryHistoryManager.add(task);
    }

    @Test
    public void addTaskInHistory() {
        Assertions.assertEquals(1, inMemoryHistoryManager.getHistory().size());
        epic = new Epic("testName", "testDescription");
        taskManager.addEpic(epic);
        inMemoryHistoryManager.add(epic);
        Assertions.assertEquals(2, inMemoryHistoryManager.getHistory().size());
        subtask = new Subtask("testName", "testDescription", epic.getId());
        taskManager.addSubtasks(subtask, epic);
        inMemoryHistoryManager.add(subtask);
        Assertions.assertEquals(3, inMemoryHistoryManager.getHistory().size());
    }

    @Test
    public void addIdenticalTasks() {
        Assertions.assertEquals(1, inMemoryHistoryManager.getHistory().size());
        inMemoryHistoryManager.add(task);
        Assertions.assertEquals(1, inMemoryHistoryManager.getHistory().size());
    }

    @Test
    public void remove() {
        epic = new Epic("testName", "testDescription");
        taskManager.addEpic(epic);
        inMemoryHistoryManager.add(epic);
        subtask = new Subtask("testName", "testDescription", epic.getId());
        taskManager.addSubtasks(subtask, epic);
        inMemoryHistoryManager.add(subtask);
        Assertions.assertEquals(3, inMemoryHistoryManager.getHistory().size());
        inMemoryHistoryManager.remove(task.getId());
        Assertions.assertEquals(2, inMemoryHistoryManager.getHistory().size());
        inMemoryHistoryManager.remove(epic.getId());
        Assertions.assertEquals(1, inMemoryHistoryManager.getHistory().size());
    }

    @Test
    public void getHistory() {
        Assertions.assertEquals(1, inMemoryHistoryManager.getHistory().size());
        Assertions.assertEquals(1, inMemoryHistoryManager.getHistory().get(0).getId());
    }


}
