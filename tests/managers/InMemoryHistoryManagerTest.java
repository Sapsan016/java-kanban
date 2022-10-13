package managers;

import tasks.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    protected static HistoryManager testHistoryManager = Managers.getDefaultHistory();
    protected Task testTask1 = new Task("Test NewTask1", "Test NewTask1 description", Status.NEW, 1,
            30, LocalDateTime.now());

    protected Task testTask2 = new Task("Test NewTask2", "Test NewTask2 description", Status.NEW, 2,
            100, LocalDateTime.now().plusHours(1));
    protected Epic testEpic1 = new Epic("Test NewEpic1", "Test NewEpic1 description", Status.NEW, 3,
            0,LocalDateTime.now().minusMinutes(30));
    @Test
    void shouldAddToHistoryWithoutDuplicates() {  //Должен добавлять задачу в историю без повторов
        testHistoryManager.add(testTask1);                       //Добавляем задачу в историю
        List<Task> history = testHistoryManager.getHistory();    //Получаем список истории
        assertNotNull(history, "History is empty.");      //Проверяем что история не пустая
        assertEquals(1, history.size(), "History size don`t match"); //Проверяем размер
        testHistoryManager.add(testTask1);                          //Повторно добавляем ту же задачу
        assertEquals(1, history.size(), "History size don`t match"); //Снова проверяем размер
    }

    @Test
    void shouldRemoveFromDifferentPositions() {  //Должен удалять задачи с разных позици в списке истории
        testHistoryManager.add(testTask1);    //Добавляем три разные истории
        testHistoryManager.add(testTask2);
        testHistoryManager.add(testEpic1);
        testHistoryManager.remove(2);   //Удаляем задачу из середины списка
        List<Task> history = testHistoryManager.getHistory();    //Получаем список истории
        assertEquals(2, history.size(), "History size don`t match"); //Проверяем размер
        testHistoryManager.remove(3);   //Удаляем задачу последнюю в списке
        history = testHistoryManager.getHistory();    //Получаем список истории
        assertEquals(1, history.size(), "History size don`t match"); //Проверяем размер
        testHistoryManager.remove(1);   //Удаляем оставгшуюся задачу
        history = testHistoryManager.getHistory();    //Получаем список истории
        assertEquals(0, history.size(), "History size don`t match"); //Проверяем размер
    }

}