import java.util.List;

public interface HistoryManager {       //Интерфейс менеджера истории просмотров

   void add(Task task);

   List<Task> getHistory();
}
