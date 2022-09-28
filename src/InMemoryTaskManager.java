import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected int id = 0;                                        //Начальный id

    protected HashMap<Integer, Task> tasks = new HashMap<>();            //Мапа для задач
    protected HashMap<Integer, Epic> epics = new HashMap<>();            //Мапа для эпиков
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();      //Мапа для подзадач
    protected HistoryManager history = Managers.getDefaultHistory();   // Получаем менеджер истории

    StartTimeComparator comparator = new StartTimeComparator();   //Компаратор для сравнения starTime

    @Override
    public int getId() {                                       //Получаем новый уникальный id
        id++;
        return id;
    }

    public TreeSet<Task> getPrioritizedTasks() {                 //Создаем сортированный список задач по времени начала
        TreeSet<Task> startTimeSet = new TreeSet<>(comparator);

        for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
            startTimeSet.add(entry.getValue());
        }
        for (Map.Entry<Integer, Epic> entry : epics.entrySet()) {

            startTimeSet.add(entry.getValue());
        }
        for (Map.Entry<Integer, Subtask> entry : subtasks.entrySet()) {

            startTimeSet.add(entry.getValue());
        }
        return startTimeSet;
    }

    public Temporal setEpicsStartTime(Epic epic) {                                  //Определяем время старта эпика
        List<Integer> subtasksIds = epic.getSubtasksIds();
        LocalDateTime subtaskStartTime = subtasks.get(subtasksIds.get(0)).startTime; //Время старта первой подзадачи
        for (Integer subtasksId : subtasksIds) {
            if (subtaskStartTime.isAfter(subtasks.get(subtasksId).startTime)) {  //Находим самую позднюю подзадачу
                subtaskStartTime = subtasks.get(subtasksId).startTime;
            }
        }
        return subtaskStartTime;
    }

    public Temporal setEpicsEndTime(Epic epic) {                                  //Определяем время завершения эпика
        List<Integer> subtasksIds = epic.getSubtasksIds();
        LocalDateTime subtaskStartTime = subtasks.get(subtasksIds.get(0)).getEndTime(); //Время завершения первой подзадачи
        for (Integer subtasksId : subtasksIds) {
            if (subtaskStartTime.isAfter(subtasks.get(subtasksId).getEndTime())) {  //Находим самое позднее завершение подзадачи
                subtaskStartTime = subtasks.get(subtasksId).getEndTime();
            }
        }
        return subtaskStartTime;
    }

    public boolean validateTasksAndEpics(Task task) {   //Проверяем задачи и эпики на наличие пересечений по времени
        boolean isValid = true;
        TreeSet<Task> startTimeSet = getPrioritizedTasks();
        for (Task t : startTimeSet) {
            if (task.getEndTime().isBefore(t.getEndTime())) {
                return false;
            }
        }
        return isValid;
    }

    public boolean validateSubtasks(Subtask subtask) {   //Проверяем подзадачи и наличие пересечений по времени со всеми задачами кроме эпиков
        boolean isValid = true;
        TreeSet<Task> startTimeSet = getPrioritizedTasks();
        for (Task t : startTimeSet) {
            if (!t.getType().equals(Type.EPIC)) {
                if (subtask.getEndTime().isBefore(t.getEndTime())) {
                    return false;
                }
            }
        }
        return isValid;
    }

    @Override
    public int createTask(Task task) {                   //Добавляем задачу
        task.setEndTime();                               //Определяем время завершения
        if (validateTasksAndEpics(task)) {            //Проверяем на пересечение во времени с существующими задачами
            tasks.put(getId(), task);
            task.setId(id);
            return id;
        } else {
            System.out.println("The task has time intersection with existing task. Choose another start time");
            return 0;
        }
    }

    @Override
    public int createEpic(Epic epic) {                         //Добавляем эпик
        epic.setEndTime();                                     //Определяем время завершения
        if (validateTasksAndEpics(epic)) {               //Проверяем на пересечение во времени с существующими задачами
            epics.put(getId(), epic);
            epic.setId(id);
            return id;
        } else {
            System.out.println("The task has time intersection with existing task. Choose another start time");
            return 0;
        }
    }

    @Override
    public int createSubtask(Subtask subtask) {                 //Добавляем подзадачу
        subtask.setEndTime();                                   //Определяем время завершения подзадачи
        if (validateSubtasks(subtask)) {                  //Проверяем на пересечение во времени с существующими задачами кроме эпиков,
            subtasks.put(getId(), subtask);               // тк подзадача входит в эпик
            subtask.setId(id);
            epics.get(subtask.getEpicId()).setSubtasksIds(id);      //Записываем id подзадачи в эпик
            int epicsId = subtask.getEpicId();
            changeEpicsStatus(epicsId);                                //Вызываем метод для обновления статуса эпика
            Epic epic = epics.get(epicsId);
            epic.setStartTime((LocalDateTime) setEpicsStartTime(epic)); //Высчитываем и устанавливаем время старта
            epic.setEndTime((LocalDateTime) setEpicsEndTime(epic));     // и завершения эпика по подзадачам

            Duration epicsDuration = Duration.between(setEpicsStartTime(epic),
                    setEpicsEndTime(epic));                              // Считаем продолжительность эпика
            epic.setDuration((int) (epicsDuration.getSeconds() / 60));   //Переводим в минуты и устанавливаем продолжительность
            return id;
        } else {
            System.out.println("The task has time intersection with existing task. Choose another start time");
            return 0;
        }
    }


    @Override
    public HashMap<Integer, Task> getAllTasks() {                               // Получаем все задачи
        return tasks;
    }

    @Override
    public HashMap<Integer, Epic> getAllEpics() {                               //Получаем все эпики
        return epics;
    }

    @Override
    public HashMap<Integer, Subtask> getAllSubtasks() {                             //Получаем все подзадачи
        return subtasks;
    }

    @Override
    public void removeAllTasks() {                                      //Удаляем все задачи
        tasks.clear();
    }

    @Override
    public void removeAllEpics() {                                      //Удаляем все эпики
        epics.clear();
        subtasks.clear();                                        //Также удаляем все подзадачи
    }

    @Override
    public void removeAllSubtasks() {                                   //Удаляем все подзадачи
        subtasks.clear();
        for (int key : epics.keySet()) {                         //Вызываем метод удаления id подзадач для всех эпиков
            epics.get(key).removeAllSubtasksIds();
            changeEpicsStatus(key);                              //Вызываем метод изменение статуса для всех эпиков
        }
    }

    @Override
    public Task getTask(int id) {                                 //Получаем задачу по id
        history.add(tasks.get(id));                               //Добавляем вызов задачи в историю
        return tasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {                                 //Получаем эпик по id
        history.add(epics.get(id));                               //Добавляем вызов эпика в историю
        return epics.get(id);
    }

    @Override
    public Subtask getSubtask(int id) {                           //Получаем подзадачу по id
        history.add(subtasks.get(id));                            //Добавляем вызов эпика в историю
        return subtasks.get(id);
    }

    @Override
    public void removeTaskById(int id) {                           //Удаляем задачу по id
        tasks.remove(id);
        history.remove(id);                                       //Удаляем из истории
    }

    @Override
    public void removeEpicById(int id) {                          //Удаляем эпик по id
        for (int subId : epics.get(id).getSubtasksIds()) {        //Удаляем все подзадачи эпика
            subtasks.remove(subId);
            history.remove(subId);                                //Удаляем подзадачи из истории
        }
        epics.remove(id);                                         //Удаляем сам эпик
        history.remove(id);                                       //Удаляем из истории
    }

    @Override
    public void removeSubtaskById(int id) {                       //Удаляем подзадачу по id
        int epicsId = subtasks.get(id).getEpicId();
        epics.get(epicsId).removeSubtask(subtasks.get(id));     //Удаляем подзадачу из эпика
        subtasks.remove(id);
        changeEpicsStatus(epicsId);                               //Вызываем метод для обновления статуса эпика
        history.remove(id);                                       //Удаляем из истории
    }

    @Override
    public ArrayList<Object> getEpicsSubtasks(int id) {           // Получение подзадач эпика
        if (epics.containsKey(id)) {                              //Проверка переданного id на соотвествие id эпика
            Epic epic = epics.get(id);
            ArrayList<Object> subtasksList = new ArrayList<>();   //Список для подзадач
            for (Integer subId : epic.getSubtasksIds()) {
                subtasksList.add(subtasks.get(subId));            //Добавляем подзадачи по id
            }
            return subtasksList;
        }
        return null;                                              //Если id не соотвествует возвращаем null
    }

    @Override
    public void updateTask(Task newTask) {                        //Обновить задачу
        int id = newTask.getId();
        Task savedTask = tasks.get(id);                           //Проверяем наличие такой задачи
        if (savedTask == null) {
            return;
        }
        if(validateTasksAndEpics(newTask)) {                //Проверяем на пересечение по времени
            tasks.put(id, newTask);                          //Перезаписываем
        }
    }

    @Override
    public void updateEpic(Epic newEpic) {                        //Обновить эпик
        int id = newEpic.getId();
        Task savedEpic = epics.get(id);                           //Проверяем наличие такого эпика
        if (savedEpic == null) {
            return;
        }
        if(validateTasksAndEpics(newEpic)) {                         //Проверяем на пересечение по времени
            epics.put(id, newEpic);                                  //Перезаписываем
        }
    }

    @Override
    public void updateSubtask(Subtask newSubtask) {              //Обновить подзадачу
        int id = newSubtask.getId();
        Task savedSubtask = subtasks.get(id);                    //Проверяем наличие такой подзадачи
        if (savedSubtask == null) {
            return;
        }
        subtasks.put(id, newSubtask);                            //Перезаписываем
        int epicsId = newSubtask.getEpicId();
        changeEpicsStatus(epicsId);                             //Вызываем метод для обновления статуса эпика

    }                          //Вызываем метод для обновления статуса эпика


    @Override
    public void changeEpicsStatus(int id) {
        if (epics.get(id).getSubtasksIds().isEmpty()) {                  //Если подзадач нет статус NEW
            epics.get(id).setStatus(Status.NEW);
            return;
        }
        ArrayList<Integer> subtaskIds = epics.get(id).getSubtasksIds();
        Status curStatus = (subtasks.get(subtaskIds.get(0)).status);      //Присваиваем переменной статус первой
                                                                          // подзадачи в эпике

        for (int subId : epics.get(id).getSubtasksIds()) {

            if (subtasks.get(subId).status.equals(curStatus)) {           //Сравниваем статус всех подзадач с первым,
                epics.get(id).setStatus(curStatus);                       // если статусы одинаковы, присваиваем статус
                // эпику
            } else
                epics.get(id).setStatus(Status.IN_PROGRESS);             //Во всех остальных случаях статус IN_PROGRESS
        }
    }

    @Override
    public List<Task> getHistory() {                                        //Просмотр истории
        return history.getHistory();
    }
}

class StartTimeComparator implements Comparator<Task> {   //Переопределяем компаратор для сравнения времени начала
    @Override
    public int compare(Task o1, Task o2) {
        if (o1.startTime.isAfter(o2.startTime)) {
            return 1;
        } else if (o1.startTime.isBefore(o2.startTime)) {
            return -1;
        } else
            return 0;
    }
}