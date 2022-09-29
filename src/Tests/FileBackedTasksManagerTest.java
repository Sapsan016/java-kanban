package Tests;

import Managers.*;
import Tasks.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends InMemoryTaskManagerTest {

    private static final Path path = Paths.get("testFile.csv");                //Путь для файла
    private static final String FILE_HEADER = "id,type,name,status,description,duration," +
            "startTime,epic`s/subtask`s id"; // Поле для заголовка

    @Override
    @BeforeEach
    void beforeEach() {
        super.beforeEach();
    }

    @Override
    @Test
    void shouldIncreaseIdToOne() {                                  //Id должен увеличиться до 1
        super.shouldIncreaseIdToOne();
    }

    @Test
    void shouldReturnSubtaskOfEpic() {                               //Должны возврщаться подзадачи эпика
        super.shouldReturnSubtaskOfEpic();
    }

    @Test
    void shouldUpdateTask() {                                   // Должен обновляться статус подзадачи
        super.shouldUpdateTask();
    }

    @Test
    void shouldUpdateEpic() {                                   // Должен обновляться статус эпика
        super.shouldUpdateEpic();
    }

    @Test
    void shouldUpdateSubtaskEpicInProgress() {//Должен изменить статус подзадачи и связанного с ней эпика на IN PROGRESS
        super.shouldUpdateSubtaskEpicInProgress();
    }

    @Test
    void shouldUpdateSubtaskEpicDone() {    //Должен изменяит статус подзадачи и связанного с ней эпика на DONE
        super.shouldUpdateSubtaskEpicDone();
    }

    @Test
    void ShouldRecoverManagerFromFile() {       //Должен восстанавливать менеджер из файла
        testManager.createEpic(testEpic1);     //Создаем задачи и заполяем историю
        testManager.createSubtask(testSubtask1);
        testManager.createTask(testTask1);
        testManager.getEpic(testEpic1.getId());
        testManager.getTask(testTask1.getId());
        testManager.getSubtask(testSubtask1.getId());
        List<Task> savedHistoryList = testManager.getHistory();

        FileBackedTasksManager newTestManager = FileBackedTasksManager.loadFromFile(path.toFile()); //Создаем новый менеджер
        newTestManager.fromString(newTestManager.readFromFile(path.toString()));
        newTestManager.recoveryHistory(FileBackedTasksManager.historyFromString
                (newTestManager.readFromFile(path.toString()))); //Восстанавливаем историю просмотров


        Assertions.assertEquals(savedHistoryList, newTestManager.getHistory(), "History Lists don`t match"); //Проверяем восстановление истории
        assertEquals(testTask1, newTestManager.tasks.get(testTask1.getId()), "Tasks.Task not found.");  //Проверяем восстановление задач
        assertEquals(testEpic1, newTestManager.epics.get(testEpic1.getId()), "Tasks.Task not found.");
        assertEquals(testSubtask1, newTestManager.subtasks.get(testSubtask1.getId()), "Tasks.Task not found.");
    }

    @Test
    void shouldSaveTask() {                                                       //Должен сохранять задачу в файл
        testManager.createTask(testTask1);
        String savedTask = testManager.readFromFile(String.valueOf(path)); //Получаем строку с задачей из файла
        String testString = FILE_HEADER + "\n" + testTask1.toString() + "\n" + "" + "\n";
        assertEquals(testString, savedTask);                    //Проверяем равенство тестовой строки и строки из файла
    }

    @Test
    void shouldSaveEpic() {                                          //Должен сохранять эпик вместе с подзадачей в файл
        testManager.createEpic(testEpic1);
        testManager.createSubtask(testSubtask1);
        String savedTask = testManager.readFromFile(String.valueOf(path)); //Получаем строку из файла
        String testString = FILE_HEADER + "\n" + testEpic1.toString() +
                "\n" + testSubtask1.toString() + "\n" + "" + "\n";
        assertEquals(testString, savedTask);            //Проверяем равенство тестовой строки и строки из файла
    }

    @Test
    void shouldRecoverTaskFromString() {              //Должен восстановить задачу из строки
        testManager.createTask(testTask1);
        String savedTaskString = testManager.readFromFile(String.valueOf(path)); //Получаем строку с задачей из файла
        testManager.removeTaskById(testTask1.getId());         //Удаляем задачу
        assertNull(testManager.getTask(testTask1.getId()));    //Проверяем, что задача удалена
        testManager.fromString(savedTaskString);               //Восстанавливаем задачу из строки
        assertEquals(testTask1, testManager.tasks.get(testTask1.id), "Tasks.Task not found."); //Проверяем соответствие
    }

    @Test
    void shouldRecoverEpicAndSubtaskFromString() {               //Должен восстановить эпик и подзадачу из строки
        testManager.createEpic(testEpic1);
        testManager.createSubtask(testSubtask1);
        String savedTaskEpic = testManager.readFromFile(String.valueOf(path)); //Получаем строку с эпиком из файла
        String savedSubtask = testManager.readFromFile(String.valueOf(path)); //Получаем строку с подзадачей из файла
        testManager.removeEpicById(testEpic1.getId());                 //Удаляем эпик вместе с подзадачей
        assertNull(testManager.getEpic(testEpic1.getId()));           //Проверяем, что эпик и подзадача удалены
        assertNull(testManager.getSubtask(testSubtask1.getId()));
        testManager.fromString(savedTaskEpic);                          //Восстанавливаем эпик и подзадачу из строки
        testManager.fromString(savedSubtask);
        assertEquals(testEpic1, testManager.epics.get(testEpic1.id), "Tasks.Task not found."); //Проверяем соответствие
        assertEquals(testSubtask1, testManager.subtasks.get(testSubtask1.getId()), "Tasks.Task not found.");
    }
}