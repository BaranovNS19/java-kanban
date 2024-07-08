public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("Сходить в магазин", "Купить продукты", Status.IN_PROGRESS);
        Task task2 = new Task("Уборка", "Помыть полы", Status.NEW);
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Отпуск", "Море", Status.NEW);
        Epic epic2 = new Epic("Работа", "Расписание", Status.NEW);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        Subtask subtask1 = new Subtask("Одежда", "Купить плавки", Status.NEW, epic1.getId());
        taskManager.addSubtasks(subtask1);
        epic1.addSubtaskId(subtask1.getId());

        Subtask subtask2 = new Subtask("Билеты", "бронь", Status.NEW, epic1.getId());
        taskManager.addSubtasks(subtask2);
        epic1.addSubtaskId(subtask2.getId());

        Subtask subtask3 = new Subtask("График", "Календарь", Status.NEW, epic2.getId());
        taskManager.addSubtasks(subtask3);
        epic2.addSubtaskId(subtask3.getId());

        System.out.println("Задачи: " + taskManager.getTasks());
        System.out.println("Эпики: " + taskManager.getEpics());
        System.out.println("Подзадачи: " + taskManager.getSubtasks());

        taskManager.updateTask(1, new Task("Сходить в магазин", "Купить продукты", Status.DONE));
        taskManager.updateEpic(3, new Epic("Отпуск", "Море", Status.DONE));
        taskManager.updateSubtask(7, new Subtask("График", "Календарь", Status.NEW, epic2.getId()));

        System.out.println("Задачи: " + taskManager.getTasks());
        System.out.println("Эпики: " + taskManager.getEpics());
        System.out.println("Подзадачи: " + taskManager.getSubtasks());

        taskManager.removeTaskById(1);
        taskManager.removeEpicById(3);

        System.out.println("Задачи: " + taskManager.getTasks());
        System.out.println("Эпики: " + taskManager.getEpics());
        System.out.println("Подзадачи: " + taskManager.getSubtasks());


    }


}
