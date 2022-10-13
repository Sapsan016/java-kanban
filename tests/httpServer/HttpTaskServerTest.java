package httpServer;

import managers.Managers;
import managers.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;
import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpTaskServerTest {
    protected HttpTaskServer server;
    protected TaskManager manager;
    protected HttpClient client;
    protected Gson gson = Managers.getGson();
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
    void setUp() throws IOException {
        manager = Managers.getNewManager();
        server = new HttpTaskServer(manager);
        server.start();
        client = HttpClient.newHttpClient();
    }

    @AfterEach
    void stop() {
        server.stop();
    }

    @Test
    void shouldAddTask() throws IOException, InterruptedException {                  //Должна добавиться задача
        URI uri = URI.create("http://localhost:8080/tasks/task/");
        String json = gson.toJson(task);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertNotNull(manager.getAllTasks(), "Tasks don`t return");
        assertEquals(1, manager.getAllTasks().size(), "Wrong size of the list");
        task.setId(1);
        assertEquals(task, manager.getTask(1), "Tasks don`t match");
    }

    @Test
    void shouldUpdateTask() throws IOException, InterruptedException {                  //Должна обновиться задача
        manager.createTask(task);
        URI uri = URI.create("http://localhost:8080/tasks/task/");
        task.setId(1);
        task.setStatus(Status.DONE);
        String json = gson.toJson(task);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertEquals(task.getStatus(), manager.getTask(1).getStatus(), "Task`s status don`t match");
    }

    @Test
    void shouldAddEpic() throws IOException, InterruptedException {                  //Должен добавиться эпик
        URI uri = URI.create("http://localhost:8080/tasks/epic/");
        String json = gson.toJson(epic);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertNotNull(manager.getAllEpics(), "Epics don`t return");
        assertEquals(1, manager.getAllEpics().size(), "Wrong size of the list");
        epic.setId(1);
        assertEquals(epic, manager.getEpic(1), "Epics don`t match");
    }

    @Test
    void shouldUpdateEpic() throws IOException, InterruptedException {                  //Должен обновиться эпик
        manager.createEpic(epic);
        URI uri = URI.create("http://localhost:8080/tasks/epic/");
        epic.setId(1);
        epic.setStatus(Status.IN_PROGRESS);
        String json = gson.toJson(epic);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertEquals(epic.getStatus(), manager.getEpic(1).getStatus(), "Epic`s status don`t match");
    }

    @Test
    void shouldAddSubtask() throws IOException, InterruptedException {                  //Должна добавиться подзадача
        URI uri = URI.create("http://localhost:8080/tasks/subtask/");
        manager.createEpic(epic);
        String json = gson.toJson(subtask);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertNotNull(manager.getAllSubtasks(), "Subtasks don`t return");
        assertEquals(1, manager.getAllSubtasks().size(), "Wrong size of the list");
        subtask.setId(2);
        assertEquals(subtask, manager.getSubtask(2), "Subtask don`t match");
    }

    @Test
    void shouldUpdateSubtask() throws IOException, InterruptedException {                  //Должна обновиться подзадача
        manager.createEpic(epic);
        manager.createSubtask(subtask);
        URI uri = URI.create("http://localhost:8080/tasks/subtask/");
        subtask.setId(2);
        task.setStatus(Status.DONE);
        String json = gson.toJson(subtask);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertEquals(subtask.getStatus(), manager.getSubtask(2).getStatus(), "Subtasks`s status don`t match");
    }

    @Test
    void shouldGetTasks() throws IOException, InterruptedException {                  //Должны возвращаться все задачи
        manager.createTask(task);
        manager.createTask(task2);
        URI uri = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Type taskType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> tasks = gson.fromJson(response.body(), taskType);
        assertNotNull(response.body(), "Tasks don`t return");
        assertEquals(2, tasks.size(), "Wrong size of the list");
        assertEquals(task, tasks.get(0), "Tasks don`t match");
    }

    @Test
    void shouldGetTaskById() throws IOException, InterruptedException {             //Должна возвращаться задача по Id
        manager.createTask(task);
        URI uri = URI.create("http://localhost:8080/tasks/task/1");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Type taskType = new TypeToken<Task>() {
        }.getType();
        Task actual = gson.fromJson(response.body(), taskType);
        assertNotNull(response, "Task don`t return");
        assertEquals(task, actual, "Tasks don`t match");
    }

    @Test
    void shouldGetEpics() throws IOException, InterruptedException {                  //Должны возвращаться все эпики
        manager.createEpic(epic);
        URI uri = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Type epicType = new TypeToken<ArrayList<Epic>>() {
        }.getType();
        List<Epic> epics = gson.fromJson(response.body(), epicType);
        assertNotNull(epics, "Epics don`t return");
        assertEquals(1, epics.size(), "Wrong size of the list");
        assertEquals(epic, epics.get(0), "Epics don`t match");
    }

    @Test
    void shouldGetEpicById() throws IOException, InterruptedException {             //Должен возвращаться эпик по Id
        manager.createEpic(epic);
        URI uri = URI.create("http://localhost:8080/tasks/epic/1");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Type epicType = new TypeToken<Epic>() {
        }.getType();
        Epic actual = gson.fromJson(response.body(), epicType);
        assertNotNull(epic, "Epic don`t return");
        assertEquals(epic, actual, "Epics don`t match");
    }

    @Test
    void shouldGetEpicsSubtasks() throws IOException, InterruptedException { //Должен возвращаться список подзадач эпика
        manager.createEpic(epic);
        manager.createSubtask(subtask);
        URI uri = URI.create("http://localhost:8080/tasks/epic/1/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Type subtaskType = new TypeToken<ArrayList<Subtask>>() {
        }.getType();
        List<Subtask> subtasks = gson.fromJson(response.body(), subtaskType);
        assertNotNull(subtasks, "Subtasks don`t return");
        assertEquals(1, subtasks.size(), "Wrong size of the list");
        assertEquals(subtask, subtasks.get(0), "Subtasks don`t match");
    }

    @Test
    void shouldGetSubtasks() throws IOException, InterruptedException {             //Должны возвращаться все подзадачи
        manager.createEpic(epic);
        manager.createSubtask(subtask);
        URI uri = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Type subtaskType = new TypeToken<ArrayList<Subtask>>() {
        }.getType();
        List<Subtask> subtasks = gson.fromJson(response.body(), subtaskType);
        assertNotNull(subtasks, "Subtasks don`t return");
        assertEquals(1, subtasks.size(), "Wrong size of the list");
        assertEquals(subtask, subtasks.get(0), "Subtasks don`t match");
    }

    @Test
    void shouldGetSubtaskById() throws IOException, InterruptedException {        //Должен возвращаться подзадача по Id
        manager.createEpic(epic);
        manager.createSubtask(subtask);
        URI uri = URI.create("http://localhost:8080/tasks/subtask/2");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Type subtaskType = new TypeToken<Subtask>() {
        }.getType();
        Subtask actual = gson.fromJson(response.body(), subtaskType);
        assertNotNull(subtask, "Subtask don`t return");
        assertEquals(subtask, actual, "Subtasks don`t match");
    }

    @Test
    void shouldRemoveTasks() throws IOException, InterruptedException {               //Должны удаляться все задачи
        manager.createTask(task);
        URI uri = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode());
        List<Task> checkList = new ArrayList<>();
        assertEquals(checkList, manager.getAllTasks(), "Tasks were not removed");
    }

    @Test
    void shouldRemoveTaskById() throws IOException, InterruptedException {           //Должна удаляться задача по Id
        manager.createTask(task);
        manager.createTask(task2);
        URI uri = URI.create("http://localhost:8080/tasks/task/1");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode());
        List<Task> checkList;
        checkList = manager.getAllTasks();
        assertNull(manager.getTask(task.getId()), "Task was not removed");
        assertEquals(task2, checkList.get(0), "Removed tasks don`t match");
    }

    @Test
    void shouldRemoveEpics() throws IOException, InterruptedException {               //Должны удаляться все эпики
        manager.createEpic(epic);
        URI uri = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode());
        List<Epic> checkList = new ArrayList<>();
        assertEquals(checkList, manager.getAllEpics(), "Epics were not removed");
    }

    @Test
    void shouldRemoveEpicById() throws IOException, InterruptedException {             //Должен удаляться эпик по Id
        manager.createEpic(epic);
        manager.createEpic(epic2);
        URI uri = URI.create("http://localhost:8080/tasks/epic/1");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode());
        List<Epic> checkList;
        checkList = manager.getAllEpics();
        assertNull(manager.getEpic(epic.getId()), "Epic was not removed");
        assertEquals(epic2, checkList.get(0), "Removed epics don`t match");
    }

    @Test
    void shouldRemoveSubtasks() throws IOException, InterruptedException {             //Должны удаляться все подзадачи
        manager.createEpic(epic);
        manager.createSubtask(subtask);
        URI uri = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode());
        List<Subtask> checkList = new ArrayList<>();
        assertEquals(checkList, manager.getAllSubtasks(), "Subtasks were not removed");
    }

    @Test
    void shouldRemoveSubtaskById() throws IOException, InterruptedException {        //Должна удаляться подзадача по Id
        manager.createEpic(epic);
        manager.createSubtask(subtask);
        manager.createSubtask(subtask2);
        URI uri = URI.create("http://localhost:8080/tasks/subtask/2");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode());
        List<Subtask> checkList;
        checkList = manager.getAllSubtasks();
        assertNull(manager.getSubtask(subtask.getId()), "Subtask was not removed");
        assertEquals(subtask2, checkList.get(0), "Removed subtask don`t match");
    }

    @Test
    void shouldProcessUnknownRequest() throws IOException, InterruptedException { //Должен обрабатывать неизвестный запрос
        URI uri = URI.create("http://localhost:8080/tasks/task/");
        String json = gson.toJson(task);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).PUT(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(405, response.statusCode());
    }

    @Test
    void shouldGetHistory() throws IOException, InterruptedException {       //Должен возвращать историю
        manager.createEpic(epic);
        manager.createSubtask(subtask);
        manager.getEpic(epic.getId());
        manager.getSubtask(subtask.getId());
        List<Task> historyList = manager.getHistory();
        URI uri = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Type taskType = new TypeToken<ArrayList<Subtask>>() {
        }.getType();
        List<Subtask> history = gson.fromJson(response.body(), taskType);
        assertNotNull(response.body(), "History don`t return");
        assertEquals(history.size(), historyList.size(), "Wrong size of the list");
        assertEquals(history.get(1), historyList.get(1), "Tasks don`t match");
    }

}

