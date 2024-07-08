import java.util.ArrayList;

public class Epic extends Task {

    public ArrayList<Integer> subtasksId;

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        subtasksId = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Epic{name=" + name + ", description=" + description + ", id=" + id + ", status=" + status + "," +
                " subtasksId=" + subtasksId + "}";
    }

    public ArrayList<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void addSubtaskId(int id) {
        subtasksId.add(id);
    }
}
