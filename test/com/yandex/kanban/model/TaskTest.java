package com.yandex.kanban.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class TaskTest {
    @Test
    void checkingObjectEqualityByIdAndName() {
        Task task1 = new Task("testName", "testDescription");
        task1.setId(1);
        Task task2 = new Task("testName", "testDescription1234");
        task2.setId(1);
        Assertions.assertEquals(task1, task2, "Объекты не равны!");
    }

}