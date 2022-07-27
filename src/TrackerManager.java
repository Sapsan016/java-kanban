import java.util.ArrayList;
import java.util.HashMap;

public class TrackerManager {

    private int id = 0;                                        //Начальный id

    HashMap<Integer, Task> tasks = new HashMap<>();            //Мапа для задач
    HashMap<Integer, Epic> epics = new HashMap<>();            //Мапа для эпиков
    HashMap<Integer, Subtask> subtasks = new HashMap<>();      //Мапа для подзадач

    public int getId() {                                       //Получаем новый уникальный id
        id++;
        return id;
    }

    public int createTask(Task task) {                         //Добавляем задачу
        tasks.put(getId(), task);
        task.setId(id);
        return id;
    }

    public int createEpic(Epic epic) {                         //Добавляем эпик
        epics.put(getId(), epic);
        epic.setId(id);
        return id;
    }

    public int createSubtask(Subtask subtask) {                 //Добавляем подзадачу
        subtasks.put(getId(), subtask);
        subtask.setId(id);

        epics.get(subtask.getEpicId()).setSubtasksIds(id);      //Записываем id подзадачи в эпик
        int epicsId = subtask.getEpicId();
        changeEpicsStatus(epicsId);                            //Вызываем метод для обновления статуса эпика

        return id;
    }

    public Object getAllTasks() {                               // Получаем все задачи
        return tasks;
    }

    public Object getAllEpics() {                               //Получаем все эпики
        return epics;
    }

    public Object getAllSubtasks() {                             //Получаем все подзадачи
        return subtasks;
    }

    void removeAllTasks() {                                      //Удаляем все задачи
        tasks.clear();
    }

    void removeAllEpics() {                                      //Удаляем все эпики
        epics.clear();
        subtasks.clear();                                        //Также удаляем все подзадачи
    }

    void removeAllSubtasks() {                                   //Удаляем все подзадачи
        subtasks.clear();

        for (int key : epics.keySet()) {                         //Вызываем метод удаления id подзадач для всех эпиков
            epics.get(key).removeAllSubtasksIds();

            changeEpicsStatus(key);                              //Вызываем метод изменение статуса для всех эпиков

        }
    }

    public Task getTask(int id) {                                 //Получаем задачу по id
        return tasks.get(id);
    }

    public Epic getEpic(int id) {                                 //Получаем эпик по id
        return epics.get(id);
    }

    public Subtask getSubtask(int id) {                           //Получаем подзадачу по id
        return subtasks.get(id);
    }

    void removeTaskById(int id) {                                 //Удаляем задачу по id
        tasks.remove(id);
    }

    void removeEpicById(int id) {                                 //Удаляем эпик по id
        for (int subId : epics.get(id).getSubtasksIds()) {        //Удаляем все подзадачи эпика
            subtasks.remove(subId);
        }
        epics.remove(id);                                         //Удаляем сам эпик
    }

    void removeSubtaskById(int id) {                              //Удаляем подзадачу по id
        int epicsId = subtasks.get(id).getEpicId();
        subtasks.remove(id);
        epics.get(epicsId).removeSubtask(id);                     //Удаляем подзадачу из эпика

        changeEpicsStatus(epicsId);                               //Вызываем метод для обновления статуса эпика
    }

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

    public void updateTask(Task newTask) {                        //Обновить задачу
        int id = newTask.getId();
        Task savedTask = tasks.get(id);                           //Проверяем наличие такой задачи
        if (savedTask == null) {
            return;
        }
        tasks.put(id, newTask);                                   //Перезаписываем
    }

    public void updateEpic(Epic newEpic) {                        //Обновить эпик
        int id = newEpic.getId();
        Task savedTask = epics.get(id);                           //Проверяем наличие такого эпика
        if (savedTask == null) {
            return;
        }
        tasks.put(id, newEpic);                                  //Перезаписываем

    }

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

    public void changeEpicsStatus(int id) {
        if (epics.get(id).getSubtasksIds().isEmpty()) {                  //Если подзадач нет статус NEW
            epics.get(id).setStatus("NEW");
            return;
        }
        ArrayList<Integer> subtaskIds = epics.get(id).getSubtasksIds();
        String curStatus = subtasks.get(subtaskIds.get(0)).status;      //Присваиваем переменной статус первой
        // подзадачи в эпике

        for (int subId : epics.get(id).getSubtasksIds()) {

            if (subtasks.get(subId).status.equals(curStatus)) {           //Сравниваем статус всех подзадач с первым,
                epics.get(id).setStatus(curStatus);                       // если статусы одинаковы, присваиваем статус
                                                                          // эпику
            } else
                epics.get(id).setStatus("IN_PROGRESS");                  //Во всех остальных случаях статус IN_PROGRESS
        }
    }
}








