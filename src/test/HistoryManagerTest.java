package test;

import manager.HistoryManager;
import manager.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {

    protected HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = Managers.getDefaultHistory();
    }
    @Test
    void addTest() {
        Task task = new Task();
        task.setId(0);

        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");

        Epic epic = new Epic();
        epic.setId(1);

        historyManager.add(epic);
        final List<Task> history1 = historyManager.getHistory();
        assertEquals(2, history1.size(), "Эпик не был добавлен.");
        assertEquals(history1.get(0), task, "Нарушен порядок истории вызова задач.");
        assertEquals(history1.get(1), epic, "Нарушен порядок истории вызова задач.");

        Subtask subtask = new Subtask();
        subtask.setEpicId(1);
        subtask.setId(2);

        historyManager.add(subtask);
        final List<Task> history2 = historyManager.getHistory();
        assertEquals(3, history2.size(), "Сабтаск не был добавлен.");
        assertEquals(history2.get(0), task, "Нарушен порядок истории вызова задач.");
        assertEquals(history2.get(1), epic, "Нарушен порядок истории вызова задач.");
        assertEquals(history2.get(2), subtask, "Нарушен порядок истории вызова задач.");

        historyManager.add(task);
        final List<Task> history3 = historyManager.getHistory();
        assertEquals(3, history3.size(), "Продублирована задача.");
        assertEquals(history3.get(0), epic, "Нарушен порядок истории вызова задач.");
        assertEquals(history3.get(1), subtask, "Нарушен порядок истории вызова задач.");
        assertEquals(history3.get(2), task, "Нарушен порядок истории вызова задач.");
    }

    @Test
    void removeTest(){
        Task task = new Task();
        task.setId(0);
        historyManager.add(task);

        Task task1 = new Task();
        task1.setId(1);
        historyManager.add(task1);

        Epic epic = new Epic();
        epic.setId(2);
        historyManager.add(epic);

        Subtask subtask = new Subtask();
        subtask.setEpicId(2);
        subtask.setId(3);
        historyManager.add(subtask);

        Subtask subtask1 = new Subtask();
        subtask1.setEpicId(2);
        subtask1.setId(4);
        historyManager.add(subtask1);

        Epic epic1 = new Epic();
        epic1.setId(5);
        historyManager.add(epic1);


        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(6, history.size(), "История не пустая.");
        assertEquals(history.get(0), task, "Нарушен порядок записи истории задач.");
        assertEquals(history.get(1), task1, "Нарушен порядок записи истории задач.");
        assertEquals(history.get(2), epic, "Нарушен порядок записи истории задач.");
        assertEquals(history.get(3), subtask, "Нарушен порядок записи истории задач.");
        assertEquals(history.get(4), subtask1, "Нарушен порядок записи истории задач.");
        assertEquals(history.get(5), epic1, "Нарушен порядок записи истории задач.");

        historyManager.remove(task.getId());
        final List<Task> history1 = historyManager.getHistory();
        assertEquals(5, history1.size(), "Не произошло удаление задачи.");
        assertEquals(history1.get(0), task1, "Нарушен порядок записи истории задач.");
        assertEquals(history1.get(1), epic, "Нарушен порядок записи истории задач.");
        assertEquals(history1.get(2), subtask, "Нарушен порядок записи истории задач.");
        assertEquals(history1.get(3), subtask1, "Нарушен порядок записи истории задач.");
        assertEquals(history1.get(4), epic1, "Нарушен порядок записи истории задач.");

        historyManager.remove(subtask.getId());
        final List<Task> history2 = historyManager.getHistory();
        assertEquals(4, history2.size(), "Не произошло удаление задачи.");
        assertEquals(history2.get(0), task1, "Нарушен порядок записи истории задач.");
        assertEquals(history2.get(1), epic, "Нарушен порядок записи истории задач.");
        assertEquals(history2.get(2), subtask1, "Нарушен порядок записи истории задач.");
        assertEquals(history2.get(3), epic1, "Нарушен порядок записи истории задач.");

        historyManager.remove(epic1.getId());
        final List<Task> history3 = historyManager.getHistory();
        assertEquals(3, history3.size(), "Не произошло удаление задачи.");
        assertEquals(history3.get(0), task1, "Нарушен порядок записи истории задач.");
        assertEquals(history3.get(1), epic, "Нарушен порядок записи истории задач.");
        assertEquals(history3.get(2), subtask1, "Нарушен порядок записи истории задач.");

        historyManager.remove(epic1.getId());
        final List<Task> history4 = historyManager.getHistory();
        assertEquals(3, history4.size(), "Не произошло удаление несуществующей задачи.");
    }
}
