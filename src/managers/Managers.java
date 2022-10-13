package managers;
import adapter.LocalDateTimeAdapter;
import kvServer.KVServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.time.LocalDateTime;

public class Managers {

    public static HTTPTaskManager getDefault() throws IOException, InterruptedException {
        return new HTTPTaskManager("http://localhost:" + KVServer.PORT + "/register");
    }
    public static FileBackedTasksManager getNewManager(){
        return new FileBackedTasksManager("testFile.csv");
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());

        return gsonBuilder.create();
    }
}
