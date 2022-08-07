public class Main {

    public static void main(String[] args) {
      //Получаем менеджер:
        TaskManager taskManager = Managers.getDefault();

     //Создаем задачи разных типов:

        Task task1 = new Task("Задача 1", "Помыть посуду", Status.NEW, 0);
        Task task2 = new Task("Задача 2", "Приготовить ужин", Status.NEW, 0);
        Task task3 = new Task("Задача 3", "Сходить в магазин", Status.NEW, 0);
        Epic epic1 = new Epic("Эпик1", "Купить новую квартиру", Status.NEW, 0);
        Epic epic2 = new Epic("Эпик2", "Организовать переезд", Status.NEW, 0);
        Epic epic3 = new Epic("Эпик 3", "Поехать в отпуск", Status.NEW, 0);

        Subtask subtask1e1 = new Subtask("Подзадача 1 Эпик 1", "Найти подходящую квартиру",
                Status.NEW, 0, 4);
        Subtask subtask2e1 = new Subtask("Подзадача 2 Эпик 1", "Проверить документы",
                Status.NEW, 0, 4);
        Subtask subtask1e2 = new Subtask("Подзадача 1 Эпик 2", "Найти транспорт",
                Status.NEW, 0, 5);
        Subtask subtask1e3 = new Subtask("Подзадача 1 Эпик 3", "Забронировать жильё",
                Status.NEW, 0, 6);
        Subtask subtask2e3 = new Subtask("Подзадача 2 Эпик 3", "Купить авиабилеты",
                Status.NEW, 0, 6);

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createEpic(epic3);
        taskManager.createSubtask(subtask1e1);
        taskManager.createSubtask(subtask2e1);
        taskManager.createSubtask(subtask1e2);
        taskManager.createSubtask(subtask1e3);
        taskManager.createSubtask(subtask2e3);

        //Вызываем разные методы менеджера:

       System.out.println("Список всех задач: " + taskManager.getAllTasks());
       System.out.println();
       System.out.println("Список всех эпиков: " + taskManager.getAllEpics());
       System.out.println();
       System.out.println("Список всех подзадач: " + taskManager.getAllSubtasks());
       System.out.println();

       //Меняем статус задач:
        task1.setStatus(Status.DONE);
        taskManager.updateTask(task1);
       subtask1e1.setStatus(Status.IN_PROGRESS);
       taskManager.updateSubtask(subtask1e1);
       subtask1e3.setStatus(Status.DONE);

    
       //Просматриваем задачи и вызываем историю просмотров:                 //Количество вызовов просмотра задач
        taskManager.getTask(task1.getId());                                                    //1
        taskManager.getTask(task2.getId());                                                    //2
        System.out.println("Вывод истории: " + taskManager.getHistory());
        System.out.println();
        taskManager.getSubtask(subtask1e1.getId());                                            //3
        System.out.println("Вывод истории: " + taskManager.getHistory());
        System.out.println();
        taskManager.getTask(task1.getId());                                                    //4
        taskManager.getTask(task1.getId());                                                    //5
         taskManager.getEpic(epic1.getId());                                                   //6
        System.out.println("Вывод истории: " + taskManager.getHistory());
        System.out.println();
        taskManager.getTask(task1.getId());                                                    //7
        taskManager.getTask(task2.getId());                                                    //8
        taskManager.getSubtask(subtask1e1.getId());                                            //9
        taskManager.getSubtask(subtask1e3.getId());                                            //10
        System.out.println("Вывод истории: " + taskManager.getHistory());
        System.out.println();
        taskManager.getTask(task1.getId());                                                    //11
        taskManager.getSubtask(subtask1e3.getId());                                            //12
        System.out.println("Вывод истории: " + taskManager.getHistory());
    }
}
