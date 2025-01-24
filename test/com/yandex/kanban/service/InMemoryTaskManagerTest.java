package com.yandex.kanban.service;

import com.yandex.kanban.model.Epic;
import com.yandex.kanban.model.Subtask;
import com.yandex.kanban.model.Task;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;


class InMemoryTaskManagerTest {

    Task task;
    Epic epic;
    Subtask subtask;
    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @Test
    void addTaskTest() {
        task = new Task("testName", "testDescription", Duration.ofMinutes(10), LocalDateTime.now());
        Assertions.assertTrue(taskManager.getTasks().isEmpty());
        Assertions.assertTrue(taskManager.getPrioritizedTasks().isEmpty());
        taskManager.addTask(task);
        Assertions.assertFalse(taskManager.getTasks().isEmpty());
        Assertions.assertFalse(taskManager.getPrioritizedTasks().isEmpty());
        Task task2 = new Task("testName1", "testDescription1");
        taskManager.addTask(task2);
        Assertions.assertEquals(2, taskManager.getTasks().size());
        Assertions.assertEquals(1, taskManager.getPrioritizedTasks().size());
        System.out.println("-" + taskManager.getPrioritizedTasks());
        Task task1 = new Task("testName", "testDescription");
        taskManager.addTask(task1);
        Assertions.assertEquals(2, taskManager.getTasks().size());
        Assertions.assertEquals(1, taskManager.getPrioritizedTasks().size());
        Assertions.assertNotEquals(task.getId(), task2.getId(), "ID объектов равны!");
    }

    @Test
    void addEpicTest() {
        epic = new Epic("testName", "testDescription");
        Assertions.assertTrue(taskManager.getEpics().isEmpty());
        taskManager.addEpic(epic);
        Assertions.assertFalse(taskManager.getEpics().isEmpty());
        Epic epic1 = new Epic("testName1", "testDescription1");
        taskManager.addEpic(epic1);
        Assertions.assertNotEquals(epic.getId(), epic1.getId(), "ID объектов равны!");
    }


    @Test
    void addSubTaskTest() {
        epic = new Epic("testName1", "testDescription");
        subtask = new Subtask("testName", "testDescription", epic.getId());
        Assertions.assertTrue(taskManager.getSubtasks().isEmpty());
        Assertions.assertTrue(taskManager.getPrioritizedTasks().isEmpty());
        taskManager.addSubtasks(subtask, epic);
        Assertions.assertFalse(taskManager.getSubtasks().isEmpty());
        Subtask subtask2 = new Subtask("Subtask2", "descrip", Duration.ofMinutes(10),
                LocalDateTime.now(), epic.getId());
        taskManager.addSubtasks(subtask2, epic);
        Assertions.assertEquals(2, taskManager.getSubtasks().size());
        Assertions.assertEquals(1, taskManager.getPrioritizedTasks().size());
        Subtask subtask1 = new Subtask("testName", "testDescription", epic.getId());
        taskManager.addSubtasks(subtask1, epic);
        Assertions.assertEquals(2, taskManager.getSubtasks().size());
        Assertions.assertEquals(1, taskManager.getPrioritizedTasks().size());
        Assertions.assertNotEquals(subtask.getId(), subtask2.getId(), "ID объектов равны!");
    }

    @Test
    void removeAllTasksTest() {
        Assertions.assertTrue(taskManager.getTasks().isEmpty());
        Assertions.assertTrue(taskManager.getPrioritizedTasks().isEmpty());
        task = new Task("testName", "testDescription");
        Task task1 = new Task("testName1", "testDescription");
        Task task2 = new Task("testName2", "testDescription", Duration.ofMinutes(10), LocalDateTime.now());
        taskManager.addTask(task);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        Assertions.assertFalse(taskManager.getTasks().isEmpty());
        Assertions.assertEquals(3, taskManager.getTasks().size());
        taskManager.removeAllTasks();
        Assertions.assertTrue(taskManager.getTasks().isEmpty());
        Assertions.assertTrue(taskManager.getPrioritizedTasks().isEmpty());
    }

    @Test
    void removeAllEpicsTest() {
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
    void removeAllSubtasksTest() {
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
    void removeTaskByIdTest() {
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
    void removeEpicByIdTest() {
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
    void removeSubtaskByIdTest() {
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

    @Test
    public void getPrioritizedTasks() {
        task = new Task("testNameTask", "description", Duration.ofMinutes(10), LocalDateTime.now()
                .plusMonths(1));
        epic = new Epic("testNameEpic", "description");
        taskManager.addEpic(epic);
        Task task1 = new Task("testNameTask1", "description", Duration.ofMinutes(10), LocalDateTime.now()
                .plusMonths(3));
        subtask = new Subtask("testNameSubtask", "description", Duration.ofMinutes(10), LocalDateTime
                .now().plusMonths(7), epic.getId());
        taskManager.addSubtasks(subtask, epic);
        taskManager.addTask(task);
        taskManager.addTask(task1);
        for (Task t : taskManager.getPrioritizedTasks()){
            System.out.println("-" + t);
        }
        Assertions.assertEquals(task, taskManager.getPrioritizedTasks().getFirst());
        Assertions.assertEquals(subtask, taskManager.getPrioritizedTasks().getLast());
    }

}