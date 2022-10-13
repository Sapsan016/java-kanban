package Managers;
import KVServer.KVServer;
import Tasks.Epic;
import Tasks.Status;
import Tasks.Subtask;
import Tasks.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpTaskManagerTest {
    protected KVServer server;
    protected TaskManager manager;
    private final Task task = new Task("Test NewTask1", "Test NewTask1 description", Status.NEW, 0,
            30, LocalDateTime.now());
    private final Task task2 = new Task("Test NewTask2", "Test NewTask2 description", Status.NEW, 1,
            20, LocalDateTime.now().plusMinutes(360));
    private final Epic epic = new Epic("Test NewEpic1", "Test NewEpic1 description", Status.NEW, 0,
            60, LocalDateTime.now());
    private final Epic epic2 = new Epic("Test NewEpic2", "Test NewEpic2 description", Status.NEW, 1,
            40, LocalDateTime.now().plusMinutes(90));
    private final Subtask subtask = new Subtask("Test NewSubtask1", "Test dNewSubtask1 description",
            Status.NEW, 0, 1, 25, LocalDateTime.now().plusMinutes(5));
    private final Subtask subtask2 = new Subtask("Test NewSubtask2", "Test dNewSubtask2 description",
            Status.NEW, 0, 1, 30, LocalDateTime.now().plusMinutes(60));

    @BeforeEach
    void setUp() throws IOException, InterruptedException {                  //Запускаем сервер и получаем менеджер
        server = new KVServer();
        server.start();
        manager = Managers.getDefault();
    }

    @AfterEach
    void stop() {
        server.stop();
    }

    @Test
    void shouldAddAndSaveTask() {                                           //Должна быть добавлена и сохранена задача
        manager.createTask(task);
        Task savedTask = manager.getTask(task.getId());
        Assertions.assertEquals(task.getId(), savedTask.getId(), "Tasks.Task not found.");
        assertNull(manager.getTask(0));                               //Проверяем возврат ноля с неверным ID
    }
    @Test
    void shouldAddSaveEpic() {                                              //Должен быть добавлен и сохранен эпик
        manager.createEpic(epic);
        Epic savedEpic = manager.getEpic(epic.getId());
        Assertions.assertEquals(epic.getId(), savedEpic.getId(), "Epic not found.");
        assertNull(manager.getEpic(0));                               //Проверяем возврат ноля с неверным ID
    }
    @Test
    void shouldAddAndSaveSubtask() {                                    //Должна быть добавлена и сохранена подзадача
        manager.createEpic(epic);
        manager.createSubtask(subtask);
        Subtask savedSubtask = manager.getSubtask(subtask.getId());
        Assertions.assertEquals(subtask.getId(), savedSubtask.getId(), "Tasks.Subtask not found.");
        assertNull(manager.getSubtask(0));                   //Проверяем возврат ноля с неверным ID
        Assertions.assertEquals(savedSubtask.getEpicId(), epic.getId(),   //Проверяем у подзадачи наличин Id эпика
                "Subtasks Id don`t match.");
    }
    @Test
    void shouldReturnAndRemoveTasks() {                            //Должны возвращаться и удаляться задачи
        manager.createTask(task);
        manager.createTask(task2);
        List<Task> savedTasks = manager.getAllTasks();
        assertNotNull(savedTasks, "Tasks don`t return.");
        assertEquals(2, savedTasks.size(), "Tasks amount is wrong."); //Проверяем количство
        Assertions.assertEquals(task, savedTasks.get(0),                             //Проверяем соответствие
                "Tasks don`t match.");
        manager.removeTaskById(task.getId());                                        //Удаляем задачу
        savedTasks = manager.getAllTasks();
        assertEquals(1, savedTasks.size(), "Tasks amount is wrong."); //Проверяем количство
        Assertions.assertEquals(task2, savedTasks.get(0),                            //Проверяем соответствие
                "Tasks don`t match.");
        manager.removeAllTasks();
        savedTasks = manager.getAllTasks();
        assertEquals(0, savedTasks.size(),                             //Проверяем что все задачи удалены
                "Tasks.Task`s map is NOT empty.");
    }

    @Test
    void shouldReturnAndRemoveEpics() {                                //Должны возвращаться и удаляться эпики
        manager.createEpic(epic);
        manager.createEpic(epic2);
        List<Epic> savedEpics = manager.getAllEpics();
        assertNotNull(savedEpics, "Epics don`t return.");
        assertEquals(2, savedEpics.size(), "Epics amount is wrong."); //Проверяем количство
        Assertions.assertEquals(epic, savedEpics.get(0),                              //Проверяем соответствие
                "Epics don`t match.");
        manager.removeEpicById(epic.getId());                                         //Удаляем эпик
        savedEpics = manager.getAllEpics();
        assertEquals(1, savedEpics.size(), "Epics amount is wrong."); //Проверяем количство
        Assertions.assertEquals(epic2, savedEpics.get(0),                             //Проверяем соответствие
                "Epics don`t match.");
        manager.removeAllEpics();
        savedEpics = manager.getAllEpics();
        assertEquals(0, savedEpics.size(),                                    //Проверяем что эпики удалены
                "Epic`s map is NOT empty.");
    }

    @Test
    void shouldReturnAndRemoveSubtasks() {                             //Должны возвращаться и удаляться подзадачи
        manager.createEpic(epic);
        manager.createSubtask(subtask);
        manager.createSubtask(subtask2);
        List<Subtask> savedSubtasks = manager.getAllSubtasks();
        assertNotNull(savedSubtasks, "Subtasks don`t return.");
        assertEquals(2, savedSubtasks.size(), "Subtasks amount is wrong."); //Проверяем количство
        assertEquals(subtask, savedSubtasks.get(0),
                "Epics don`t match.");                                             //Проверяем соответствие
        manager.removeSubtaskById(subtask2.getId());                                       //Удаляем подзадачу
        savedSubtasks = manager.getAllSubtasks();
        assertEquals(1, savedSubtasks.size(), "Subtasks amount is wrong."); //Проверяем количство
        assertEquals(subtask, savedSubtasks.get(0),
                "Epics don`t match.");                                             //Проверяем соответствие
        manager.removeAllSubtasks();
        savedSubtasks = manager.getAllSubtasks();
        assertEquals(0, savedSubtasks.size(),                                   //Проверяем что задачи удалены
                "Tasks.Subtask`s map is NOT empty.");
    }

    @Test
    void ShouldRecoverManagerFromServer() throws IOException, InterruptedException { //Должен восстанавливать менеджер из сервера
        manager.createEpic(epic);                                                    //Создаем задачи и заполяем историю
        manager.createSubtask(subtask);
        manager.createTask(task2);
        manager.getEpic(epic.getId());
        manager.getTask(task2.getId());
        manager.getSubtask(subtask.getId());
        List<Task> savedHistoryList = manager.getHistory();
        HTTPTaskManager newTestManager = Managers.getDefault();             //Создаем новый менеджер и загружаем данные
        newTestManager.fromString("tasks");
        newTestManager.fromString("epics");
        newTestManager.fromString("subtasks");
        newTestManager.fromString("history");

        assertEquals(savedHistoryList, newTestManager.getHistory(),
                "History Lists don`t match");                            //Проверяем восстановление истории
        assertEquals(task2, newTestManager.tasks.get(task2.getId()),
                "Tasks.Task not found.");                               //Проверяем восстановление задач
        assertEquals(epic, newTestManager.epics.get(epic.getId()),
                "Tasks.Task not found.");
        assertEquals(subtask, newTestManager.subtasks.get(subtask.getId()),
                "Tasks.Task not found.");
    }
}