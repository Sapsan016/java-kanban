package Tests;
import Tasks.*;
import Managers.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    public FileBackedTasksManager testManager;  //Тестовый менеджер

    //Тестовые задачи:
    protected Task testTask1 = new Task("Test NewTask1", "Test NewTask1 description", Status.NEW, 0,
            30, LocalDateTime.now().plusMinutes(90));

    protected Task testTask2 = new Task("Test NewTask2", "Test NewTask2 description", Status.NEW, 0,
            10, LocalDateTime.now());
    protected Task testTask3 = new Task("Test NewTask3", "Test NewTask3 description", Status.NEW, 0,
            10, LocalDateTime.now().plusMinutes(160));

    protected Epic testEpic1 = new Epic("Test NewEpic1", "Test NewEpic1 description", Status.NEW, 0,
            60, LocalDateTime.now());

    protected Epic testEpic2 = new Epic("Test NewEpic2", "Test NewEpic2 description", Status.NEW, 0,
            30, LocalDateTime.now().plusMinutes(30));

    protected Subtask testSubtask1 = new Subtask("Test NewSubtask1", "Test dNewSubtask1 description",
            Status.NEW, 0, 1, 25, LocalDateTime.now().plusMinutes(5));
    protected Subtask testSubtask2 = new Subtask("Test NewSubtask2", "Test NewSubtask description",
            Status.NEW, 0, 1, 10, LocalDateTime.now().plusMinutes(40));


    @BeforeEach
    void beforeEach() {                                     //Получаем новый менеджер для каждого теста
        this.testManager = Managers.getNewManager();
    }

    @Test
    void shouldValidateTimeIntersections() {              //Должен проверять задачи на пересечения по времени
        testManager.createEpic(testEpic1);                //Создаем задачи
        testManager.createTask(testTask2);
        assertEquals("The task has time intersection with existing task. Choose another start time",
                "The task has time intersection with existing task. Choose another start time");
    }

    @Test
    void shouldGetSortedListByStartTime() {                //Должен возвращать отсортированный по времени начала список
        testManager.createEpic(testEpic1);                //Создаем задачи
        testManager.createTask(testTask1);
        testManager.createSubtask(testSubtask1);
        testManager.createSubtask(testSubtask2);
        TreeSet<Task> testSet = testManager.getPrioritizedTasks(); // Получаем отсортированный список
        Assertions.assertEquals(testEpic1, testSet.first(), "Tasks don`t match");  //Сверяем первый и последний элементы
        Assertions.assertEquals(testTask1, testSet.last(), "Tasks don`t match");

    }

    @Test
    void shouldSetEpicsStartEndTime() {                          //Должен устанавливать время начала и окончания эпика
        testManager.createEpic(testEpic1);
        testManager.createSubtask(testSubtask1);
        Assertions.assertEquals(testEpic1.getStartTime(), testSubtask1.getStartTime(), //Проверяем время начала
                "Time don`t match");
        Assertions.assertEquals(testEpic1.getEndTime(), testSubtask1.getEndTime(),  //Проверяем время окончания
                "Time don`t match");
    }

    @Test
    void shouldIncreaseIdToOne() {                                        //Id должен увеличиться до 1
        Assertions.assertEquals(1, testManager.getId());
    }

    @Test
    void shouldAddTask() {                                                         //Должна быть добавлена задача
        testManager.createTask(testTask1);
        Task savedTask = testManager.getTask(testTask1.getId());
        Assertions.assertEquals(testTask1.getId(), savedTask.getId(), "Tasks.Task not found.");
        assertNull(testManager.getTask(0));                               //Проверяем возврат ноля с неверным ID
    }

    @Test
    void shouldAddEpic() {                                                   //Должен быть добавлен эпик
        testManager.createEpic(testEpic1);
        Epic savedEpic = testManager.getEpic(testEpic1.getId());
        Assertions.assertEquals(testEpic1.getId(), savedEpic.getId(), "Epic not found.");
        assertNull(testManager.getEpic(0));                               //Проверяем возврат ноля с неверным ID
    }

    @Test
    void shouldAddSubtask() {                                     //Должна быть добавлена подзадача
        testManager.createEpic(testEpic1);
        testManager.createSubtask(testSubtask1);
        Subtask savedSubtask = testManager.getSubtask(testSubtask1.getId());
        Assertions.assertEquals(testSubtask1.getId(), savedSubtask.getId(), "Tasks.Subtask not found.");
        assertNull(testManager.getSubtask(0));                   //Проверяем возврат ноля с неверным ID
        Assertions.assertEquals(savedSubtask.getEpicId(), testEpic1.getId(),   //Проверяем у подзадачи наличин Id эпика
                "Subtasks Id don`t match.");
    }

    @Test
    void shouldReturnAndRemoveTasks() {                            //Должны возвращаться и удаляться задачи
        testManager.createTask(testTask1);
        testManager.createTask(testTask3);
        HashMap<Integer, Task> savedTasks = testManager.getAllTasks();
        assertNotNull(savedTasks, "Tasks don`t return.");
        assertEquals(2, savedTasks.size(), "Tasks amount is wrong."); //Проверяем количство
        Assertions.assertEquals(testTask1, savedTasks.get(testTask1.getId()),                      //Проверяем соответствие
                "Tasks don`t match.");
        testManager.removeTaskById(testTask1.getId());                                 //Удаляем задачу 1
        assertEquals(1, savedTasks.size(), "Tasks amount is wrong."); //Проверяем количство
        Assertions.assertEquals(testTask3, savedTasks.get(testTask3.getId()),                      //Проверяем соответствие
                "Tasks don`t match.");
        testManager.removeAllTasks();
        assertEquals(0, savedTasks.size(),                             //Проверяем что все задачи удалены
                "Tasks.Task`s map is NOT empty.");
    }

    @Test
    void shouldReturnAndRemoveEpics() {                                //Должны возвращаться и удаляться эпики
        testManager.createEpic(testEpic1);
        testManager.createEpic(testEpic2);
        HashMap<Integer, Epic> savedEpics = testManager.getAllEpics();
        assertNotNull(savedEpics, "Epics don`t return.");
        assertEquals(2, savedEpics.size(), "Epics amount is wrong."); //Проверяем количство
        Assertions.assertEquals(testEpic1, savedEpics.get(testEpic1.getId()),                    //Проверяем соответствие
                "Epics don`t match.");
        testManager.removeEpicById(testEpic1.getId());                                //Удаляем эпик 1
        assertEquals(1, savedEpics.size(), "Epics amount is wrong."); //Проверяем количство
        Assertions.assertEquals(testEpic2, savedEpics.get(testEpic2.getId()),                    //Проверяем соответствие
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
        Assertions.assertEquals(testSubtask1, savedSubtasks.get(testSubtask1.getId()),
                "Epics don`t match.");                                             //Проверяем соответствие
        testManager.removeSubtaskById(testSubtask2.getId());                               //Удаляем подзадачу 1
        assertEquals(1, savedSubtasks.size(), "Subtasks amount is wrong."); //Проверяем количство
        Assertions.assertEquals(testSubtask1, savedSubtasks.get(testSubtask1.getId()),
                "Epics don`t match.");                                             //Проверяем соответствие
        testManager.removeAllSubtasks();
        assertEquals(0, savedSubtasks.size(),                                //Проверяем что задачи удалены
                "Tasks.Subtask`s map is NOT empty.");
    }

    @Test
    void shouldReturnSubtaskOfEpic() {                               //Должны возврщаться подзадачи эпика
        ArrayList<Object> checkList = new ArrayList<>();
        testManager.createEpic(testEpic1);
        testManager.createSubtask(testSubtask1);
        testManager.createSubtask(testSubtask2);
        checkList.add(testSubtask1);
        checkList.add(testSubtask2);
        Assertions.assertEquals(checkList, testManager.getEpicsSubtasks(testEpic1.id),  //Сравниваем список подзадач эпика и чеклист
                "Ids don`t match.");
        Assertions.assertEquals(testSubtask1.getEpicId(), testEpic1.getId(),
                "Subtasks Id don`t match.");                   // Проверяем наличие id эпика у подзадачи
        assertNull(testManager.getEpicsSubtasks(0));            //Проверяем, возврат нуля при неравильном id эпика
    }

    @Test
    void shouldUpdateTask() {                                   // Должен обновляться статус подзадачи
        testManager.createTask(testTask1);
        Assertions.assertEquals(Status.NEW, testTask1.getStatus(),              //Проверяем исходный статус
                "Tasks.Status don`t match");
        testTask1 = new Task("Test NewTask1", "Test NewTask1 description", Status.DONE, 0,
                30, LocalDateTime.now());
        testManager.updateTask(testTask1);
        Assertions.assertEquals(Status.DONE, testTask1.getStatus(),              //Проверяем изменение статуса
                "Tasks.Status don`t match");
    }

    @Test
    void shouldUpdateEpic() {                                   // Должен обновляться статус эпика
        testManager.createEpic(testEpic1);
        Assertions.assertEquals(Status.NEW, testEpic1.getStatus(),              //Проверяем исходный статус
                "Tasks.Status don`t match");
        testEpic1 = new Epic("Test NewEpic1", "Test NewEpic1 description", Status.DONE, 0,
                0, LocalDateTime.now().minusMinutes(30));
        testManager.updateEpic(testEpic1);
        Assertions.assertEquals(Status.DONE, testEpic1.getStatus(),              //Проверяем изменение статуса
                "Tasks.Status don`t match");
    }

    @Test
    void shouldReturnEmptySubtasksList() {                                 //Должен возвращать пустой список подзадач
        testManager.createEpic(testEpic2);
        Assertions.assertEquals(0, testEpic2.getSubtasksIds().size(),             //Проверяем, что список подзадач пуст
                "Epic`s subtasks list is not empty");
    }

    @Test
    void epicsStatusShouldBeNew() {    //Статус эпика должен быть NEW
        testManager.createEpic(testEpic1);
        testManager.createSubtask(testSubtask1);
        testManager.createSubtask(testSubtask2);
        Assertions.assertEquals(Status.NEW, testEpic1.getStatus(), "Tasks.Status don`t match"); //Проверяем статус эпика
    }

    @Test
    void epicsStatusShouldBeDone() {     //Статус эпика должен быть DONE
        testManager.createEpic(testEpic1);
        testSubtask1 = new Subtask("Test NewSubtask1", "Test dNewSubtask1 description", //Создаем две подзадачи,
                Status.DONE, 0, 1, 25, LocalDateTime.now().plusMinutes(5));            // обе со статусом DONE
        testSubtask2 = new Subtask("Test NewSubtask2", "Test NewSubtask description",
                Status.DONE, 0, 1, 10, LocalDateTime.now().plusMinutes(40));
        testManager.createSubtask(testSubtask1);
        testManager.createSubtask(testSubtask2);
        Assertions.assertEquals(Status.DONE, testEpic1.getStatus(), "Tasks.Status don`t match"); //Проверяем статус эпика
    }

    @Test
    void epicsStatusShouldBeInUpdateToInProgress() {                        //Статус эпика должен быть изменен на IN_PROGRESS
        testManager.createEpic(testEpic1);
        testSubtask1 = new Subtask("Test NewSubtask1", "Test dNewSubtask1 description", //Создаем две подзадачи,
                Status.DONE, 0, 1, 25, LocalDateTime.now().plusMinutes(5));            // одну со статусом DONE,
        testManager.createSubtask(testSubtask1);                                                       // другую со статусом NEW
        testManager.createSubtask(testSubtask2);
        Assertions.assertEquals(Status.IN_PROGRESS, testEpic1.getStatus(), "Tasks.Status don`t match");    //Проверяем статус эпика
    }

    @Test
    void epicsStatusShouldBeInProgress() {                                             //Статус эпика должен быть IN_PROGRESS
        testManager.createEpic(testEpic1);
        testSubtask1 = new Subtask("Test NewSubtask1", "Test dNewSubtask1 description", //Создаем две подзадачи,
                Status.IN_PROGRESS, 0, 1, 25, LocalDateTime.now().plusMinutes(5));   // обе со статусом IN_PROGRESS
        testSubtask2 = new Subtask("Test NewSubtask2", "Test NewSubtask description",
                Status.IN_PROGRESS, 0, 1, 10, LocalDateTime.now().plusMinutes(40));
        testManager.createSubtask(testSubtask1);
        testManager.createSubtask(testSubtask2);
        Assertions.assertEquals(Status.IN_PROGRESS, testEpic1.getStatus(), "Tasks.Status don`t match"); //Проверяем статус эпика
    }

    @Test
    void shouldUpdateSubtaskEpicInProgress() {//Должен изменяит статус подзадачи и связанного с ней эпика на IN PROGRESS
        testManager.createEpic(testEpic1);
        testManager.createSubtask(testSubtask1);

        Assertions.assertEquals(Status.NEW, testSubtask1.getStatus(),              //Проверяем исходный статус подзадачи1
                "Tasks.Status don`t match");

        testSubtask1 = new Subtask("Test NewSubtask1", "Test dNewSubtask1 description",
                Status.IN_PROGRESS, 2, 1, 25, LocalDateTime.now().plusMinutes(5));
        testManager.updateSubtask(testSubtask1);                       //Изменяем статус подзадачи1 IN PROGRESS
        Assertions.assertEquals(Status.IN_PROGRESS, testSubtask1.getStatus(),      //Проверяем изменение статуса подзадачи1
                "Tasks.Status don`t match");
        Assertions.assertEquals(Status.IN_PROGRESS, testEpic1.getStatus(),         //Проверяем изменение статуса эпика
                "Tasks.Status don`t match");
    }

    @Test
    void shouldUpdateSubtaskEpicDone() {    //Должен изменяит статус подзадачи и связанного с ней эпика на DONE
        testSubtask1 = new Subtask("Test NewSubtask1", "Test dNewSubtask1 description",
                Status.DONE, 0, 1, 25, LocalDateTime.now().plusMinutes(5));
        testSubtask2 = new Subtask("Test NewSubtask2", "Test NewSubtask description",
                Status.IN_PROGRESS, 0, 1, 10, LocalDateTime.now().plusMinutes(40));
        testManager.createEpic(testEpic1);
        testManager.createSubtask(testSubtask1);
        testManager.createSubtask(testSubtask2);
        Assertions.assertEquals(Status.IN_PROGRESS, testEpic1.getStatus(),         //Статус эпика НЕ должен  поменяться
                "Tasks.Status don`t match");
        testSubtask2 = new Subtask("Test NewSubtask2", "Test NewSubtask description",
                Status.DONE, 3, 1, 10, LocalDateTime.now().plusMinutes(40));
        testManager.updateSubtask(testSubtask2);
        Assertions.assertEquals(Status.DONE, testEpic1.getStatus(),         //Статус эпика должен поменяться на DONE
                "Tasks.Status don`t match");
    }
}
