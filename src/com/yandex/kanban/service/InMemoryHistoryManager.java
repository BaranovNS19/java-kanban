package com.yandex.kanban.service;

import com.yandex.kanban.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private static CustomLinkedList historyMap;

    public InMemoryHistoryManager() {
        historyMap = new CustomLinkedList();
    }

    @Override
    public void add(Task task) {
        historyMap.linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return historyMap.getTasks();
    }

    @Override
    public void remove(int id) {
        historyMap.remove(id);
    }

    @Override
    public String toString() {
        return "История просмотра: " + historyMap;
    }

    static class CustomLinkedList {
        public Node head;
        public Node tail;
        private int historySize = 0;
        private final HashMap<Integer, Node> customMap;


        public CustomLinkedList() {
            this.head = null;
            this.tail = null;
            this.customMap = new HashMap<>();
        }

        public void linkLast(Task task) {
            if (customMap.containsKey(task.getId())) {
                remove(task);
                historySize--;
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
            historySize++;
        }

        public int getHistorySize() {
            return historySize;
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

        public ArrayList<Task> getTasks() {
            ArrayList<Task> tasks = new ArrayList<>();
            Node current = head;

            while (current != null) {
                tasks.add(current.data);
                current = current.next;
            }
            return tasks;
        }

        public boolean isEmpty() {
            if (historySize != 0) {
                return false;
            }
            return true;
        }
    }
}
