package managers;

import kvServer.KVTaskClient;
import tasks.*;
import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

public class HTTPTaskManager extends FileBackedTasksManager {

    protected KVTaskClient taskClient;
    protected Gson gson;

    public HTTPTaskManager(String url) throws IOException, InterruptedException {
        super(url);
        this.taskClient = new KVTaskClient(url);      //Создаем объект KVTaskClient
        this.gson = Managers.getGson();
    }

    @Override
    public void save() {                                //Сохраняем данные на KVServer
        String jsonTasks = gson.toJson(tasks);
        String jsonEpics = gson.toJson(epics);
        String jsonSubtasks = gson.toJson(subtasks);
        String jsonHistory = gson.toJson(historyToString(history));

        try {
            taskClient.put("tasks", jsonTasks);
            taskClient.put("epics", jsonEpics);
            taskClient.put("subtasks", jsonSubtasks);
            taskClient.put("history", jsonHistory);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void fromString(String key) throws IOException, InterruptedException { //Восстанавливаем данные из JSON строки
        int recoveredId = 0;                                  //Создаем переменную для воссстановлению последнего Id
        String jsonString = (taskClient.load(key));
        if (key.equals("tasks")) {                             //Задачи
            Type type = new TypeToken<Map<Integer, Task>>() {
            }.getType();
            tasks = gson.fromJson(jsonString, type);
            for (Map.Entry<Integer, Task> task : tasks.entrySet()) {
                int id1 = task.getValue().id;
                if (id1 > recoveredId) {
                    recoveredId = id1;
                }
            }
            this.id = recoveredId;
            return;
        }
        if (key.equals("epics")) {
            Type type = new TypeToken<Map<Integer, Epic>>() {   //Эпики
            }.getType();
            epics = gson.fromJson(jsonString, type);
            for (Map.Entry<Integer, Epic> epic : epics.entrySet()) {
                int id1 = epic.getValue().id;
                if (id1 > recoveredId) {
                    recoveredId = id1;
                }
            }
            this.id = recoveredId;
            return;
        }
        if (key.equals("subtasks")) {
            Type type = new TypeToken<Map<Integer, Subtask>>() { //Подзадачи
            }.getType();
            subtasks = gson.fromJson(jsonString, type);
            for (Map.Entry<Integer, Subtask> subtask : subtasks.entrySet()) {
                int id1 = subtask.getValue().id;
                if (id1 > recoveredId) {
                    recoveredId = id1;
                }
            }
            this.id = recoveredId;
            return;
        }
        if (key.equals("history")) {            //Историю просмотров
            ArrayList<Integer> newHistory = new ArrayList<>();
            jsonString = jsonString.replaceAll("\"", "");
            String[] lines = jsonString.split(",");
            for (String line : lines) {
                newHistory.add(Integer.valueOf(line));
            }
            recoveryHistory(newHistory);
        }
    }
}