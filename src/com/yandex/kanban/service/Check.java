package com.yandex.kanban.service;

import com.yandex.kanban.model.Epic;
import com.yandex.kanban.model.Status;
import com.yandex.kanban.model.Subtask;
import com.yandex.kanban.model.Task;

import java.time.LocalDateTime;
import java.util.*;

public class Check {
    private Check() {

    }

    public static boolean checkTask(HashMap<Integer, Task> tasks, Task task) {
        for (Task t : tasks.values()) {
            if (task.equals(t)) {
                System.out.println("Задача с таким названием уже существует!");
                return false;
            }
        }
        return true;
    }

    public static boolean checkEpic(HashMap<Integer, Epic> epics, Epic epic) {
        for (Epic e : epics.values()) {
            if (epic.equals(e)) {
                System.out.println("Эпик с таким названием уже существует!");
                return false;
            }
        }
        return true;
    }

    public static boolean checkSubtask(HashMap<Integer, Subtask> subtasks, Subtask subtask) {
        for (Subtask s : subtasks.values()) {
            if (subtask.equals(s)) {
                System.out.println("Подзадача с таким названием уже существует!");
                return false;
            }
        }
        return true;
    }

    public static boolean checkId(HashMap<Integer, Task> tasks, int id) {
        for (int t : tasks.keySet()) {
            if (id == t) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkIdEpics(HashMap<Integer, Epic> epics, int id) {
        for (int e : epics.keySet()) {
            if (id == e) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkIdSubtasks(HashMap<Integer, Subtask> subtasks, int id) {
        for (int s : subtasks.keySet()) {
            if (id == s) {
                return true;
            }
        }
        return false;
    }

    public static Status checkStatus(Set<Status> statuses) {
        if (statuses.contains(Status.NEW) && (!statuses.contains(Status.DONE))
                && (!statuses.contains(Status.IN_PROGRESS))) {
            return Status.NEW;
        } else if (statuses.isEmpty()) {
            return Status.NEW;
        } else if (statuses.contains(Status.DONE) && (!statuses.contains(Status.IN_PROGRESS))
                && (!statuses.contains(Status.NEW))) {
            return Status.DONE;
        }
        return Status.IN_PROGRESS;

    }

    public static boolean checkStartTime(LocalDateTime localDateTime) {
        boolean result = localDateTime.isAfter(LocalDateTime.now());
        if (!result) {
            System.out.println("Дата начала задачи не может быть в прошлом времени " + localDateTime);
        }
        return result;
    }

    /*public static boolean checkStartTimeIntersection(TreeSet<Task> treeSet, Task task) {
        if (task.getStartTime() != null && task.getEndTime() != null) {
            boolean hasIntersection = treeSet.stream()
                    .anyMatch(t -> task.getStartTime().isBefore(t.getEndTime()) && task.getEndTime().isAfter(t.getStartTime()));

            if (hasIntersection) {
                System.out.println("Задачи пересекаются по времени!");
            }
            return !hasIntersection;
        } else {
            return true;
        }
    }*/



    public static boolean checkStartTimeIntersection(Calendar calendar, Task task) {
        if (task.getStartTime() != null && task.getEndTime() != null) {
            if (checkCalendarBoundaries(calendar, task)) {
                for (TimeInterval t : calendar.getCalendar().keySet()) {
                    if (t.intersect(new TimeInterval(task.getStartTime(), task.getEndTime()))) {
                        if (calendar.getCalendar().get(t)) {
                            calendar.getCalendar().put(t, false);
                        } else {
                            System.out.println("Задачи пересекаются по времени!");
                            return false;
                        }
                    }

                }
            } else {
                System.out.println("Планирование возможно с " + calendar.getStartCalendar() + " по "
                        + calendar.getEndCalendar() + " интервал задачи с " + task.getStartTime() + " по "
                        + task.getEndTime());
                return false;
            }
            return true;
        }
        return true;
    }


    private static boolean checkCalendarBoundaries(Calendar calendar, Task task) {
        return task.getStartTime().isEqual(calendar.getStartCalendar())
                || task.getStartTime().isAfter(calendar.getEndCalendar())
                && task.getEndTime().isEqual(calendar.getEndCalendar())
                || task.getEndTime().isBefore(calendar.getEndCalendar());
    }

}
