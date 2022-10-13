package managers;

import tasks.Task;

import java.util.List;

public interface HistoryManager {       //Интерфейс менеджера истории просмотров

   void add(Task task);     //Добавить задачу в историю

   void remove (int id);     //Удалить задачу из истории

   List<Task> getHistory();
}
