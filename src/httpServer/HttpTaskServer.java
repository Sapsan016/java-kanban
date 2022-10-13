package httpServer;

import managers.Managers;
import managers.TaskManager;
import tasks.Epic;

import tasks.Subtask;
import tasks.Task;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {

    public static final int PORT = 8080;

    private final HttpServer server;

    private final Gson gson;

    private final TaskManager manager;

    public HttpTaskServer(TaskManager manager) throws IOException {
        this.manager = manager;
        gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks/task", new TaskHandler());  //Подключаем обработчик для задач
        server.createContext("/tasks/epic", new EpicHandler());  //Подключаем обработчик для эпиков
        server.createContext("/tasks/subtask", new SubtaskHandler()); //Подключаем обработчик для подзадач
        server.createContext("/tasks/history", new HistoryHandler());  //Подключаем Обработчик для истории
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer server = new HttpTaskServer(Managers.getNewManager());
        server.start();
    }
    class TaskHandler implements HttpHandler {      //Обработчик для задач
        @Override
        public void handle(HttpExchange exchange) {
            try {
                String requestMethod = exchange.getRequestMethod();
                String path = exchange.getRequestURI().getPath();
                switch (requestMethod) {
                    case "GET": {                                                                 //Отрабатываем метод GET
                        if (Pattern.matches("^/tasks/task/\\d+$", path)) {
                            int id = parsePathId(path.replaceFirst("/tasks/task/", ""));//Получаем id задачи
                            if (id != -1) {
                                String response = gson.toJson(manager.getTask(id), Task.class);   //Получаем задачу по id
                                sendText(exchange, response);
                                return;
                            }
                        }
                        String response = gson.toJson(manager.getAllTasks());       //Получаем все задачи
                        sendText(exchange, response);
                        return;
                    }
                    case "POST": {                                                       //Отрабатываем метод POST
                        String body = readText(exchange);                                //Считываем и десериализуем
                        Task task = gson.fromJson(body, Task.class);                     //тело запроса
                        if (task.id == 0) {                                                      //Если задача новая
                            manager.createTask(task);                                          //Добавляем задачу
                            System.out.println("The new task with id = " + task.id + " was added");
                            exchange.sendResponseHeaders(201, 0);
                            return;
                        }
                        manager.updateTask(task);                                             //Обновляем существующую
                        System.out.println("The task with id = " + task.id + " was updated");  //задачу
                        exchange.sendResponseHeaders(201, 0);
                        return;
                    }
                    case "DELETE": {
                        if (Pattern.matches("^/tasks/task/\\d+$", path)) {
                            int id = parsePathId(path.replaceFirst("/tasks/task/", ""));
                            if (id != -1) {
                                manager.removeTaskById(id);                                   //Удаляем задачу по ID
                                System.out.println("The task with id = " + id + " was removed");
                                exchange.sendResponseHeaders(204, -1);
                                return;
                            }
                        }
                        manager.removeAllTasks();                                          //Удаляем все задачи
                        System.out.println("All tasks were removed");
                        exchange.sendResponseHeaders(204, -1);
                        return;
                    }
                    default: {
                        System.out.println("Unknown request: " + requestMethod);
                        exchange.sendResponseHeaders(405, 0);
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            } finally {
                exchange.close();
            }
        }
    }

    class EpicHandler implements HttpHandler {  //Обработчик для эпиков
        @Override
        public void handle(HttpExchange exchange) {
            String requestMethod = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            try {
                switch (requestMethod) {
                    case "GET": {
                        if (Pattern.matches("^/tasks/epic$", path)) {
                            String response = gson.toJson(manager.getAllEpics());       //Получаем все эпики
                            sendText(exchange, response);
                            return;
                        }
                        if (Pattern.matches("^/tasks/epic/\\d+$", path)) {
                            int id = parsePathId(path.                                   //Получаем id эпика
                                    replaceFirst("/tasks/epic/", ""));
                            if (id != -1) {
                                String response = gson.toJson(manager.getEpic(id));     //Получаем эпик по id
                                sendText(exchange, response);
                                return;
                            }
                        }
                        if (Pattern.matches("^/tasks/epic/\\d+/subtask$", path)) {
                            int id = parsePathId(path.                                   //Получаем id эпика
                                    replaceFirst("/tasks/epic/", "")
                                    .replaceFirst("/subtask", ""));
                            if (id != -1) {
                                String response = gson.toJson(manager.getEpicsSubtasks(id));  //Получаем подзадачи эпика
                                sendText(exchange, response);
                                return;
                            }
                        }
                    }
                    case "POST": {                                                    //Отрабатываем метод POST
                        String body = readText(exchange);                            //Считываем и десериализуем
                        Epic epic = gson.fromJson(body, Epic.class);                 //тело запроса
                        if (epic.id == 0) {                                                      //Если эпик новый
                            manager.createEpic(epic);                                            //Добавляем эпик
                            System.out.println("The new epic with id = " + epic.id + " was added");
                            exchange.sendResponseHeaders(201, 0);
                            return;
                        }
                        manager.updateEpic(epic);                                             //Обновляем существующий
                        System.out.println("The epic with id = " + epic.id + " was updated");  //эпик
                        exchange.sendResponseHeaders(201, 0);
                        return;
                    }
                    case "DELETE": {
                        if (Pattern.matches("^/tasks/epic/\\d+$", path)) {
                            int id = parsePathId(path.replaceFirst("/tasks/epic/", ""));
                            if (id != -1) {
                                manager.removeEpicById(id);                                   //Удаляем эпик по ID
                                System.out.println("The epic with id = " + id + " was removed");
                                exchange.sendResponseHeaders(204, -1);
                                return;
                            }
                        }
                        manager.removeAllEpics();                                          //Удаляем все эпики
                        System.out.println("All epics were removed");
                        exchange.sendResponseHeaders(204, -1);
                        return;
                    }

                    default: {
                        System.out.println("Unknown request: " + requestMethod);
                        exchange.sendResponseHeaders(405, 0);
                    }
                }

            } catch (Exception exception) {
                exception.printStackTrace();
            } finally {
                exchange.close();
            }
        }
    }

    class SubtaskHandler implements HttpHandler {             //Обработчик для подзадач
        @Override
        public void handle(HttpExchange exchange) {
            String requestMethod = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            try {
                switch (requestMethod) {
                    case "GET": {
                        if (Pattern.matches("^/tasks/subtask$", path)) {
                            String response = gson.toJson(manager.getAllSubtasks());       //Получаем все подзадачи
                            sendText(exchange, response);
                            return;
                        }
                        if (Pattern.matches("^/tasks/subtask/\\d+$", path)) {
                            int id = parsePathId(path.                                   //Получаем id подзадачи
                                    replaceFirst("/tasks/subtask/", ""));
                            if (id != -1) {
                                String response = gson.toJson(manager.getSubtask(id));     //Получаем подзадачу по id
                                sendText(exchange, response);
                                return;
                            }
                        }
                    }
                    case "POST": {                                                    //Отрабатываем метод POST
                        String body = readText(exchange);                            //Считываем и десериализуем
                        Subtask subtask = gson.fromJson(body, Subtask.class);                 //тело запроса
                        if (subtask.id == 0) {                                                   //Если подзадача новая
                            manager.createSubtask(subtask);                                        //Добавляем подзадчу
                            System.out.println("The new subtask with id = " + subtask.id + " was added");
                            exchange.sendResponseHeaders(201, 0);
                            return;
                        }
                        manager.updateSubtask(subtask);                                        //Обновляем существующую
                        System.out.println("The subtask with id = " + subtask.id + " was updated");  //подзадачу
                        exchange.sendResponseHeaders(201, 0);
                        return;
                    }
                    case "DELETE": {
                        if (Pattern.matches("^/tasks/subtask/\\d+$", path)) {
                            int id = parsePathId(path.replaceFirst("/tasks/subtask/", ""));
                            if (id != -1) {
                                manager.removeSubtaskById(id);                                //Удаляем подзадачу по ID
                                System.out.println("The subtask with id = " + id + " was removed");
                                exchange.sendResponseHeaders(204, -1);
                                return;
                            }
                        }
                        manager.removeAllSubtasks();                                          //Удаляем все подзадачи
                        System.out.println("All subtasks were removed");
                        exchange.sendResponseHeaders(204, -1);
                        return;
                    }
                    default: {
                        System.out.println("Unknown request: " + requestMethod);
                        exchange.sendResponseHeaders(405, 0);
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            } finally {
                exchange.close();
            }
        }
    }

    class HistoryHandler implements HttpHandler { //Обработчик истории
        @Override
        public void handle(HttpExchange exchange) {
            try {
                String response = gson.toJson(manager.getHistory());       //Получаем все задачи
                sendText(exchange, response);
            } catch (Exception exception) {
                exception.printStackTrace();
            } finally {
                exchange.close();
            }
        }
    }

    private int parsePathId(String pathId) {                    //Парсим путь в ID
        try {
            return Integer.parseInt(pathId);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void start() {
        System.out.println("Launch server on port " + PORT);
        System.out.println("Open in browser http://localhost:" + PORT + "/");
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Server stopped on port" + PORT);
    }

    protected String readText(HttpExchange h) throws IOException {   //Метод для чтения запроса
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void sendText(HttpExchange h, String text) throws IOException { //Метод для записи запроса
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }
}
