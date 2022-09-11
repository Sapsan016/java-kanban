
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private static final String FILE_HEADER = "id,type,name,status,description,epic`s/subtask`s id"; // Поле для заголовка

    private static final Path path = Paths.get("testFile.csv");                //Путь для файла


    private final String fileName;                                                 //Поле для имени файла


    public FileBackedTasksManager(String fileName) {                               //Конструктор
        super();
        this.fileName = fileName;
    }

    public static void main(String[] args) throws IOException {
        TaskManager taskManager = Managers.getNewManager();

        //Создаем задачи и заполняем и изменяем историю просмотров

        Task task1 = new Task("Задача 1", "Помыть посуду", Status.NEW, 0);
        Task task2 = new Task("Задача 2", "Приготовить ужин", Status.NEW, 0);

        Epic epic1 = new Epic("Эпик1", "Купить новую квартиру", Status.NEW, 0);
        Epic epic2 = new Epic("Эпик2", "Организовать переезд", Status.NEW, 0);


        Subtask subtask1e1 = new Subtask("Подзадача 1 Эпик 1", "Найти подходящую квартиру",
                Status.NEW, 0, 3);
        Subtask subtask2e1 = new Subtask("Подзадача 2 Эпик 1", "Проверить документы",
                Status.NEW, 0, 3);
        Subtask subtask3e1 = new Subtask("Подзадача 2 Эпик 1", "Перевести деньги",
                Status.NEW, 0, 3);


        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask1e1);
        taskManager.createSubtask(subtask2e1);
        taskManager.createSubtask(subtask3e1);

        taskManager.getTask(task1.getId());

        taskManager.getTask(task2.getId());


        taskManager.getEpic(epic1.getId());

        taskManager.getSubtask(subtask1e1.getId());

        task1.setStatus(Status.DONE);
        taskManager.updateTask(task1);
        taskManager.getHistory();
        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        taskManager.getEpic(epic2.getId());
        taskManager.getHistory();




