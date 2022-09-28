import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest<T extends TaskManager> {
    protected static TaskManager testManager;
    protected static Task testTask1 = new Task("Test NewTask1", "Test NewTask1 description", Status.NEW, 0);

    protected static Task testTask2 = new Task("Test NewTask2", "Test NewTask2 description", Status.NEW, 0);
    protected static Epic testEpic1 = new Epic("Test NewEpic1", "Test NewEpic1 description", Status.NEW, 0);

    protected static Epic testEpic2 = new Epic("Test NewEpic2", "Test NewEpic2 description", Status.NEW, 0);

    protected static Subtask testSubtask1 = new Subtask("Test NewSubtask1", "Test dNewSubtask1 description",
            Status.NEW, 0, 1);
    protected static Subtask testSubtask2 = new Subtask("Test NewSubtask2", "Test NewSubtask description",
            Status.NEW, 0, 1);

    @BeforeEach
    void beforeEach() {
        testManager = Managers.getDefault();
    }

    @Test
    void shouldIncreaseIdToOne() {                             //Id должен увеличиться до 1
        assertEquals(1, testManager.getId());
    }

    @Test
    void shouldAddTask() {                                          //Должна быть добавлена задача
        testManager.createTask(testTask1);
        Task savedTask = testManager.getTask(testTask1.getId());
        assertEquals(testTask1.getId(), savedTask.getId(), "Task not found.");
        assertNull(testManager.getTask(0));        //Проверяем возврат ноля с неверным ID
    }

    @Test
    void shouldAddEpic() {                                          //Должен быть добавлен эпик
        testManager.createEpic(testEpic1);
        Epic savedEpic = testManager.getEpic(testEpic1.getId());
        assertEquals(testEpic1.getId(), savedEpic.getId(), "Epic not found.");
        assertNull(testManager.getEpic(0));        //Проверяем возврат ноля с неверным ID
    }

    @Test
    void shouldAddSubtask() {                                     //Должна быть добавлена подзадача
        testManager.createEpic(testEpic1);
        testManager.createSubtask(testSubtask1);
        Subtask savedSubtask = testManager.getSubtask(testSubtask1.getId());
        assertEquals(testSubtask1.getId(), savedSubtask.getId(), "Subtask not found.");
        assertNull(testManager.getSubtask(0));        //Проверяем возврат ноля с неверным ID
        assertEquals(savedSubtask.getEpicId(), testEpic1.getId(),   //Проверяем у подзадачи наличин Id эпика
                "Subtasks Id don`t match.");
        testManager.removeAllSubtasks();
    }

    @Test
    void shouldReturnAndRemoveTasks() {                            //Должны возвращаться и удаляться задачи
        testManager.createTask(testTask1);
        testManager.createTask(testTask2);
        HashMap<Integer, Task> savedTasks = testManager.getAllTasks();
        assertNotNull(savedTasks, "Tasks don`t return.");
        assertEquals(2, savedTasks.size(), "Tasks amount is wrong."); //Проверяем количство
        assertEquals(testTask1, savedTasks.get(testTask1.getId()),                      //Проверяем соответствие
                "Tasks don`t match.");
        testManager.removeTaskById(testTask1.getId());                                 //Удаляем задачу 1
        assertEquals(1, savedTasks.size(), "Tasks amount is wrong."); //Проверяем количство
        assertEquals(testTask2, savedTasks.get(testTask2.getId()),                      //Проверяем соответствие
                "Tasks don`t match.");
        testManager.removeAllTasks();
        assertEquals(0, savedTasks.size(),                             //Проверяем что все задачи удалены
                "Task`s map is NOT empty.");
    }

    @Test
    void shouldReturnAndRemoveEpics() {                                //Должны возвращаться и удаляться эпики
        testManager.createEpic(testEpic1);
        testManager.createEpic(testEpic2);
        HashMap<Integer, Epic> savedEpics = testManager.getAllEpics();
        assertNotNull(savedEpics, "Epics don`t return.");
        assertEquals(2, savedEpics.size(), "Epics amount is wrong."); //Проверяем количство
        assertEquals(testEpic1, savedEpics.get(testEpic1.getId()),                    //Проверяем соответствие
                "Epics don`t match.");
        testManager.removeEpicById(testEpic1.getId());                                //Удаляем эпик 1
        assertEquals(1, savedEpics.size(), "Epics amount is wrong."); //Проверяем количство
        assertEquals(testEpic2, savedEpics.get(testEpic2.getId()),                    //Проверяем соответствие
                "Epics don`t match.");
        testManager.removeAllEpics();
        assertEquals(0, savedEpics.size(),                                   //Проверяем что эпики удалены
                "Epic`s map is NOT empty.");
    }

    @Test
    void shouldReturnAndRemoveSubtasks() {                             //Должны возвращаться и удаляться подзадачи
        testManager.createEpic(testEpic1);
        testManager.createSubtask(testSubtask1);
        testManager.createSubtask(testSubtask2);
        HashMap<Integer, Subtask> savedSubtasks = testManager.getAllSubtasks();
        assertNotNull(savedSubtasks, "Subtasks don`t return.");
        assertEquals(2, savedSubtasks.size(), "Subtasks amount is wrong."); //Проверяем количство
        assertEquals(testSubtask1, savedSubtasks.get(testSubtask1.getId()),
                "Epics don`t match.");                                             //Проверяем соответствие
        testManager.removeSubtaskById(testSubtask2.getId());                               //Удаляем подзадачу 1
        assertEquals(1, savedSubtasks.size(), "Subtasks amount is wrong."); //Проверяем количство
        assertEquals(testSubtask1, savedSubtasks.get(testSubtask1.getId()),
                "Epics don`t match.");                                             //Проверяем соответствие
        testManager.removeAllSubtasks();
        assertEquals(0, savedSubtasks.size(),                                //Проверяем что задачи удалены
                "Subtask`s map is NOT empty.");
    }

    @Test
    void shouldReturnSubtaskOfEpic() {                               //Должны возврщаться подзадачи эпика
        ArrayList<Object> checkList = new ArrayList<>();
        testManager.createEpic(testEpic1);
        testManager.createSubtask(testSubtask1);
        testManager.createSubtask(testSubtask2);
        checkList.add(testSubtask1);
        checkList.add(testSubtask2);
        assertEquals(checkList, testManager.getEpicsSubtasks(testEpic1.id),  //Сравниваем список подзадач эпика и чеклист
                "Ids don`t match.");
        assertEquals(testSubtask1.getEpicId(), testEpic1.getId(),
                "Subtasks Id don`t match.");                   // Проверяем наличие id эпика у подзадачи
        assertNull(testManager.getEpicsSubtasks(0));            //Проверяем, возврат нуля при неравильном id эпика
        testManager.removeAllSubtasks();
    }

    @Test
    void shouldUpdateTask() {                                   // Должен обновляться статус подзадачи
        testManager.createTask(testTask1);
        assertEquals(Status.NEW, testTask1.getStatus(),              //Проверяем исходный статус
                "Status don`t match");
        testTask1 = new Task("Test NewTask1", "Test NewTask1 description", Status.DONE, testTask1.getId());
        testManager.updateTask(testTask1);
        assertEquals(Status.DONE, testTask1.getStatus(),              //Проверяем изменение статуса
                "Status don`t match");
    }

    @Test
    void shouldUpdateEpic() {                                   // Должен обновляться статус эпика
        testManager.createEpic(testEpic1);
        assertEquals(Status.NEW, testEpic1.getStatus(),              //Проверяем исходный статус
                "Status don`t match");
        testEpic1 = new Epic("Test NewEpic1", "Test NewEpic1 description", Status.DONE, testEpic1.getId());
        testManager.updateEpic(testEpic1);
        assertEquals(Status.DONE, testEpic1.getStatus(),              //Проверяем изменение статуса
                "Status don`t match");
    }

    @Test
    void shouldUpdateSubtaskEpicInProgress() {//Должен изменяит статус подзадачи и связанного с ней эпика на IN PROGRESS
        testManager.createEpic(testEpic1);
        testManager.createSubtask(testSubtask1);

        assertEquals(Status.NEW, testSubtask1.getStatus(),              //Проверяем исходный статус подзадачи1
                "Status don`t match");

        testSubtask1 = new Subtask("Test NewSubtask1", "Test dNewSubtask1 description",
                Status.IN_PROGRESS, testSubtask1.getId(), 1);
        testManager.updateSubtask(testSubtask1);                       //Изменяем статус подзадачи1 IN PROGRESS
        assertEquals(Status.IN_PROGRESS, testSubtask1.getStatus(),      //Проверяем изменение статуса подзадачи1
                "Status don`t match");
        assertEquals(Status.IN_PROGRESS, testEpic1.getStatus(),         //Проверяем изменение статуса эпика
                "Status don`t match");
        testManager.removeAllSubtasks();
    }

    @Test
    void shouldUpdateSubtaskEpicDone() {    //Должен изменяит статус подзадачи и связанного с ней эпика на DONE
        testSubtask1 = new Subtask("Test NewSubtask1", "Test dNewSubtask1 description",
                Status.DONE, 0, 1);
        testSubtask2 = new Subtask("Test NewSubtask2", "Test NewSubtask description",
                Status.IN_PROGRESS, 0, 1);
        testManager.createEpic(testEpic1);
        testManager.createSubtask(testSubtask1);
        testManager.createSubtask(testSubtask2);
        assertEquals(Status.IN_PROGRESS, testEpic1.getStatus(),         //Статус эпика НЕ должен  поменяться
                "Status don`t match");
        testSubtask2 = new Subtask("Test NewSubtask2", "Test NewSubtask description",
                Status.DONE, testSubtask2.getId(), 1);
        testManager.updateSubtask(testSubtask2);
        assertEquals(Status.DONE, testEpic1.getStatus(),         //Статус эпика должен поменяться на DONE
                "Status don`t match");
        testManager.removeAllSubtasks();
    }

    @Test
    void getHistory() {
    }
}
