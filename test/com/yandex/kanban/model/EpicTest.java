package com.yandex.kanban.model;

import com.yandex.kanban.service.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EpicTest {
    InMemoryTaskManager manager = new InMemoryTaskManager();
    Epic epic = new Epic("testName", "testDescription");
    Subtask subtask1 = new Subtask("nameSubtask1", "descriptionSubtask1", epic.getId());
    Subtask subtask2 = new Subtask("nameSubtask2", "descriptionSubtask2", epic.getId());
    Subtask subtask3 = new Subtask("nameSubtask3", "descriptionSubtask3", epic.getId());
    Subtask subtaskWithStatusNew1 = new Subtask("nameSubtask1", "descriptionSubtask1", Status.NEW,
            epic.getId());
    Subtask subtaskWithStatusNew2 = new Subtask("nameSubtask2", "descriptionSubtask2", Status.NEW,
            epic.getId());
    Subtask subtaskWithStatusNew3 = new Subtask("nameSubtask3", "descriptionSubtask3", Status.NEW,
            epic.getId());
    Subtask subtaskWithStatusDone1 = new Subtask("nameSubtask1", "descriptionSubtask1", Status.DONE,
            epic.getId());
    Subtask subtaskWithStatusDone2 = new Subtask("nameSubtask2", "descriptionSubtask2", Status.DONE,
            epic.getId());
    Subtask subtaskWithStatusDone3 = new Subtask("nameSubtask3", "descriptionSubtask3", Status.DONE,
            epic.getId());

    @Test
    public void getEpicStatusWithSubtasksWithoutStatuses() {
        manager.addEpic(epic);
        manager.addSubtasks(subtask1, epic);
        manager.addSubtasks(subtask2, epic);
        manager.addSubtasks(subtask3, epic);
        Assertions.assertEquals(Status.NEW, manager.getEpicById(1).getStatus());
    }

    @Test
    public void getEpicStatusWithSubtasksInStatusNew() {
        manager.addEpic(epic);
        manager.addSubtasks(subtaskWithStatusNew1, epic);
        manager.addSubtasks(subtaskWithStatusNew2, epic);
        manager.addSubtasks(subtaskWithStatusNew3, epic);
        Assertions.assertEquals(Status.NEW, manager.getEpicById(1).getStatus());
    }

    @Test
    public void getEpicStatusWithSubtasksInStatusDone() {
        manager.addEpic(epic);
        manager.addSubtasks(subtaskWithStatusDone1, epic);
        manager.addSubtasks(subtaskWithStatusDone2, epic);
        manager.addSubtasks(subtaskWithStatusDone3, epic);
        System.out.println(manager.getEpicById(1));
        Assertions.assertEquals(Status.NEW, manager.getEpicById(1).getStatus());

    }
}
