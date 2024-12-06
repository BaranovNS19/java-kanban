package com.yandex.kanban.service;

import com.yandex.kanban.model.Epic;
import com.yandex.kanban.model.Subtask;
import com.yandex.kanban.model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class InMemoryTaskManagerTest {

    Task task;
    Epic epic;
    Subtask subtask;
    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @Test
    void addTaskTest() {
        task = new Task("testName", "testDescription");
        Assertions.assertTrue(taskManager.getTasks().isEmpty());
        taskManager.addTask(task);
        Assertions.assertFalse(taskManager.getTasks().isEmpty());
        Task task1 = new Task("testName", "testDescription");
        taskManager.addTask(task1);
        Assertions.assertNotEquals(task.getId(), task1.getId(), "ID объектов равны!");
    }

    @Test
    void addEpicTest() {
        epic = new Epic("testName", "testDescription");
        Assertions.assertTrue(taskManager.getEpics().isEmpty());
        taskManager.addEpic(epic);
        Assertions.assertFalse(taskManager.getEpics().isEmpty());
        Epic epic1 = new Epic("testName", "testDescription");
        taskManager.addEpic(epic1);
        Assertions.assertNotEquals(epic.getId(), epic1.getId(), "ID объектов равны!");
    }


    @Test
    void addSubTaskTest() {
        epic = new Epic("testName", "testDescription");
        subtask = new Subtask("testName", "testDescription", epic.getId());
        Assertions.assertTrue(taskManager.getSubtasks().isEmpty());
        taskManager.addSubtasks(subtask, epic);
        Assertions.assertFalse(taskManager.getSubtasks().isEmpty());
        Subtask subtask1 = new Subtask("testName", "testDescription", epic.getId());
        taskManager.addSubtasks(subtask1, epic);
        Assertions.assertNotEquals(subtask.getId(), subtask1.getId(), "ID объектов равны!");
    }

    @Test
    void removeAllTasksTest() {
        Assertions.assertTrue(taskManager.getTasks().isEmpty());
        task = new Task("testName", "testDescription");
        Task task1 = new Task("testName", "testDescription");
        taskManager.addTask(task);
        taskManager.addTask(task1);
        Assertions.assertFalse(taskManager.getTasks().isEmpty());
        taskManager.removeAllTasks();
        Assertions.assertTrue(taskManager.getTasks().isEmpty());
    }

    @Test
    void removeAllEpicsTest(){
        Assertions.assertTrue(taskManager.getEpics().isEmpty());
        epic = new Epic("testName", "testDescription");
        Epic epic1 = new Epic("testName", "testDescription");
        taskManager.addEpic(epic);
        taskManager.addEpic(epic1);
        Assertions.assertFalse(taskManager.getEpics().isEmpty());
        taskManager.removeAllEpics();
        Assertions.assertTrue(taskManager.getEpics().isEmpty());
    }

    @Test
    void removeAllSubtasksTest(){
        Assertions.assertTrue(taskManager.getSubtasks().isEmpty());
        epic = new Epic("testName", "testDescription");
        subtask = new Subtask("testName", "testDescription", epic.getId());
        Subtask subtask1 = new Subtask("testName", "testDescription", epic.getId());
        taskManager.addSubtasks(subtask, epic);
        taskManager.addSubtasks(subtask1, epic);
        Assertions.assertFalse(taskManager.getSubtasks().isEmpty());
        taskManager.removeAllEpics();
        Assertions.assertTrue(taskManager.getSubtasks().isEmpty());
    }

    @Test
    void removeTaskByIdTest(){
        Assertions.assertTrue(taskManager.getTasks().isEmpty());
        task = new Task("testName", "testDescription");
        Task task1 = new Task("testName", "testDescription");
        taskManager.addTask(task);
        taskManager.addTask(task1);
        Assertions.assertFalse(taskManager.getTasks().isEmpty());
        taskManager.removeTaskById(task1.getId());
        Assertions.assertFalse(taskManager.getTasks().isEmpty());
    }

    @Test
    void removeEpicByIdTest(){
        Assertions.assertTrue(taskManager.getEpics().isEmpty());
        epic = new Epic("testName", "testDescription");
        Epic epic1 = new Epic("testName", "testDescription");
        taskManager.addEpic(epic);
        taskManager.addEpic(epic1);
        Assertions.assertFalse(taskManager.getEpics().isEmpty());
        taskManager.removeEpicById(epic1.getId());
        Assertions.assertFalse(taskManager.getEpics().isEmpty());
    }

    @Test
    void removeSubtaskByIdTest(){
        Assertions.assertTrue(taskManager.getSubtasks().isEmpty());
        epic = new Epic("testName", "testDescription");
        taskManager.addEpic(epic);
        subtask = new Subtask("testName", "testDescription", epic.getId());
        Subtask subtask1 = new Subtask("testName1", "testDescription", epic.getId());
        taskManager.addSubtasks(subtask, epic);
        taskManager.addSubtasks(subtask1, epic);
        Assertions.assertFalse(taskManager.getSubtasks().isEmpty());
        taskManager.removeSubtasksById(subtask.getId());
        Assertions.assertFalse(taskManager.getSubtasks().isEmpty());
    }

}