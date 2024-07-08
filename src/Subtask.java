public class Subtask extends Task {

    protected int epicId;

    public Subtask(String name, String description, Status status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{name=" + name + ", description=" + description + ", id=" + id + ", status=" + status + ", " +
                "epicId=" + epicId + "}";
    }

}
