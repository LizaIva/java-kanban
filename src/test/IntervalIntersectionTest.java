package test;

import exception.IntervalIntersectionException;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IntervalIntersectionTest<T extends TaskManager> {
    protected TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
    }


    @Test
    void checkIntervalIntersectionInEmptyTaskTest() {
        assertDoesNotThrow(() -> taskManager.createTask("Test addNewTask", "Test addNewTask description",
                Duration.ofMinutes(10), LocalDateTime.of(2022, 10, 12, 14, 0)), "Задача не создана");
    }

    @Test
    void checkIntervalIntersectionIfNewTaskBeforeTest() {
        Task task = taskManager.createTask("Test addNewTask", "Test addNewTask description",
                Duration.ofMinutes(10), LocalDateTime.of(2022, 10, 12, 14, 0));

        Task task1 = taskManager.createTask("Test addNewTask1", "Test addNewTask1 description",
                Duration.ofMinutes(10), LocalDateTime.of(2022, 10, 12, 15, 0));

        assertDoesNotThrow(() -> taskManager.createTask("Test addNewTask2", "Test addNewTask2 description",
                        Duration.ofMinutes(10), LocalDateTime.of(2022, 10, 12, 13, 0)),
                "Задача была добавлена на занятый интервал времени");
    }

    @Test
    void checkIntervalIntersectionIfNewTaskIntersectsTaskTest() {
        Task task = taskManager.createTask("Test addNewTask", "Test addNewTask description",
                Duration.ofMinutes(10), LocalDateTime.of(2022, 10, 12, 14, 0));

        Task task1 = taskManager.createTask("Test addNewTask1", "Test addNewTask1 description",
                Duration.ofMinutes(10), LocalDateTime.of(2022, 10, 12, 15, 0));

        assertThrows(IntervalIntersectionException.class, () -> taskManager.createTask("Test addNewTask2", "Test addNewTask2 description",
                        Duration.ofMinutes(65), LocalDateTime.of(2022, 10, 12, 13, 0)),
                "Задача была добавлена на занятый интервал времени");
    }

    @Test
    void checkIntervalIntersectionIfNewTaskHasSameTimeIntervalTest() {
        Task task = taskManager.createTask("Test addNewTask", "Test addNewTask description",
                Duration.ofMinutes(10), LocalDateTime.of(2022, 10, 12, 14, 0));

        Task task1 = taskManager.createTask("Test addNewTask1", "Test addNewTask1 description",
                Duration.ofMinutes(10), LocalDateTime.of(2022, 10, 12, 15, 0));

        assertThrows(IntervalIntersectionException.class, () -> taskManager.createTask("Test addNewTask2", "Test addNewTask2 description",
                        Duration.ofMinutes(2), LocalDateTime.of(2022, 10, 12, 14, 1)),
                "Задача была добавлена на занятый интервал времени");
    }

    @Test
    void checkIntervalIntersectionIfNewTaskIntersectsEndOfTaskTest() {
        Task task = taskManager.createTask("Test addNewTask", "Test addNewTask description",
                Duration.ofMinutes(10), LocalDateTime.of(2022, 10, 12, 14, 0));

        Task task1 = taskManager.createTask("Test addNewTask1", "Test addNewTask1 description",
                Duration.ofMinutes(10), LocalDateTime.of(2022, 10, 12, 15, 0));

        assertThrows(IntervalIntersectionException.class, () -> taskManager.createTask("Test addNewTask2", "Test addNewTask2 description",
                        Duration.ofMinutes(10), LocalDateTime.of(2022, 10, 12, 14, 2)),
                "Задача была добавлена на занятый интервал времени");
    }

    @Test
    void checkIntervalIntersectionIfNewTaskTakesLongerThanTaskIntervalTest() {
        Task task = taskManager.createTask("Test addNewTask", "Test addNewTask description",
                Duration.ofMinutes(10), LocalDateTime.of(2022, 10, 12, 14, 0));

        Task task1 = taskManager.createTask("Test addNewTask1", "Test addNewTask1 description",
                Duration.ofMinutes(10), LocalDateTime.of(2022, 10, 12, 15, 0));

        assertThrows(IntervalIntersectionException.class, () -> taskManager.createTask("Test addNewTask2", "Test addNewTask2 description",
                        Duration.ofMinutes(40), LocalDateTime.of(2022, 10, 12, 13, 40)),
                "Задача была добавлена на занятый интервал времени");
    }

    @Test
    void checkIntervalIntersectionIfNewTaskIntersectsTwoTasksTest() {
        Task task = taskManager.createTask("Test addNewTask", "Test addNewTask description",
                Duration.ofMinutes(10), LocalDateTime.of(2022, 10, 12, 14, 0));

        Task task1 = taskManager.createTask("Test addNewTask1", "Test addNewTask1 description",
                Duration.ofMinutes(10), LocalDateTime.of(2022, 10, 12, 15, 0));

        assertThrows(IntervalIntersectionException.class, () -> taskManager.createTask("Test addNewTask2", "Test addNewTask2 description",
                        Duration.ofMinutes(60), LocalDateTime.of(2022, 10, 12, 14, 7)),
                "Задача была добавлена на занятый интервал времени");
    }

    @Test
    void checkIntervalIntersectionIfNewTaskBetweenTwoTask() {
        Task task = taskManager.createTask("Test addNewTask", "Test addNewTask description",
                Duration.ofMinutes(10), LocalDateTime.of(2022, 10, 12, 14, 0));

        Task task1 = taskManager.createTask("Test addNewTask1", "Test addNewTask1 description",
                Duration.ofMinutes(10), LocalDateTime.of(2022, 10, 12, 15, 0));

        assertDoesNotThrow(() -> taskManager.createTask("Test addNewTask2", "Test addNewTask2 description",
                        Duration.ofMinutes(20), LocalDateTime.of(2022, 10, 12, 14, 20)),
                "Задача была добавлена на занятый интервал времени");
    }

    @Test
    void checkIntervalIntersectionIfNewTaskIntersectsSecondTaskTest() {
        Task task = taskManager.createTask("Test addNewTask", "Test addNewTask description",
                Duration.ofMinutes(10), LocalDateTime.of(2022, 10, 12, 14, 0));

        Task task1 = taskManager.createTask("Test addNewTask1", "Test addNewTask1 description",
                Duration.ofMinutes(10), LocalDateTime.of(2022, 10, 12, 15, 0));

        assertThrows(IntervalIntersectionException.class, () -> taskManager.createTask("Test addNewTask2", "Test addNewTask2 description",
                        Duration.ofMinutes(10), LocalDateTime.of(2022, 10, 12, 14, 57)),
                "Задача была добавлена на занятый интервал времени");
    }

    @Test
    void checkIntervalIntersectionIfNewTaskTakesLongerThanSecondTaskIntervalTes() {
        Task task = taskManager.createTask("Test addNewTask", "Test addNewTask description",
                Duration.ofMinutes(10), LocalDateTime.of(2022, 10, 12, 14, 0));

        Task task1 = taskManager.createTask("Test addNewTask1", "Test addNewTask1 description",
                Duration.ofMinutes(10), LocalDateTime.of(2022, 10, 12, 15, 0));

        assertThrows(IntervalIntersectionException.class, () -> taskManager.createTask("Test addNewTask2", "Test addNewTask2 description",
                        Duration.ofMinutes(30), LocalDateTime.of(2022, 10, 12, 14, 57)),
                "Задача была добавлена на занятый интервал времени");
    }

    @Test
    void checkIntervalIntersectionIfNewTaskIntersectsEndOfSecondTaskTest() {
        Task task = taskManager.createTask("Test addNewTask", "Test addNewTask description",
                Duration.ofMinutes(10), LocalDateTime.of(2022, 10, 12, 14, 0));

        Task task1 = taskManager.createTask("Test addNewTask1", "Test addNewTask1 description",
                Duration.ofMinutes(10), LocalDateTime.of(2022, 10, 12, 15, 0));

        assertThrows(IntervalIntersectionException.class, () -> taskManager.createTask("Test addNewTask2", "Test addNewTask2 description",
                        Duration.ofMinutes(15), LocalDateTime.of(2022, 10, 12, 15, 2)),
                "Задача была добавлена на занятый интервал времени");
    }

    @Test
    void checkIntervalIntersectionIfNewTaskIncludesTwoTasksTest() {
        Task task = taskManager.createTask("Test addNewTask", "Test addNewTask description",
                Duration.ofMinutes(10), LocalDateTime.of(2022, 10, 12, 14, 0));

        Task task1 = taskManager.createTask("Test addNewTask1", "Test addNewTask1 description",
                Duration.ofMinutes(10), LocalDateTime.of(2022, 10, 12, 15, 0));

        assertThrows(IntervalIntersectionException.class, () -> taskManager.createTask("Test addNewTask2", "Test addNewTask2 description",
                        Duration.ofMinutes(120), LocalDateTime.of(2022, 10, 12, 13, 50)),
                "Задача была добавлена на занятый интервал времени");
    }

    @Test
    void checkIntervalIntersectionIfNewTaskIntersectsFirstAndSecondTasks() {
        Task task = taskManager.createTask("Test addNewTask", "Test addNewTask description",
                Duration.ofMinutes(10), LocalDateTime.of(2022, 10, 12, 14, 0));

        Task task1 = taskManager.createTask("Test addNewTask1", "Test addNewTask1 description",
                Duration.ofMinutes(10), LocalDateTime.of(2022, 10, 12, 15, 0));

        assertThrows(IntervalIntersectionException.class, () -> taskManager.createTask("Test addNewTask2", "Test addNewTask2 description",
                        Duration.ofMinutes(73), LocalDateTime.of(2022, 10, 12, 13, 50)),
                "Задача была добавлена на занятый интервал времени");
    }


    @Test
    void checkIntervalIntersectionInEmptySubtaskTest() {
        assertDoesNotThrow(() -> taskManager.createTask("Test addNewTask", "Test addNewTask description",
                Duration.ofMinutes(10), LocalDateTime.of(2022, 10, 12, 14, 0)), "Задача не создана");
    }

}
