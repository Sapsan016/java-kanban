package Managers;

import Tasks.Epic;
import Tasks.Task;
import Tasks.Subtask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {     //Интерфейс менеджера задач

    int getId();

    int createTask(Task task);

    int createEpic(Epic epic);

    int createSubtask(Subtask subtask);

    HashMap<Integer, Task> getAllTasks();

    HashMap<Integer, Epic> getAllEpics();

    HashMap<Integer, Subtask> getAllSubtasks();

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubtasks();

    Task getTask(int id);

    Epic getEpic(int id);

    Subtask getSubtask(int id);

    void removeTaskById(int id);

    void removeEpicById(int id);

    void removeSubtaskById(int id);

    ArrayList<Object> getEpicsSubtasks(int id);

    void updateTask(Task newTask);

    void updateEpic(Epic newEpic);

    void updateSubtask(Subtask newSubtask);

    void changeEpicsStatus(int id);

    List<Task> getHistory();
}









