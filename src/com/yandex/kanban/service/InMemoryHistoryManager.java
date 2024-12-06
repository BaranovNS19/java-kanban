package com.yandex.kanban.service;

import com.yandex.kanban.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private static CustomLinkedList customHistory;

    public InMemoryHistoryManager() {
        customHistory = new CustomLinkedList();
    }

    @Override
    public void addTaskInHistory(Task task) {
        customHistory.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return customHistory.getAllTasks();
    }

    @Override
    public void remove(int id) {
        customHistory.remove(id);
    }

    @Override
    public String toString() {
        return "История просмотра: " + customHistory;
    }

    static class CustomLinkedList {
        public Node head;
        public Node tail;
        private int size = 0;
        private final HashMap<Integer, Node> customMap;


        public CustomLinkedList() {
            this.head = null;
            this.tail = null;
            this.customMap = new HashMap<>();
        }

        public void add(Task task) {
            if (customMap.containsKey(task.getId())) {
                remove(task);
                size--;
            }
            Node newNode = new Node(task);

            if (tail == null) {
                head = newNode;
                tail = newNode;
            } else {
                tail.next = newNode;
                newNode.prev = tail;
                tail = newNode;
            }
            customMap.put(task.getId(), newNode);
            size++;
        }

        public int size() {
            return size;
        }

        public void remove(Task task) {
            if (!customMap.containsKey(task.getId())) {
                return;
            }

            Node nodeToRemove = customMap.get(task.getId());

            if (nodeToRemove.prev != null) {
                nodeToRemove.prev.next = nodeToRemove.next;
            } else {
                head = nodeToRemove.next;
            }

            if (nodeToRemove.next != null) {
                nodeToRemove.next.prev = nodeToRemove.prev;
            } else {
                tail = nodeToRemove.prev;
            }

            customMap.remove(task.getId());
        }

        public void remove(int id) {
            if (!customMap.containsKey(id)) {
                return;
            }

            Node nodeToRemove = customMap.get(id);

            if (nodeToRemove.prev != null) {
                nodeToRemove.prev.next = nodeToRemove.next;
            } else {
                head = nodeToRemove.next;
            }

            if (nodeToRemove.next != null) {
                nodeToRemove.next.prev = nodeToRemove.prev;
            } else {
                tail = nodeToRemove.prev;
            }

            customMap.remove(id);
        }

        public ArrayList<Task> getAllTasks() {
            ArrayList<Task> tasks = new ArrayList<>();
            Node current = head;

            while (current != null) {
                tasks.add(current.data);
                current = current.next;
            }
            return tasks;
        }

        public boolean isEmpty() {
            if (size != 0) {
                return false;
            }
            return true;
        }
    }
}
