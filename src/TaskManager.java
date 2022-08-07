import java.util.ArrayList;
import java.util.List;

public interface TaskManager {     //Интерфейс менеджера задач

    int getId();

    int createTask(Task task);

    int createEpic(Epic epic);

    int createSubtask(Subtask subtask);

    Object getAllTasks();

    Object getAllEpics();

    Object getAllSubtasks();

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









