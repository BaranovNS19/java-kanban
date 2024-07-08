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
        if (statuses.contains(Status.NEW) && (!statuses.contains(Status.DONE))
                && (!statuses.contains(Status.IN_PROGRESS))) {
            epic.setStatus(Status.NEW);
        } else if (statuses.isEmpty()) {
            epic.setStatus(Status.NEW);
        } else if (statuses.contains(Status.DONE) && (!statuses.contains(Status.IN_PROGRESS))
                && (!statuses.contains(Status.NEW))) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }

    }

    public ArrayList<Status> getAllStatus() {
        ArrayList<Status> statuses = new ArrayList<>();
        for (Subtask s : subtasks.values()) {
            statuses.add(s.getStatus());
        }
        return statuses;
    }

    public int addSubtasks(Subtask subtask) {
        if (Check.checkSubtask(subtasks, subtask)) {
            final int id = ++generateId;
            subtask.setId(id);
            subtasks.put(id, subtask);
            return id;
        }
        return 0;
    }

    public void removeAllTasks() {
        tasks.clear();
        System.out.println("������ ����� ����!");
    }

    public void removeAllEpics() {
        epics.clear();
        subtasks.clear();
        System.out.println("����� � ��������� �������!");
    }

    public void removeAllSubtasks() {
        subtasks.clear();
        System.out.println("��������� �������!");
    }

    public void removeTaskById(int id) {
        if (Check.checkId(tasks, id)) {
            tasks.remove(id);
            System.out.println("������ � ��������������� " + id + " �������!");
        } else {
            System.out.println("������ � ��������������� " + id + " �����������!");
        }
    }

    public void removeEpicById(int id) {
        if (Check.checkIdEpics(epics, id)) {
            epics.remove(id);
            System.out.println("���� � ��������������� " + id + " ������!");
        } else {
            System.out.println("���� � ��������������� " + id + " �����������!");
        }
    }

    public void removeSubtasksById(int id) {
        if (Check.checkIdSubtasks(subtasks, id)) {
            subtasks.remove(id);
            System.out.println("��������� � ��������������� " + id + " �������!");
        } else {
            System.out.println("��������� � ��������������� " + id + " �����������!");
        }
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

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public void updateTask(int id, Task task) {
        if (Check.checkId(tasks, id)) {
            task.setId(id);
            tasks.put(id, task);
            System.out.println("������ � ��������������� " + id + " ���������!");
        } else {
            System.out.println("������ � ��������������� " + id + " �����������!");
        }
    }

    public void updateEpic(int id, Epic epic) {
        if (Check.checkIdEpics(epics, id)) {
            epic.setId(id);
            addStatusEpic(epic);
            epics.put(id, epic);
            System.out.println("���� � ��������������� " + id + " ��������!");
        } else {
            System.out.println("���� � ��������������� " + id + " �����������!");
        }
    }

    public void updateSubtask(int id, Subtask subtask) {
        if (Check.checkIdSubtasks(subtasks, id)) {
            subtask.setId(id);
            subtasks.put(id, subtask);
            System.out.println("��������� � ��������������� " + id + " ���������!");
        } else {
            System.out.println("��������� � ��������������� " + id + " �����������!");
        }
    }

    public ArrayList<Subtask> getSubtaskByEpic(Epic epic) {
        ArrayList<Subtask> subtaskName = new ArrayList<>();
        if (epic.subtasksId.isEmpty()) {
            System.out.println("� ������� ����� ����������� ���������");
        } else {
            for (int id : epic.subtasksId) {
                subtaskName.add(subtasks.get(id));
                System.out.println("�������� ������ ��������: " + subtaskName);
            }
        }
        return subtaskName;
    }

}
