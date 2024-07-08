package com.yandex.kanban.service;

import com.yandex.kanban.model.Epic;
import com.yandex.kanban.model.Status;
import com.yandex.kanban.model.Subtask;
import com.yandex.kanban.model.Task;

import java.util.*;

public class TaskManager {
    private int generateId;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public int addTask(Task task) {
        if (Check.checkTask(tasks, task)) {
            final int id = ++generateId;
            task.setId(id);
            tasks.put(id, task);
            return id;
        }
        return 0;
    }

    public int addEpic(Epic epic) {
        if (Check.checkEpic(epics, epic)) {
            final int id = ++generateId;
            epic.setId(id);
            addStatusEpic(epic);
            epics.put(id, epic);
            return id;
        }
        return 0;
    }

    public void addStatusEpic(Epic epic) {
        Set<Status> statuses = new HashSet<>(getAllStatus());
        epic.setStatus(Check.CheckStatus(statuses));

    }

    public ArrayList<Status> getAllStatus() {
        ArrayList<Status> statuses = new ArrayList<>();
        for (Subtask s : subtasks.values()) {
            statuses.add(s.getStatus());
        }
        return statuses;
    }

    public int addSubtasks(Subtask subtask, Epic epic) {
        if (Check.checkSubtask(subtasks, subtask)) {
            final int id = ++generateId;
            subtask.setId(id);
            subtask.setStatus(epic.getStatus());
            epic.addSubtaskId(subtask.getId());
            subtasks.put(id, subtask);
            return id;
        }
        return 0;
    }

    public boolean removeAllTasks() {
        tasks.clear();
        return true;
    }

    public boolean removeAllEpics() {
        epics.clear();
        subtasks.clear();
        return true;
    }

    public boolean removeAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtasksId();
            addStatusEpic(epic);
        }
        return true;
    }

    public boolean removeTaskById(int id) {
        if (Check.checkId(tasks, id)) {
            tasks.remove(id);
            return true;
        }
        return false;
    }

    public boolean removeEpicById(int id) {
        if (Check.checkIdEpics(epics, id)) {
            epics.get(id).clearSubtasksId();
            ArrayList<Integer> subtaskByEpic = new ArrayList<>();
            for (int subtaskId : subtasks.keySet()) {
                if (id == subtasks.get(subtaskId).getEpicId()) {
                    subtaskByEpic.add(subtaskId);
                }
            }
            for (int idSubtask : subtaskByEpic) {
                subtasks.remove(idSubtask);
            }
            epics.remove(id);
            return true;
        }
        return false;
    }

    public boolean removeSubtasksById(int id) {
        if (Check.checkIdSubtasks(subtasks, id)) {
            int epicId = subtasks.get(id).getEpicId();
            Epic epic = epics.get(epicId);
            for (int i = 0; i < epic.getSubtasksId().size(); i++) {
                if (epic.getSubtasksId().get(i) == id) {
                    epic.removeSubtaskId(i);
                }
            }
            subtasks.remove(id);
            addStatusEpic(epic);
            return true;
        }
        return false;
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public List<Task> getTasks() {
        List<Task> task = new ArrayList<>();
        for (Task t : tasks.values()) {
            task.add(t);
        }
        return task;
    }

    public List<Epic> getEpics() {
        List<Epic> epic = new ArrayList<>();
        for (Epic e : epics.values()) {
            epic.add(e);
        }
        return epic;
    }

    public List<Subtask> getSubtasks() {
        List<Subtask> subtask = new ArrayList<>();
        for (Subtask s : subtasks.values()){
            subtask.add(s);
        }
        return subtask;
    }

    public boolean updateTask(int id, Task task) {
        if (Check.checkId(tasks, id)) {
            task.setId(id);
            tasks.put(id, task);
            return true;
        }
        return false;
    }

    public boolean updateEpic(int id, Epic epic) {
        if (Check.checkIdEpics(epics, id)) {
            epic.setId(id);
            addStatusEpic(epic);
            epic.setSubtasksId(epics.get(id).getSubtasksId());
            epics.put(id, epic);
            return true;
        }
        return false;
    }

    public boolean updateSubtask(int id, Subtask subtask, Epic epic) {
        if (Check.checkIdSubtasks(subtasks, id)) {
            subtask.setId(id);
            addStatusEpic(epic);
            subtasks.put(id, subtask);
            return true;
        }
        return false;
    }

    public ArrayList<Subtask> getSubtaskByEpic(Epic epic) {
        ArrayList<Subtask> subtaskName = new ArrayList<>();
        for (int id : epic.getSubtasksId()) {
            subtaskName.add(subtasks.get(id));
            System.out.println("Получили список подзадач: " + subtaskName);
        }
        return subtaskName;
    }

}
