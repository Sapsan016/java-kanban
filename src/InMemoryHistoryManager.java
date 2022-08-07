import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private final ArrayList<Task> historyList = new ArrayList<>(10); //Список просмотренных задач

    @Override
    public void add(Task task) {
        if (historyList.size() == 10) {                       //Проверяем, что в списке не более 10 элементов
            historyList.remove(0);
        }
        historyList.add(task);                                //Добавляем просмотренную задачу в историю
    }

    @Override
    public List<Task> getHistory() {                          //Возвращаем список истории просмотров
        return historyList;
    }
}