//Создаем новый менеджер из файла
        FileBackedTasksManager fbm = loadFromFile(path.toFile());
        fbm.fromString(fbm.readFromFile(path.toString()));               //Считываем данные
        System.out.println(fbm.getAllTasks());                           //Проверяем все задачи,
        System.out.println(fbm.getAllEpics());                           //эпики,
        System.out.println(fbm.getAllSubtasks());                        //подзадачи
        fbm.recoveryHistory(historyFromString(fbm.readFromFile(path.toString()))); //Восстанавливаем историю просмотров
        System.out.println(fbm.history.getHistory());                       //Проверяем историю
    }

    public void recoveryHistory(List<Integer> historyList) {    //Восстанавливаем историю из списка
        for (Integer i : historyList) {                        //Ищем по ID просмотренные задачи
            if (tasks.containsKey(i)) {
                getTask(i);
            }
            if (epics.containsKey(i)) {
                getEpic(i);
            }
            if (subtasks.containsKey(i)) {
                getSubtask(i);
            }
        }
    }

    public void save() {                                                               //Сохраняем все задачи
        try (Writer fileWriter = new FileWriter(String.valueOf(createFile()), StandardCharsets.UTF_8, true)) {
            fileWriter.write(FILE_HEADER + "\n");                                   //Сохранение заголовка

            for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {                  //Сохранение задач
                fileWriter.write(entry.getValue().toString() + "\n");
            }
            for (Map.Entry<Integer, Epic> entry : epics.entrySet()) {

                fileWriter.write(entry.getValue().toString() + "\n");              //Сохранение эпиков
            }
            for (Map.Entry<Integer, Subtask> entry : subtasks.entrySet()) {

                fileWriter.write(entry.getValue().toString() + "\n");             //Печать подзадач
            }
            fileWriter.write("" + "\n");                                        //Сохранение в файл пустой строки
            fileWriter.write(historyToString(history));

        } catch ( IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public Path createFile() throws IOException {    //Создаем новый файл для записи
        if (Files.exists(path)) {                    //Если файл уже есть,
            Files.delete(path);                      // вначале удаляем
        }
        return Files.createFile(path);              // и создаем новый
    }

    public String readFromFile(String path)  {    //Считываем данные из файла в строку
        try {
            return Files.readString(Path.of(path));
        } catch (IOException e) {
            System.out.println("Невозможно прочитать файл. " +
                    "Возможно, файл не находится в нужной директории.");
            return null;
        }
    }

    public void fromString(String value) {                          //Восстанавливаем задачи из строки

        String[] lines = value.split("\n");                   //Делим строку по линиям
        for (int i = 1; i < lines.length; i++) {                    //начиная со второй
            String line = lines[i];
            if (line.equals("")) {                                   //Пустая линия - завершаем метод
                return;
            }
            String[] parts = line.split(",");                     // Делим строку по запятым
            int id = Integer.parseInt(parts[0]);                       //Сохраняем id из первой колонки
            Type type = Type.valueOf(parts[1]);                        //Сохраняем тип задачи из второй колонки
            String name = parts[2];                                    //Сохраняем имя задачи из третей колонки
            Status status = Status.valueOf(parts[3]);                  //Сохраняем статус задачи из четвертой колонки
            String description = parts[4];                             //Сохраняем описание задачи из пятой колонки
            int epicsId = 0;
            List<Integer> ids = new ArrayList<>();
            if (parts.length > 5) {                                  //Если эпик или подзадача то
                epicsId = Integer.parseInt(parts[5]);                //добавляем соотвествующий id
                int j = 5;                                          //Если подзадач 2 и более, то добавляем их в список
                while (j < parts.length) {
                    ids.add(Integer.parseInt(parts[j++]));
                }
            }
            switch (type) {                                                 //Сохраняем данные
                case TASK: {                                               //Задача
                    tasks.put(id, new Task(name, description, status, id));
                    break;
                }
                case EPIC: {
                    Epic epic = new Epic(name, description, status, id);    //Эпик
                    if (!ids.isEmpty()) {                                   //Если список подзадач не пустой
                        for (Integer n : ids) {                             //то добавляем подзпдпчи к эпику
                            epic.setSubtasksIds(n);
                        }
                    } else {
                        epic.setSubtasksIds(epicsId);                       //если только одна подзадача
                    }
                    epics.put(id, epic);
                    break;
                }
                case SUBTASK: {                                             //Подзадача
                    subtasks.put(id, new Subtask(name, description, status, id, epicsId));
                }
            }
        }
    }

    static String historyToString(HistoryManager manager) {               //Сохраняем в строку историю просмотров
        String str = "";
        if (!manager.getHistory().isEmpty()) {
            for (int i = 0; i < manager.getHistory().size(); i++) {

                str = (str + manager.getHistory().get(i).id + ",");
            }
        }
        return str;
    }

    static List<Integer> historyFromString(String value) {         //Восстанавливаем список просмотренных задач
        List<Integer> historyList = new ArrayList<>();
        String[] lines = value.split("\n");                   //Делим строку по линиям
        for (int i = 1; i < lines.length; i++) {                    //начиная со второй
            String line = lines[i];
            if (line.equals("")) {                                   //Находим пустую строку
                line = lines[i + 1];                                //Строка с историей
                String[] parts = line.split(",");              // Делим строку по запятым
                int j = 0;
                while (j < parts.length) {
                    historyList.add(Integer.parseInt(parts[j++]));   //Добавляем id просмотренных задач в список
                }
            }
        }
        return historyList;
    }

    static FileBackedTasksManager loadFromFile(File file) {

        return new FileBackedTasksManager(file.toString());
    }

    @Override
    public int createTask(Task task) {                         //Добавляем задачу
        super.createTask(task);                                //Выполняем родительскую версию
        save();
        return id;
    }

    @Override
    public int createEpic(Epic epic) {                         //Добавляем эпик
        super.createEpic(epic);                                //Выполняем родительскую версию
        save();
        return id;
    }

    @Override
    public int createSubtask(Subtask subtask) {                 //Добавляем подзадачу
        super.createSubtask(subtask);                           //Выполняем родительскую версию
        save();
        return id;
    }

    @Override
    public void removeAllTasks() {                                //Удаляем все задачи
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllEpics() {                                      //Удаляем все эпики
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeAllSubtasks() {                                   //Удаляем все подзадачи
        super.removeAllSubtasks();                                      //Выполняем родительскую версию
        save();
    }

    @Override
    public void removeTaskById(int id) {                           //Удаляем задачу по id
        super.removeTaskById(id);                                  //Выполняем родительскую версию
        save();
    }

    @Override
    public void removeEpicById(int id) {                           //Удаляем эпик по id
        super.removeEpicById(id);                                  //Выполняем родительскую версию
        save();
    }

    @Override
    public void removeSubtaskById(int id) {                       //Удаляем подзадачу по id
        super.removeSubtaskById(id);                              //Выполняем родительскую версию
        save();
    }

    @Override
    public void updateTask(Task newTask) {                        //Обновить задачу
        super.updateTask(newTask);                                //Выполняем родительскую версию
        save();
    }

    @Override
    public void updateEpic(Epic newEpic) {                        //Обновить эпик
        super.updateEpic(newEpic);                                //Выполняем родительскую версию
        save();
    }

    @Override
    public void updateSubtask(Subtask newSubtask) {              //Обновить подзадачу
        super.updateSubtask(newSubtask);                          //Выполняем родительскую версию
        save();
    }

    @Override
    public void changeEpicsStatus(int id) {                    //Меняем статус эпика
        super.changeEpicsStatus(id);                          //Выполняем родительскую версию
        save();
    }

    @Override
    public List<Task> getHistory() {                                            //Просмотр истории
        save();
        return history.getHistory();
    }
}