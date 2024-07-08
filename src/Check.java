import java.util.HashMap;

public class Check {
    private Check() {

    }

    public static boolean checkTask(HashMap<Integer, Task> tasks, Task task) {
        for (Task t : tasks.values()) {
            if (task.equals(t)) {
                System.out.println("«адача с таким названием уже существует!");
                return false;
            }
        }
        return true;
    }

    public static boolean checkEpic(HashMap<Integer, Epic> epics, Epic epic) {
        for (Epic e : epics.values()) {
            if (epic.equals(e)) {
                System.out.println("Ёпик с таким названием уже существует!");
                return false;
            }
        }
        return true;
    }

    public static boolean checkSubtask(HashMap<Integer, Subtask> subtasks, Subtask subtask) {
        for (Subtask s : subtasks.values()) {
            if (subtask.equals(s)) {
                System.out.println("ѕодзадача с таким названием уже существует!");
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

}
