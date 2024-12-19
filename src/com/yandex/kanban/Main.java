package com.yandex.kanban;

import com.yandex.kanban.model.Epic;
import com.yandex.kanban.model.Status;
import com.yandex.kanban.model.Subtask;
import com.yandex.kanban.model.Task;
import com.yandex.kanban.service.FileBackedTaskManager;

import java.io.File;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        File file = new File("src/com/yandex/kanban/file/manager.CSV");
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(file);
        System.out.println("Задачи: " + fileBackedTaskManager.getTasks());
        System.out.println("Эпики: " + fileBackedTaskManager.getEpics());
        System.out.println("Подзадачи: " + fileBackedTaskManager.getSubtasks());
        Task task = new Task("New", "по работать");
        fileBackedTaskManager.addTask(task);
        Epic epic = new Epic("История", "Яндекс");
        fileBackedTaskManager.addEpic(epic);
        Epic epic1 = new Epic("NEW Эпик", "ЭПИК");
        fileBackedTaskManager.addEpic(epic1);
        Subtask subtask = new Subtask("НОВАЯ ПОДЗАДАЧА2", "ПОДЗАДАЧА", epic1.getId());
        fileBackedTaskManager.addSubtasks(subtask, epic1);
        Task task1 = new Task("Поход", "Купить хлеб");
        fileBackedTaskManager.addTask(task1);
        Task task2 = new Task("Очень новая", "Задача", Status.NEW);
        fileBackedTaskManager.addTask(task2);

        System.out.println("Задачи: " + fileBackedTaskManager.getTasks());
        System.out.println("Эпики: " + fileBackedTaskManager.getEpics());
        System.out.println("Подзадачи: " + fileBackedTaskManager.getSubtasks());
    }

    public static void printHistory(List<Task> tasks) {
        System.out.println("ИСТОРИЯ: ");
        int number = 0;
        for (Task t : tasks) {
            number++;
            System.out.println(number + " Элемент: " + t);
        }
    }


}
