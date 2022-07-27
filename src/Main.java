public class Main {

    public static void main(String[] args) {
        TrackerManager trackerManager = new TrackerManager();
        Task task1 = new Task("Задача 1", "Помыть посуду", "NEW", 0);
        Task task2 = new Task("Задача 2", "Приготовить ужин", "NEW", 0);
        Epic epic1 = new Epic("Эпик1", "Купить новую квартиру", "NEW");
        Epic epic2 = new Epic("Эпик2", "Организовать переезд", "NEW");
        Subtask subtask1e1 = new Subtask("Подзадача 1 Эпик 1", "Найти подходящую квартиру",
                "NEW", 0, 3);
        Subtask subtask2e1 = new Subtask("Подзадача 2 Эпик 1", "Проверить документы",
                "NEW", 0, 3);
        Subtask subtask1e2 = new Subtask("Подзадача 1 Эпик 2", "Найти транспорт",
                "NEW", 0, 4);

        trackerManager.createTask(task1);
        trackerManager.createTask(task2);
        trackerManager.createEpic(epic1);
        trackerManager.createEpic(epic2);
        trackerManager.createSubtask(subtask1e1);
        trackerManager.createSubtask(subtask2e1);
        trackerManager.createSubtask(subtask1e2);

        System.out.println("Список всех задач: " + trackerManager.getAllTasks());
        System.out.println();
        System.out.println("Список всех эпиков: " + trackerManager.getAllEpics());
        System.out.println();
        System.out.println("Список всех подзадач: " + trackerManager.getAllSubtasks());
        System.out.println();

       Task newTask = new Task("Задача 1", "Помыть посуду", "DONE", 1);
       trackerManager.updateTask(newTask);
       Subtask newSubtask = new Subtask("Подзадача 1 Эпик 1", "Найти подходящую квартиру",
                "IN_PROGRESS", 5, 3);
       trackerManager.updateSubtask(newSubtask);

        System.out.println("После изменения статуса задачи 1  и подзадачи 1 Эпика 1");
        System.out.println("Список всех задач: " + trackerManager.getAllTasks());
        System.out.println();
        System.out.println("Список всех эпиков: " + trackerManager.getAllEpics());
        System.out.println();
        System.out.println("Список всех подзадач: " + trackerManager.getAllSubtasks());
        System.out.println();

        trackerManager.removeTaskById(1);
        trackerManager.removeEpicById(4);

        System.out.println("После удаления задачи 1 и Эпика 2: ");
        System.out.println("Список всех задач: " + trackerManager.getAllTasks());
        System.out.println();
        System.out.println("Список всех эпиков: " + trackerManager.getAllEpics());
        System.out.println();
        System.out.println("Список всех подзадач: " + trackerManager.getAllSubtasks());
        System.out.println();
    }
}
