package com.yandex.kanban.service;

import com.yandex.kanban.model.Epic;
import com.yandex.kanban.model.Subtask;
import com.yandex.kanban.model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class FileBackedTaskManagerTest {

    @Test
    public void save() throws IOException {
        File tempFile = File.createTempFile("test", ".CSV");
        tempFile.deleteOnExit();
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(tempFile);
        Task task = new Task("testName", "testDescription");
        Epic epic = new Epic("testEpicName", "testDescription");
        Subtask subtask = new Subtask("testSubtaskName", "testDescription", epic.getId());
        fileBackedTaskManager.addTask(task);
        fileBackedTaskManager.addEpic(epic);
        fileBackedTaskManager.addSubtasks(subtask, epic);
        Assertions.assertTrue(tempFile.length() > 0);

    }

    @Test
    public void fromString() {
        String testDadaTask = "1,TASK,Работа,null,по работать,";
        String testDataEpic = "8,EPIC,НОВЫЙ ЭПИК,NEW,ЭПИК,";
        String testDataSubtask = "11,SUBTASK,НОВАЯ ПОДЗАДАЧА2,NEW,ПОДЗАДАЧА,8";
        Task task = new Task("Работа", "по работать");
        task.setId(1);
        Epic epic = new Epic("НОВЫЙ ЭПИК", "ЭПИК");
        epic.setId(8);
        Subtask subtask = new Subtask("НОВАЯ ПОДЗАДАЧА2", "ПОДЗАДАЧА", 8);
        subtask.setId(11);
        Task resultTask = FileBackedTaskManager.fromString(testDadaTask);
        Assertions.assertEquals(task, resultTask);
        Task resultEpic = FileBackedTaskManager.fromString(testDataEpic);
        Assertions.assertEquals(epic, resultEpic);
        Task resultSubtask = FileBackedTaskManager.fromString(testDataSubtask);
        Assertions.assertEquals(subtask, resultSubtask);
    }

    @Test
    public void toStringTask() throws IOException {
        File tempFile = File.createTempFile("test", ".CSV");
        tempFile.deleteOnExit();
        Task task = new Task("Работа", "по работать");
        task.setId(1);
        Epic epic = new Epic("НОВЫЙ ЭПИК", "ЭПИК");
        epic.setId(8);
        Subtask subtask = new Subtask("НОВАЯ ПОДЗАДАЧА2", "ПОДЗАДАЧА", 8);
        subtask.setId(11);
        String testDadaTask = "1,TASK,Работа,null,по работать,";
        String testDataEpic = "8,EPIC,НОВЫЙ ЭПИК,null,ЭПИК,";
        String testDataSubtask = "11,SUBTASK,НОВАЯ ПОДЗАДАЧА2,null,ПОДЗАДАЧА,8";
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(tempFile);
        Assertions.assertEquals(testDadaTask, fileBackedTaskManager.toStringTask(task).trim());
        Assertions.assertEquals(testDataEpic, fileBackedTaskManager.toStringTask(epic).trim());
        Assertions.assertEquals(testDataSubtask, fileBackedTaskManager.toStringTask(subtask).trim());

    }

    @Test
    public void loadFromFile() throws IOException {
        File tempFile = File.createTempFile("test", ".CSV");
        tempFile.deleteOnExit();
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(tempFile);
        Task task = new Task("testName", "testDescription");
        Epic epic = new Epic("testEpicName", "testDescription");
        Subtask subtask = new Subtask("testSubtaskName", "testDescription", epic.getId());
        fileBackedTaskManager.addTask(task);
        fileBackedTaskManager.addEpic(epic);
        fileBackedTaskManager.addSubtasks(subtask, epic);
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(tempFile);
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());
        Assertions.assertFalse(manager.getTasks().isEmpty());
        Assertions.assertFalse(manager.getEpics().isEmpty());
        Assertions.assertFalse(manager.getSubtasks().isEmpty());
    }
}
