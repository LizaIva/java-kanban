package test;

import manager.FileBackedTasksManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @BeforeEach
    void setUp() {
        File file = new File("/Users/elizavetaivanova/Prakticum/java-kanban/src/test/test_manager.csv");
        taskManager = Managers.loadFromFile(file);
    }

    @AfterEach
    void tearDown() {
        taskManager.removeAllTasks();
        taskManager.removeAllEpics();
    }

    @Test
    void writeToEmptyFileAndReadTest() {
        assertEquals(0, taskManager.getTasks().size(), "Неверное считывание файла." +
                " Обнаружены таски в пустом файле.");
        assertEquals(0, taskManager.getEpics().size(), "Неверное считывание файла." +
                " Обнаружены эпики в пустом файле.");

        Task task = taskManager.createTask("Test addNewTask", "Test addNewTask description",
                Duration.ofMinutes(5), LocalDateTime.of(2000, 1, 1, 12, 13));
        Epic epic = taskManager.createEpic("Test addNewEpic", "Test addNewEpic description");
        Subtask subtask = taskManager.createSubtask(epic.getId(), "Test addNewSubtask ",
                "Test addNewSubtask description", Duration.ofMinutes(5),
                LocalDateTime.of(2022, 10, 11, 14, 12));
        Epic epic1 = taskManager.createEpic("Test addNewEpic1", "Test addNewEpic description1");

        File file = new File("/Users/elizavetaivanova/Prakticum/java-kanban/src/test/test_manager.csv");
        TaskManager taskManager = Managers.loadFromFile(file);

        assertEquals(1, taskManager.getTasks().size(), "Неверное считывание файла." +
                " Обнаружены таски в пустом файле.");
        assertEquals(task.toString(), taskManager.getTasks().get(0).toString(), "Считался не тот файл, " +
                "который был записан");

        assertEquals(2, taskManager.getEpics().size(), "Неверное считывание файла." +
                " Обнаружены эпики в пустом файле.");
        assertEquals(epic.toString(), taskManager.getEpicById(epic.getId()).toString(), "Считался не тот файл, " +
                "который был записан");
        assertEquals(epic1.toString(), taskManager.getEpicById(epic1.getId()).toString(), "Считался не тот файл, " +
                "который был записан");

        assertEquals(1, taskManager.getSubtasks(epic.getId()).size(), "Неверное считывание файла." +
                " Обнаружены сабтаски в пустом файле.");
        assertEquals(subtask.toString(), taskManager.getSubtaskByIdEpic(epic.getId(), subtask.getId()).toString(), "Считался не тот файл, " +
                "который был записан");

        assertEquals(0, taskManager.getSubtasks(epic1.getId()).size(), "Неверное считывание файла." +
                " Обнаружены сабтаски у эпика, к которому не были добавлены сабтаски.");
    }

    @Test
    void emptyHistoryTest() {
        assertEquals(0, taskManager.getHistory().size(), "Неверный вывод истории в пустом файле.");

        Task task = taskManager.createTask("Test addNewTask", "Test addNewTask description",
                Duration.ofMinutes(5), LocalDateTime.of(2000, 1, 1, 12, 13));

        taskManager.getTaskById(task.getId());

        File file = new File("/Users/elizavetaivanova/Prakticum/java-kanban/src/test/test_manager.csv");
        TaskManager taskManager = Managers.loadFromFile(file);

        assertEquals(1, taskManager.getHistory().size(), "Неверный вывод истории файле.");
    }
}
