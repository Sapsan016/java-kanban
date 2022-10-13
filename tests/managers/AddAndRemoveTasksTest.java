package managers;

import tasks.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class AddAndRemoveTasksTest extends InMemoryTaskManagerTest {
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
        List<Task> savedTasks = testManager.getAllTasks();
        assertNotNull(savedTasks, "Tasks don`t return.");
        assertEquals(2, savedTasks.size(), "Tasks amount is wrong."); //Проверяем количство
        Assertions.assertEquals(testTask1, savedTasks.get(0),                      //Проверяем соответствие
                "Tasks don`t match.");
        testManager.removeTaskById(testTask1.getId());                                 //Удаляем задачу 1
        savedTasks = testManager.getAllTasks();
        assertEquals(1, savedTasks.size(), "Tasks amount is wrong."); //Проверяем количство
        Assertions.assertEquals(testTask3, savedTasks.get(0),                      //Проверяем соответствие
                "Tasks don`t match.");
        testManager.removeAllTasks();
        savedTasks = testManager.getAllTasks();
        assertEquals(0, savedTasks.size(),                             //Проверяем что все задачи удалены
                "Tasks.Task`s map is NOT empty.");
    }

    @Test
    void shouldReturnAndRemoveEpics() {                                //Должны возвращаться и удаляться эпики
        testManager.createEpic(testEpic1);
        testManager.createEpic(testEpic2);
        List<Epic> savedEpics = testManager.getAllEpics();
        assertNotNull(savedEpics, "Epics don`t return.");
        assertEquals(2, savedEpics.size(), "Epics amount is wrong."); //Проверяем количство
        Assertions.assertEquals(testEpic1, savedEpics.get(0),                    //Проверяем соответствие
                "Epics don`t match.");
        testManager.removeEpicById(testEpic1.getId());                                //Удаляем эпик 1
        savedEpics = testManager.getAllEpics();
        assertEquals(1, savedEpics.size(), "Epics amount is wrong."); //Проверяем количство
        Assertions.assertEquals(testEpic2, savedEpics.get(0),                    //Проверяем соответствие
                "Epics don`t match.");
        testManager.removeAllEpics();
        savedEpics = testManager.getAllEpics();
        assertEquals(0, savedEpics.size(),                                   //Проверяем что эпики удалены
                "Epic`s map is NOT empty.");
    }

    @Test
    void shouldReturnAndRemoveSubtasks() {                             //Должны возвращаться и удаляться подзадачи
        testManager.createEpic(testEpic1);
        testManager.createSubtask(testSubtask1);
        testManager.createSubtask(testSubtask2);
        List<Subtask> savedSubtasks = testManager.getAllSubtasks();
        assertNotNull(savedSubtasks, "Subtasks don`t return.");
        assertEquals(2, savedSubtasks.size(), "Subtasks amount is wrong."); //Проверяем количство
        Assertions.assertEquals(testSubtask1, savedSubtasks.get(0),
                "Epics don`t match.");                                             //Проверяем соответствие
        testManager.removeSubtaskById(testSubtask2.getId());                               //Удаляем подзадачу 1
        savedSubtasks = testManager.getAllSubtasks();
        assertEquals(1, savedSubtasks.size(), "Subtasks amount is wrong."); //Проверяем количство
        Assertions.assertEquals(testSubtask1, savedSubtasks.get(0),
                "Epics don`t match.");                                             //Проверяем соответствие
        testManager.removeAllSubtasks();
        savedSubtasks = testManager.getAllSubtasks();
        assertEquals(0, savedSubtasks.size(),                                //Проверяем что задачи удалены
                "Tasks.Subtask`s map is NOT empty.");
    }

}
