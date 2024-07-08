package com.yandex.kanban;

import com.yandex.kanban.model.Epic;
import com.yandex.kanban.model.Subtask;
import com.yandex.kanban.model.Task;
import com.yandex.kanban.model.Status;
import com.yandex.kanban.service.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task1 = new Task("Сходить в магазин", "Купить продукты", Status.IN_PROGRESS);
        Task task2 = new Task("Уборка", "Помыть полы", Status.NEW);
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Отпуск", "Море");
        Epic epic2 = new Epic("Работа", "Расписание");
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        Subtask subtask1 = new Subtask("Одежда", "Купить плавки", epic1.getId());
        taskManager.addSubtasks(subtask1, epic1);

        Subtask subtask2 = new Subtask("Билеты", "бронь", epic1.getId());
        taskManager.addSubtasks(subtask2, epic1);

        Subtask subtask3 = new Subtask("График", "Календарь", epic2.getId());
        taskManager.addSubtasks(subtask3, epic2);

        System.out.println("Задачи: " + taskManager.getTasks());
        System.out.println("Эпики: " + taskManager.getEpics());
        System.out.println("Подзадачи: " + taskManager.getSubtasks());

        taskManager.updateTask(1, new Task("Сходить в магазин", "Купить продукты", Status.DONE));
        taskManager.updateEpic(3, new Epic("Отпуск", "Море"));
        taskManager.updateSubtask(7, new Subtask("График", "Календарь",
                Status.NEW, epic2.getId()), epic2);

        System.out.println("Задачи: " + taskManager.getTasks());
        System.out.println("Эпики: " + taskManager.getEpics());
        System.out.println("Подзадачи: " + taskManager.getSubtasks());

        taskManager.removeTaskById(1);
        taskManager.removeEpicById(3);

        System.out.println("Задачи: " + taskManager.getTasks());
        System.out.println("Эпики: " + taskManager.getEpics());
        System.out.println("Подзадачи: " + taskManager.getSubtasks());

    }


}
