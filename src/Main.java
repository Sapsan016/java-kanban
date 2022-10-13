import KVServer.KVTaskClient;
import KVServer.KVServer;
import Managers.TaskManager;
import Managers.Managers;
import Managers.HTTPTaskManager;
import Tasks.Epic;
import Tasks.Status;
import Tasks.Subtask;
import Tasks.Task;
import com.google.gson.Gson;

import java.io.IOException;
import java.time.LocalDateTime;

import static KVServer.KVServer.PORT;


public class Main {

    protected static final Gson gson = Managers.getGson();

    public static void main(String[] args) throws IOException, InterruptedException {
        //   new KVServer().start();
        KVServer server = new KVServer();
        server.start();

       // KVTaskClient client = new KVTaskClient("http://localhost:" + PORT + "/register");

        Task task = new Task("Test NewTask1", "Test NewTask1 description", Status.NEW, 0,
                30, LocalDateTime.now());
        Task task2 = new Task("Test NewTask2", "Test NewTask2 description", Status.NEW, 1,
                20, LocalDateTime.now().plusMinutes(60));
        Epic epic = new Epic("Test NewEpic1", "Test NewEpic1 description", Status.NEW, 0,
                60, LocalDateTime.now());
        Epic epic2 = new Epic("Test NewEpic2", "Test NewEpic2 description", Status.NEW, 1,
                40, LocalDateTime.now().plusMinutes(90));
        Subtask subtask = new Subtask("Test NewSubtask1", "Test dNewSubtask1 description",
                Status.NEW, 0, 1, 25, LocalDateTime.now().plusMinutes(5));
        Subtask subtask2 = new Subtask("Test NewSubtask2", "Test dNewSubtask2 description",
                Status.NEW, 0, 1, 30, LocalDateTime.now().plusMinutes(60));



        HTTPTaskManager manager = Managers.getDefault();
       // manager.createTask(task);
       // manager.createTask(task2);
        manager.createEpic(epic);
        //manager.createEpic(epic2);
        String key = "tasks";
        String key2 = "epics";
        String key3 = "subtasks";
        String key4 = "history";
      //  manager.test(key);
      //  manager.test(key2);
        manager.createSubtask(subtask);
        manager.createSubtask(subtask2);
        manager.getEpic(1);
        manager.getSubtask(2);
        manager.getSubtask(3);
       // manager.test(key3);
        System.out.println(manager.getHistory());
       //manager.test(key4);










        server.stop();



    }
}
