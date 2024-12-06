package com.yandex.kanban;

import com.yandex.kanban.model.Epic;
import com.yandex.kanban.model.Subtask;
import com.yandex.kanban.model.Task;
import com.yandex.kanban.service.InMemoryTaskManager;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task task = new Task("Покупка", "Купить хлеб");
        Task task1 = new Task("Автоматизация", "Автоматизировать задачу");
        taskManager.addTask(task);
        taskManager.addTask(task1);

        Epic epic = new Epic("MVP", "Релиз");
        taskManager.addEpic(epic);

        Subtask subtask = new Subtask("Анализ", "Написать требования", epic.getId());
        taskManager.addSubtasks(subtask, epic);
        Subtask subtask1 = new Subtask("Разработка", "Написать код", epic.getId());
        taskManager.addSubtasks(subtask1, epic);
        Subtask subtask2 = new Subtask("Тестирование", "Протестировать", epic.getId());
        taskManager.addSubtasks(subtask2, epic);

        Epic epic1 = new Epic("Покупка", "Купить телефон");
        taskManager.addEpic(epic1);

        taskManager.getEpicById(epic.getId());
        taskManager.getTaskById(task.getId());
        taskManager.getSubtaskById(subtask2.getId());
        taskManager.getSubtaskById(subtask.getId());
        taskManager.getSubtaskById(subtask1.getId());
        taskManager.getTaskById(task1.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getSubtaskById(subtask2.getId());
        taskManager.getTaskById(task.getId());
        taskManager.getSubtaskById(subtask1.getId());
        printHistory(taskManager.getInMemoryHistoryManager().getHistory());
        taskManager.removeTaskById(task1.getId());
        printHistory(taskManager.getInMemoryHistoryManager().getHistory());
        taskManager.removeEpicById(epic.getId());
        printHistory(taskManager.getInMemoryHistoryManager().getHistory());
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
