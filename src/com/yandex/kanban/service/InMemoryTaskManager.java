package com.yandex.kanban.service;

import com.yandex.kanban.model.Epic;
import com.yandex.kanban.model.Status;
import com.yandex.kanban.model.Subtask;
import com.yandex.kanban.model.Task;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private int generateId;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final InMemoryHistoryManager inMemoryHistoryManager;
    private final TreeSet<Task> allTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    private final Calendar calendar;

    public InMemoryTaskManager() {
        inMemoryHistoryManager = new InMemoryHistoryManager();
        calendar = new Calendar();
    }

    public InMemoryHistoryManager getInMemoryHistoryManager() {
        return inMemoryHistoryManager;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    @Override
    public int addTask(Task task) {
        if (Check.checkTask(tasks, task)) {
            final int id = ++generateId;
            task.setId(id);
            if (task.getStartTime() == null) {
                tasks.put(id, task);
            }
            if (task.getStartTime() != null && Check.checkStartTimeIntersection(calendar, task)) {
                tasks.put(id, task);
                allTasks.add(task);
            }
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
        epic.setStatus(Check.checkStatus(statuses));

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
        if (Check.checkSubtask(subtasks, subtask) && Check.checkStartTimeIntersection(calendar, subtask)) {
            final int id = ++generateId;
            subtask.setId(id);
            subtask.setStatus(epic.getStatus());
            subtask.setEpicId(epic.getId());
            epic.addSubtaskId(subtask.getId());
            if (epic.getStartTime() != null && Check.checkStartTime(subtask.getStartTime())) {
                epic.setStartTime(epic.getStartTimeForEpic());
                epic.addSubtaskIdAndStartTime(subtask.getId(), subtask.getStartTime());
            }
            subtasks.put(id, subtask);
            addStatusEpic(epic);
            if (epic.getStartTime() != null && Check.checkStartTime(subtask.getStartTime())) {
                epic.setEndTime(epic.getStartTime().plus(getDurationForEpic(epic)));
            }
            if (subtask.getStartTime() != null) {
                allTasks.add(subtask);
            }
            return id;

        }
        return 0;
    }

    @Override
    public boolean removeAllTasks() {
        for (Task t : tasks.values()) {
            if (t.getStartTime() != null && t.getEndTime() != null) {
                calendar.updateCalendar(t);
            }
        }
        tasks.clear();
        allTasks.removeIf(t -> !(t instanceof Epic) && !(t instanceof Subtask));
        return true;
    }

    @Override
    public boolean removeAllEpics() {
        epics.clear();
        subtasks.clear();
        allTasks.removeIf(t -> (t instanceof Epic) || (t instanceof Subtask));
        return true;
    }

    @Override
    public boolean removeAllSubtasks() {
        for (Subtask s : subtasks.values()) {
            if (s.getStartTime() != null && s.getEndTime() != null) {
                calendar.updateCalendar(s);
            }
        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtasksId();
            addStatusEpic(epic);
        }
        allTasks.removeIf(t -> (t instanceof Subtask));
        return true;
    }

    @Override
    public boolean removeTaskById(int id) {
        if (Check.checkId(tasks, id)) {
            if (tasks.get(id).getStartTime() != null && tasks.get(id).getEndTime() != null) {
                calendar.updateCalendar(tasks.get(id));
                allTasks.remove(getTaskById(id));
            }
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
                calendar.updateCalendar(subtasks.get(id));
                allTasks.removeIf(t -> (t instanceof Subtask) && t.getId() == idSubtask);
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
            if (subtasks.get(id).getStartTime() != null && subtasks.get(id).getEndTime() != null) {
                calendar.updateCalendar(subtasks.get(id));
            }
            int epicId = subtasks.get(id).getEpicId();
            Epic epic = epics.get(epicId);
            for (int i = 0; i < epic.getSubtasksId().size(); i++) {
                if (epic.getSubtasksId().get(i) == id) {
                    epic.removeSubtaskId(i);
                }
            }
            allTasks.removeIf(t -> (t instanceof Subtask) && t.getId() == id);
            subtasks.remove(id);
            addStatusEpic(epic);
            inMemoryHistoryManager.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public Task getTaskById(int id) {
        if (tasks.containsKey(id)) {
            inMemoryHistoryManager.add(tasks.get(id));
            return tasks.get(id);
        }
        return null;
    }

    @Override
    public Epic getEpicById(int id) {
        if (epics.containsKey(id)) {
            inMemoryHistoryManager.add(epics.get(id));
            return epics.get(id);
        }
        return null;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            inMemoryHistoryManager.add(subtasks.get(id));
            return subtasks.get(id);
        }
        return null;
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
        if (Check.checkId(tasks, id) && Check.checkStartTimeIntersection(calendar, task)) {
            task.setId(id);
            tasks.put(id, task);
            allTasks.removeIf(t -> (t.getId() == id));
            if (task.getStartTime() != null && task.getDuration() != null) {
                allTasks.add(task);
            }
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
        if (Check.checkIdSubtasks(subtasks, id) && Check.checkStartTimeIntersection(calendar, subtask)) {
            subtask.setId(id);
            addStatusEpic(epic);
            subtasks.put(id, subtask);
            allTasks.removeIf(t -> (t instanceof Subtask) && t.getId() == id);
            if (subtask.getStartTime() != null && subtask.getDuration() != null) {
                allTasks.add(subtask);
            }
            return true;
        }
        return false;
    }

    @Override
    public ArrayList<Subtask> getSubtaskByEpic(Epic epic) {
        List<Subtask> subtaskName = epic.getSubtasksId().stream()
                .map(subtasks::get)
                .collect(Collectors.toList());
        System.out.println("Получили список подзадач: " + subtaskName);
        return (ArrayList<Subtask>) subtaskName;
    }

    public Duration getDurationForEpic(Epic epic) {
        return getSubtaskByEpic(epic).stream()
                .map(Subtask::getDuration)
                .reduce(Duration.ZERO, Duration::plus);
    }

    //фильтрация реализована в методах add и update
    public TreeSet<Task> getPrioritizedTasks() {
        return allTasks;
    }

}
