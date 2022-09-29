package Tests;

import Tasks.*;
import Tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddAndRemoveTasksTest extends InMemoryTaskManagerTest{
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

}
