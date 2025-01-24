package com.yandex.kanban.service;

import com.yandex.kanban.model.*;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(File file) {
        super();
        this.file = file;
    }

    @Override
    public void addStatusEpic(Epic epic) {
        super.addStatusEpic(epic);
        save();
    }

    @Override
    public InMemoryHistoryManager getInMemoryHistoryManager() {
        return super.getInMemoryHistoryManager();
    }

    @Override
    public int addTask(Task task) {
        int result = super.addTask(task);
        save();
        return result;
    }

    @Override
    public int addEpic(Epic epic) {
        int result = super.addEpic(epic);
        save();
        return result;
    }

    @Override
    public ArrayList<Status> getAllStatus() {
        return super.getAllStatus();
    }

    @Override
    public int addSubtasks(Subtask subtask, Epic epic) {
        int result = super.addSubtasks(subtask, epic);
        save();
        return result;
    }

    @Override
    public boolean removeAllTasks() {
        boolean result = super.removeAllTasks();
        save();
        return result;
    }

    @Override
    public boolean removeAllEpics() {
        boolean result = super.removeAllEpics();
        save();
        return result;
    }

    @Override
    public boolean removeAllSubtasks() {
        boolean result = super.removeAllSubtasks();
        save();
        return result;
    }

    @Override
    public boolean removeTaskById(int id) {
        boolean result = super.removeTaskById(id);
        save();
        return result;
    }

    @Override
    public boolean removeEpicById(int id) {
        boolean result = super.removeEpicById(id);
        save();
        return result;
    }

    @Override
    public boolean removeSubtasksById(int id) {
        boolean result = super.removeSubtasksById(id);
        save();
        return result;
    }

    @Override
    public Task getTaskById(int id) {
        return super.getTaskById(id);
    }

    @Override
    public Epic getEpicById(int id) {
        return super.getEpicById(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        return super.getSubtaskById(id);
    }

    @Override
    public List<Task> getTasks() {
        return super.getTasks();
    }

    @Override
    public List<Epic> getEpics() {
        return super.getEpics();
    }

    @Override
    public List<Subtask> getSubtasks() {
        return super.getSubtasks();
    }

    @Override
    public boolean updateTask(int id, Task task) {
        boolean result = super.updateTask(id, task);
        save();
        return result;
    }

    @Override
    public boolean updateEpic(int id, Epic epic) {
        boolean result = super.updateEpic(id, epic);
        save();
        return result;
    }

    @Override
    public boolean updateSubtask(int id, Subtask subtask, Epic epic) {
        boolean result = super.updateSubtask(id, subtask, epic);
        save();
        return result;
    }

    @Override
    public ArrayList<Subtask> getSubtaskByEpic(Epic epic) {
        return super.getSubtaskByEpic(epic);
    }

    @Override
    public String toString() {
        return super.toString();
    }


    public void save() {
        try (FileWriter fileWriter = new FileWriter(file)) {
            for (Task task : getTasks()) {
                fileWriter.write(toStringTask(task));
            }
            for (Epic task : getEpics()) {
                fileWriter.write(toStringTask(task));
            }
            for (Subtask task : getSubtasks()) {
                fileWriter.write(toStringTask(task));
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении данных", e);
        }
    }

    public static Task fromString(String value) {
        String[] arr = value.split(",");
        int id = Integer.parseInt(arr[0]);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        if (arr[1].equals(Type.TASK.toString())) {
            if (arr[3].equals("null") && arr[6].equals("null")) {
                Task task = new Task(arr[2], arr[4]);
                task.setId(id);
                return task;
            } else if (!arr[6].equals("null") && arr[3].equals("null")) {
                Task task = new Task(arr[2], arr[4], Duration.parse(arr[7]), LocalDateTime.parse(arr[6], formatter));
                task.setId(id);
                return task;
            } else if (arr[6].equals("null")) {
                Task task = new Task(arr[2], arr[4], Status.valueOf(arr[3]));
                task.setId(id);
                return task;
            } else {
                Task task = new Task(arr[2], arr[4], Status.valueOf(arr[3]), Duration.parse(arr[7]),
                        LocalDateTime.parse(arr[6], formatter));
                task.setId(id);
                return task;
            }

        } else if (arr[1].equals(Type.SUBTASK.toString())) {
            if (arr[3].equals("null") && arr[6].equals("null")) {
                Subtask subtask = new Subtask(arr[2], arr[4], Integer.parseInt(arr[5]));
                subtask.setId(id);
                return subtask;
            } else if (!arr[6].equals("null") && arr[3].equals("null")) {
                Subtask subtask = new Subtask(arr[2], arr[4], Duration.parse(arr[7]),
                        LocalDateTime.parse(arr[6], formatter), Integer.parseInt(arr[5]));
                subtask.setId(id);
                return subtask;
            } else if (arr[6].equals("null")) {
                Subtask subtask = new Subtask(arr[2], arr[4], Status.valueOf(arr[3]), Integer.parseInt(arr[5]));
                subtask.setId(id);
                return subtask;
            } else {
                Subtask subtask = new Subtask(arr[2], arr[4], Status.valueOf(arr[3]), Duration.parse(arr[7]),
                        LocalDateTime.parse(arr[6], formatter), Integer.parseInt(arr[5]));
                subtask.setId(id);
                return subtask;
            }
        } else {
            Epic epic = new Epic(arr[2], arr[4]);
            epic.setId(id);
            return epic;
        }

    }

    public String toStringTask(Task task) {
        String type;
        if (task instanceof Epic) {
            type = Type.EPIC.toString();
        } else if (task instanceof Subtask) {
            type = Type.SUBTASK.toString();
        } else {
            type = Type.TASK.toString();
        }
        return String.format("%d,%s,%s,%s,%s,%s,%s,%s%n", task.getId(), type, task.getName(), task.getStatus(),
                task.getDescription(), task instanceof Subtask ? ((Subtask) task).getEpicId() : "",
                task.getStartTime(), task.getDuration());
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        try (FileReader fileReader = new FileReader(file)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                if (!line.isEmpty()) {
                    String[] arr = line.split(",");
                    if (arr[1].equals(Type.TASK.toString())) {
                        fileBackedTaskManager.addTask(fromString(line));
                    } else if (arr[1].equals(Type.EPIC.toString())) {
                        if (fromString(line) instanceof Epic) {
                            fileBackedTaskManager.addEpic((Epic) fromString(line));
                        }
                    } else if (arr[1].equals(Type.SUBTASK.toString())) {
                        if (fromString(line) instanceof Subtask subtask) {
                            Epic epic = fileBackedTaskManager.getEpicById(subtask.getEpicId());
                            epic.addSubtaskId(subtask.getId());
                            fileBackedTaskManager.addSubtasks(subtask, epic);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении данных", e);
        }
        return fileBackedTaskManager;
    }
}
