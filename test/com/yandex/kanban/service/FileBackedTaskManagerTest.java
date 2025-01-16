package com.yandex.kanban.service;

import com.yandex.kanban.model.Epic;
import com.yandex.kanban.model.Status;
import com.yandex.kanban.model.Subtask;
import com.yandex.kanban.model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;

public class FileBackedTaskManagerTest {

    @Test
    public void save() throws IOException {
        File tempFile = File.createTempFile("test", ".CSV");
        tempFile.deleteOnExit();
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(tempFile);
        Task task = new Task("testName", "testDescription");
        String strTask = "1,TASK,testName,null,testDescription,,null,null";
        Epic epic = new Epic("testEpicName", "testDescription");
        String strEpic = "2,EPIC,testEpicName,NEW,testDescription,,null,null";
        Subtask subtask = new Subtask("testSubtaskName", "testDescription", epic.getId());
        String strSubtask = "3,SUBTASK,testSubtaskName,NEW,testDescription,2,null,null";
        fileBackedTaskManager.addTask(task);
        fileBackedTaskManager.addEpic(epic);
        fileBackedTaskManager.addSubtasks(subtask, epic);
        Assertions.assertTrue(tempFile.length() > 0);
        FileReader fileReader = new FileReader(tempFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        LinkedList<String> fileContents = new LinkedList<>();
        while (bufferedReader.ready()) {
            String line = bufferedReader.readLine();
            fileContents.add(line);
        }
        Assertions.assertEquals(strTask, fileContents.getFirst());
        Assertions.assertEquals(strEpic, fileContents.get(1));
        Assertions.assertEquals(strSubtask, fileContents.get(2));
    }

    @Test
    public void fromString() {
        String testDadaTask = "1,TASK,Работа,null,по работать,,null,null";
        String testDataEpic = "8,EPIC,НОВЫЙ ЭПИК,NEW,ЭПИК,";
        String testDataSubtask = "11,SUBTASK,под,IN_PROGRESS,задача,1,2025-02-20T18:06:02.107800400,PT1H10M";
        Task task = new Task("Работа", "по работать");
        task.setId(1);
        Epic epic = new Epic("НОВЫЙ ЭПИК", "ЭПИК");
        epic.setId(8);
        Subtask subtask = new Subtask("под", "ПОДЗАДАЧА", Status.NEW, Duration.ofMinutes(20),
                LocalDateTime.of(2025, 02, 20, 18, 06, 02), 8);
        subtask.setId(11);
        Task resultTask = FileBackedTaskManager.fromString(testDadaTask);
        Assertions.assertEquals(task, resultTask);
        Task resultEpic = FileBackedTaskManager.fromString(testDataEpic);
        Assertions.assertEquals(epic, resultEpic);
        Task resultSubtask = FileBackedTaskManager.fromString(testDataSubtask);
        Assertions.assertEquals(subtask, resultSubtask);
        System.out.println(subtask);
        System.out.println(resultSubtask);
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
        String testDadaTask = "1,TASK,Работа,null,по работать,,null,null";
        String testDataEpic = "8,EPIC,НОВЫЙ ЭПИК,null,ЭПИК,,null,null";
        String testDataSubtask = "11,SUBTASK,НОВАЯ ПОДЗАДАЧА2,null,ПОДЗАДАЧА,8,null,null";
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
