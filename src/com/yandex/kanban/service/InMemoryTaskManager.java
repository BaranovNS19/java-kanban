package com.yandex.kanban.service;

import com.yandex.kanban.model.Epic;
import com.yandex.kanban.model.Status;
import com.yandex.kanban.model.Subtask;
import com.yandex.kanban.model.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {//, HistoryManager
    private int generateId;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final InMemoryHistoryManager inMemoryHistoryManager;

    public InMemoryTaskManager() {
        inMemoryHistoryManager = new InMemoryHistoryManager();
    }

    public InMemoryHistoryManager getInMemoryHistoryManager() {
        return inMemoryHistoryManager;
    }

    @Override
    public int addTask(Task task) {
        if (Check.checkTask(tasks, task)) {
            final int id = ++generateId;
            task.setId(id);
            tasks.put(id, task);
            return id;
        }
        return 0;
    }

    @Override
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

    @Override
    public void addStatusEpic(Epic epic) {
        Set<Status> statuses = new HashSet<>(getAllStatus());
        epic.setStatus(Check.CheckStatus(statuses));

    }

    @Override
    public ArrayList<Status> getAllStatus() {
        ArrayList<Status> statuses = new ArrayList<>();
        for (Subtask s : subtasks.values()) {
            statuses.add(s.getStatus());
        }
        return statuses;
    }

    @Override
    public int addSubtasks(Subtask subtask, Epic epic) {
        if (Check.checkSubtask(subtasks, subtask)) {
            final int id = ++generateId;
            subtask.setId(id);
            subtask.setStatus(epic.getStatus());
            subtask.setEpicId(epic.getId());
            epic.addSubtaskId(subtask.getId());
            subtasks.put(id, subtask);
            addStatusEpic(epic);
            return id;
        }
        return 0;
    }

    @Override
    public boolean removeAllTasks() {
        tasks.clear();
        return true;
    }

    @Override
    public boolean removeAllEpics() {
        epics.clear();
        subtasks.clear();
        return true;
    }

    @Override
    public boolean removeAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtasksId();
            addStatusEpic(epic);
        }
        return true;
    }

    @Override
    public boolean removeTaskById(int id) {
        if (Check.checkId(tasks, id)) {
            tasks.remove(id);
            inMemoryHistoryManager.remove(id);
            return true;
        }
        return false;
    }

    @Override
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
                inMemoryHistoryManager.remove(idSubtask);
            }
            epics.remove(id);
            inMemoryHistoryManager.remove(id);
            return true;
        }
        return false;
    }

    @Override
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
            inMemoryHistoryManager.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public Task getTaskById(int id) {
        inMemoryHistoryManager.addTaskInHistory(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        inMemoryHistoryManager.addTaskInHistory(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        inMemoryHistoryManager.addTaskInHistory(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public boolean updateTask(int id, Task task) {
        if (Check.checkId(tasks, id)) {
            task.setId(id);
            tasks.put(id, task);
            return true;
        }
        return false;
    }

    @Override
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

    @Override
    public boolean updateSubtask(int id, Subtask subtask, Epic epic) {
        if (Check.checkIdSubtasks(subtasks, id)) {
            subtask.setId(id);
            addStatusEpic(epic);
            subtasks.put(id, subtask);
            return true;
        }
        return false;
    }

    @Override
    public ArrayList<Subtask> getSubtaskByEpic(Epic epic) {
        ArrayList<Subtask> subtaskName = new ArrayList<>();
        for (int id : epic.getSubtasksId()) {
            subtaskName.add(subtasks.get(id));
            System.out.println("Получили список подзадач: " + subtaskName);
        }
        return subtaskName;
    }
}
