import java.util.*;

public class InMemoryTaskManager implements TaskManager {


    private int id = 0;                                        //Начальный id

    private final HashMap<Integer, Task> tasks = new HashMap<>();            //Мапа для задач
    private final HashMap<Integer, Epic> epics = new HashMap<>();            //Мапа для эпиков
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();      //Мапа для подзадач

    private final HistoryManager history = Managers.getDefaultHistory();   // Получаем менеджер истории

    @Override
    public int getId() {                                       //Получаем новый уникальный id
        id++;
        return id;
    }

    @Override
    public int createTask(Task task) {                         //Добавляем задачу
        tasks.put(getId(), task);
        task.setId(id);
        return id;
    }

    @Override
    public int createEpic(Epic epic) {                         //Добавляем эпик
        epics.put(getId(), epic);
        epic.setId(id);
        return id;
    }

    @Override
    public int createSubtask(Subtask subtask) {                 //Добавляем подзадачу
        subtasks.put(getId(), subtask);
        subtask.setId(id);

        epics.get(subtask.getEpicId()).setSubtasksIds(id);      //Записываем id подзадачи в эпик
        int epicsId = subtask.getEpicId();
        changeEpicsStatus(epicsId);                            //Вызываем метод для обновления статуса эпика

        return id;
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
    }

    @Override
    public void removeEpicById(int id) {                          //Удаляем эпик по id
        for (int subId : epics.get(id).getSubtasksIds()) {        //Удаляем все подзадачи эпика
            subtasks.remove(subId);
        }
        epics.remove(id);                                         //Удаляем сам эпик
    }

    @Override
    public void removeSubtaskById(int id) {                       //Удаляем подзадачу по id
        int epicsId = subtasks.get(id).getEpicId();
        subtasks.remove(id);
        epics.get(epicsId).removeSubtask(id);                     //Удаляем подзадачу из эпика

        changeEpicsStatus(epicsId);                               //Вызываем метод для обновления статуса эпика
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
        tasks.put(id, newTask);                                   //Перезаписываем
    }

    @Override
    public void updateEpic(Epic newEpic) {                        //Обновить эпик
        int id = newEpic.getId();
        Task savedTask = epics.get(id);                           //Проверяем наличие такого эпика
        if (savedTask == null) {
            return;
        }
        epics.put(id, newEpic);                                  //Перезаписываем

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

    }

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
   public List<Task> getHistory() {                                             //Просмотр истории
     return history.getHistory();
    }
}