package com.yandex.kanban.service;

import com.yandex.kanban.model.Task;

public class Node {
    public Task data;
    public Node next;
    public Node prev;

    public Node(Task data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }
}
