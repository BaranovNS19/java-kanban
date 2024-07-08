public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("������� � �������", "������ ��������", Status.IN_PROGRESS);
        Task task2 = new Task("������", "������ ����", Status.NEW);
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        Epic epic1 = new Epic("������", "����", Status.NEW);
        Epic epic2 = new Epic("������", "����������", Status.NEW);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        Subtask subtask1 = new Subtask("������", "������ ������", Status.NEW, epic1.getId());
        taskManager.addSubtasks(subtask1);
        epic1.addSubtaskId(subtask1.getId());

        Subtask subtask2 = new Subtask("������", "�����", Status.NEW, epic1.getId());
        taskManager.addSubtasks(subtask2);
        epic1.addSubtaskId(subtask2.getId());

        Subtask subtask3 = new Subtask("������", "���������", Status.NEW, epic2.getId());
        taskManager.addSubtasks(subtask3);
        epic2.addSubtaskId(subtask3.getId());

        System.out.println("������: " + taskManager.getTasks());
        System.out.println("�����: " + taskManager.getEpics());
        System.out.println("���������: " + taskManager.getSubtasks());

        taskManager.updateTask(1, new Task("������� � �������", "������ ��������", Status.DONE));
        taskManager.updateEpic(3, new Epic("������", "����", Status.DONE));
        taskManager.updateSubtask(7, new Subtask("������", "���������", Status.NEW, epic2.getId()));

        System.out.println("������: " + taskManager.getTasks());
        System.out.println("�����: " + taskManager.getEpics());
        System.out.println("���������: " + taskManager.getSubtasks());

        taskManager.removeTaskById(1);
        taskManager.removeEpicById(3);

        System.out.println("������: " + taskManager.getTasks());
        System.out.println("�����: " + taskManager.getEpics());
        System.out.println("���������: " + taskManager.getSubtasks());


    }


}
