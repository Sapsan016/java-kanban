public class Main {

    public static void main(String[] args) {
        //Получаем менеджер:
        TaskManager taskManager = Managers.getDefault();

        //Создаем задачи разных типов:

        Task task1 = new Task("Задача 1", "Помыть посуду", Status.NEW, 0);
        Task task2 = new Task("Задача 2", "Приготовить ужин", Status.NEW, 0);

        Epic epic1 = new Epic("Эпик1", "Купить новую квартиру", Status.NEW, 0);
        Epic epic2 = new Epic("Эпик2", "Организовать переезд", Status.NEW, 0);


        Subtask subtask1e1 = new Subtask("Подзадача 1 Эпик 1", "Найти подходящую квартиру",
                Status.NEW, 0, 3);
        Subtask subtask2e1 = new Subtask("Подзадача 2 Эпик 1", "Проверить документы",
                Status.NEW, 0, 3);
        Subtask subtask3e1 = new Subtask("Подзадача 3 Эпик 1", "Совершить сделку",
                Status.NEW, 0, 3);


        taskManager.createTask(task1);
        taskManager.createTask(task2);

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        taskManager.createSubtask(subtask1e1);
        taskManager.createSubtask(subtask2e1);
        taskManager.createSubtask(subtask3e1);


        //Просматриваем задачи и вызываем историю просмотров:
        taskManager.getTask(task1.getId());
        System.out.println("Вывод истории: \n" + taskManager.getHistory());
        System.out.println();
        taskManager.getTask(task2.getId());
        System.out.println("Вывод истории: \n" + taskManager.getHistory());
        System.out.println();
        taskManager.getEpic(epic2.getId());
        System.out.println("Вывод истории: \n" + taskManager.getHistory());
        System.out.println();
        taskManager.getSubtask(subtask1e1.getId());
        System.out.println("Вывод истории: \n" + taskManager.getHistory());
        System.out.println();
        taskManager.getTask(task1.getId());
        System.out.println("Вывод истории: (после повторного вызова Задачи 1)\n" + taskManager.getHistory());
        System.out.println();
        taskManager.getTask(task2.getId());
        System.out.println("Вывод истории: (после повторного вызова Задачи 2)\n" + taskManager.getHistory());
        System.out.println();
        taskManager.getEpic(epic1.getId());
        System.out.println("Вывод истории: \n" + taskManager.getHistory());
        System.out.println();
        taskManager.getEpic(epic1.getId());
        System.out.println("Вывод истории: (после повторного вызова Эпика 1)\n" + taskManager.getHistory());
        System.out.println();
        taskManager.getTask(task2.getId());
        System.out.println("Вывод истории: (после третьего вызова Задачи 2)\n" + taskManager.getHistory());
        System.out.println();
        taskManager.getSubtask(subtask1e1.getId());
        System.out.println("Вывод истории: (после повторного вызова Подзадачи 1)\n" + taskManager.getHistory());
        System.out.println();
        taskManager.getSubtask(subtask3e1.getId());
        System.out.println("Вывод истории: \n" + taskManager.getHistory());
        System.out.println();
        taskManager.removeTaskById(task1.getId());         //Удаляем задачу 1
        System.out.println("Вывод истории: (после удаления Задачи 1)\n" + taskManager.getHistory());
        System.out.println();


        taskManager.removeEpicById(epic1.getId());       //Удаляем эпик 1
        System.out.println("Вывод истории: (после удаления Эпика 1)\n" + taskManager.getHistory());
        System.out.println();
        taskManager.getSubtask(subtask3e1.getId()); //Вызываем подзадачу удаленного эпика
        System.out.println("Вывод истории: (после вызова подзадачи удаленного эпика)\n" + taskManager.getHistory());
    }
}
