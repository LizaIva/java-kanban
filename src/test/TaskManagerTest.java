package test;

import manager.TaskManager;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static task.Status.*;
import static task.Status.IN_PROGRESS;
import static task.Type.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected TaskManager taskManager;

    @Test
    void addNewTask() {
        final Task task = taskManager.createTask("Test addNewTask", "Test addNewTask description",
                Duration.ofMinutes(5), LocalDateTime.of(2000, 1, 1, 12, 13));

        final Task savedTask = taskManager.getTaskById(task.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");

        final Task task1 = taskManager.createTask("Test addNewTask2", "Test addNewTask2 description",
                Duration.ofMinutes(5), LocalDateTime.of(2001, 2, 1, 12, 13));
        final Task savedTask1 = taskManager.getTaskById(task1.getId());

        assertNotNull(savedTask1, "Задача не найдена.");
        assertEquals(task1, savedTask1, "Задачи не совпадают.");

        final List<Task> tasks1 = taskManager.getTasks();

        assertNotNull(tasks1, "Задачи на возвращаются.");
        assertEquals(2, tasks1.size(), "Неверное количество задач.");
        assertEquals(task1, tasks1.get(1), "Задачи не совпадают.");
    }


    @Test
    void addNewEpic() {
        final Epic epic = taskManager.createEpic("Test addNewEpic", "Test addNewEpic description"
        );

        final Epic savedEpic = taskManager.getEpicById(epic.getId());

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Эпики на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");

        final Epic epic1 = taskManager.createEpic("Test addNewEpic1", "Test addNewEpic description1"
        );

        final Epic savedEpic1 = taskManager.getEpicById(epic1.getId());

        assertNotNull(savedEpic1, "Эпик не найден.");
        assertEquals(epic1, savedEpic1, "Эпики не совпадают.");

        final List<Epic> epics1 = taskManager.getEpics();

        assertNotNull(epics1, "Эпики на возвращаются.");
        assertEquals(2, epics1.size(), "Неверное количество эпиков.");
        assertEquals(epic1, epics1.get(1), "Эпики не совпадают.");

    }


    @Test
    void addNewSubtask() {
        Epic epic = taskManager.createEpic("Test addNewEpic", "Test addNewEpic description"
        );

        Subtask subtask = taskManager.createSubtask(epic.getId(), "Test addNewSubtask ",
                "Test addNewSubtask description", Duration.ofMinutes(5),
                LocalDateTime.of(2022, 10, 11, 14, 12));

        final Subtask savedSubtask = taskManager.getSubtaskByIdEpic(epic.getId(), subtask.getId());

        assertNotNull(savedSubtask, "Сабтаск не найден.");
        assertEquals(subtask, savedSubtask, "Сабтаск не совпадают.");

        final List<Subtask> subtasks = taskManager.getSubtasks(epic.getId());

        assertNotNull(subtasks, "Сабтаски на возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество сабтасков.");
        assertEquals(subtask, subtasks.get(0), "Сабтаски не совпадают.");

        Subtask subtask1 = taskManager.createSubtask(epic.getId(), "Test addNewSubtask ",
                "Test addNewSubtask description", Duration.ofMinutes(5),
                LocalDateTime.of(2022, 10, 11, 14, 12));

        final Subtask savedSubtask1 = taskManager.getSubtaskByIdEpic(epic.getId(), subtask1.getId());

        assertNotNull(savedSubtask1, "Сабтаск не найден.");
        assertEquals(subtask1, savedSubtask1, "Сабтаск не совпадают.");

        final List<Subtask> subtasks1 = taskManager.getSubtasks(epic.getId());

        assertNotNull(subtasks1, "Сабтаски на возвращаются.");
        assertEquals(2, subtasks1.size(), "Неверное количество сабтасков.");
        assertEquals(subtask1, subtasks1.get(1), "Сабтаски не совпадают.");
    }

    @Test
    void removeAllTasks() {
        final Task task = taskManager.createTask("Test addNewTask", "Test addNewTask description",
                Duration.ofMinutes(5), LocalDateTime.of(2000, 1, 1, 12, 13));

        final Task task1 = taskManager.createTask("Test addNewTask1", "Test addNewTask1 description",
                Duration.ofMinutes(5), LocalDateTime.of(2001, 1, 1, 12, 13));

        final Task task2 = taskManager.createTask("Test addNewTask2", "Test addNewTask description2",
                Duration.ofMinutes(5), LocalDateTime.of(2002, 1, 1, 12, 13));

        List<Task> tasks = taskManager.getTasks();

        assertEquals(3, tasks.size(), "Неверное количество задач.");

        taskManager.removeAllTasks();
        List<Task> tasks2 = taskManager.getTasks();

        assertEquals(0, tasks2.size(), "Неверное количество задач.");
    }

    @Test
    void removeAllEpics() {
        Epic epic = taskManager.createEpic("Test addNewEpic", "Test addNewEpic description"
        );

        Epic epic1 = taskManager.createEpic("Test addNewEpic1", "Test addNewEpic1 description"
        );

        Epic epic2 = taskManager.createEpic("Test addNewEpic2", "Test addNewEpic2 description"
        );

        List<Epic> epics = taskManager.getEpics();

        assertEquals(3, epics.size(), "Неверное количество эпиков.");

        taskManager.removeAllEpics();
        List<Epic> epics2 = taskManager.getEpics();

        assertEquals(0, epics2.size(), "Неверное количество эпиков.");
    }

    @Test
    void removeAllEpicSubtasks() {
        Epic epic = taskManager.createEpic("Test addNewEpic", "Test addNewEpic description"
        );

        Subtask subtask1 = taskManager.createSubtask(epic.getId(), "Test addNewSubtask ",
                "Test addNewSubtask description", Duration.ofMinutes(5),
                LocalDateTime.of(2022, 10, 11, 14, 12));

        Subtask subtask2 = taskManager.createSubtask(epic.getId(), "Test addNewSubtask2 ",
                "Test addNewSubtask description2", Duration.ofMinutes(7),
                LocalDateTime.of(2022, 10, 12, 14, 12));

        Subtask subtask3 = taskManager.createSubtask(epic.getId(), "Test addNewSubtask3",
                "Test addNewSubtask description3", Duration.ofMinutes(5),
                LocalDateTime.of(2022, 10, 11, 15, 12));


        List<Epic> epics = taskManager.getEpics();
        List<Subtask> subtasks = taskManager.getSubtasks(epic.getId());

        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(3, subtasks.size(), "Неверное количество сабтасков.");

        taskManager.removeAllEpicSubtasks(epic.getId());
        List<Epic> epics2 = taskManager.getEpics();
        List<Subtask> subtasks2 = taskManager.getSubtasks(epic.getId());

        assertEquals(1, epics2.size(), "Неверное количество эпиков.");
        assertEquals(0, subtasks2.size(), "Неверное количество сабтасков.");

    }

    @Test
    void removeAllEpicSubtasksWithWrongEpicId() {
        Epic epic = taskManager.createEpic("Test addNewEpic", "Test addNewEpic description"
        );

        Subtask subtask1 = taskManager.createSubtask(epic.getId(), "Test addNewSubtask ",
                "Test addNewSubtask description", Duration.ofMinutes(5),
                LocalDateTime.of(2022, 10, 11, 14, 12));

        Subtask subtask2 = taskManager.createSubtask(epic.getId(), "Test addNewSubtask2 ",
                "Test addNewSubtask description2", Duration.ofMinutes(7),
                LocalDateTime.of(2022, 10, 12, 14, 12));

        Subtask subtask3 = taskManager.createSubtask(epic.getId(), "Test addNewSubtask3",
                "Test addNewSubtask description3", Duration.ofMinutes(5),
                LocalDateTime.of(2022, 10, 11, 15, 12));


        List<Epic> epics = taskManager.getEpics();
        List<Subtask> subtasks = taskManager.getSubtasks(epic.getId());

        taskManager.removeAllEpicSubtasks(10);

        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(3, subtasks.size(), "Произошло удаление сабтасков по неверному id эпика.");

    }

    @Test
    void removeTaskByIdTest() {
        final Task task = taskManager.createTask("Test addNewTask", "Test addNewTask description",
                Duration.ofMinutes(5), LocalDateTime.of(2000, 1, 1, 12, 13));

        final Task task1 = taskManager.createTask("Test addNewTask1", "Test addNewTask1 description",
                Duration.ofMinutes(5), LocalDateTime.of(2001, 1, 1, 12, 13));

        final Task task2 = taskManager.createTask("Test addNewTask2", "Test addNewTask description2",
                Duration.ofMinutes(5), LocalDateTime.of(2002, 1, 1, 12, 13));

        List<Task> tasks = taskManager.getTasks();
        assertEquals(3, tasks.size(), "Неверное количество задач.");

        taskManager.removeTaskById(task1.getId());
        List<Task> tasks1 = taskManager.getTasks();
        assertEquals(2, tasks1.size(), "Неверное количество задач.");

    }

    @Test
    void removeTaskByWrongIdTest() {
        final Task task = taskManager.createTask("Test addNewTask", "Test addNewTask description",
                Duration.ofMinutes(5), LocalDateTime.of(2000, 1, 1, 12, 13));

        final Task task1 = taskManager.createTask("Test addNewTask1", "Test addNewTask1 description",
                Duration.ofMinutes(5), LocalDateTime.of(2001, 1, 1, 12, 13));

        final Task task2 = taskManager.createTask("Test addNewTask2", "Test addNewTask description2",
                Duration.ofMinutes(5), LocalDateTime.of(2002, 1, 1, 12, 13));

        taskManager.removeTaskById(10);
        List<Task> tasks1 = taskManager.getTasks();
        assertEquals(3, tasks1.size(), "Произошло удаление задачи по неверному id.");
    }


    @Test
    void removeEpicByIdTest() {
        Epic epic = taskManager.createEpic("Test addNewEpic", "Test addNewEpic description"
        );

        Epic epic1 = taskManager.createEpic("Test addNewEpic1", "Test addNewEpic1 description"
        );

        Epic epic2 = taskManager.createEpic("Test addNewEpic2", "Test addNewEpic2 description"
        );

        List<Epic> epics = taskManager.getEpics();

        assertEquals(3, epics.size(), "Неверное количество эпиков.");

        taskManager.removeEpicById(epic2.getId());
        List<Epic> epics2 = taskManager.getEpics();

        assertEquals(2, epics2.size(), "Неверное количество эпиков.");
    }

    @Test
    void removeEpicByWrongIdTest() {
        Epic epic = taskManager.createEpic("Test addNewEpic", "Test addNewEpic description"
        );

        Epic epic1 = taskManager.createEpic("Test addNewEpic1", "Test addNewEpic1 description"
        );

        Epic epic2 = taskManager.createEpic("Test addNewEpic2", "Test addNewEpic2 description"
        );

        List<Epic> epics = taskManager.getEpics();
        taskManager.removeEpicById(10);
        assertEquals(3, epics.size(), "Произошло удаление задачи по неверному id.");

    }


    @Test
    void removeSubtaskByIdEpic() {
        Epic epic = taskManager.createEpic("Test addNewEpic", "Test addNewEpic description"
        );

        Subtask subtask1 = taskManager.createSubtask(epic.getId(), "Test addNewSubtask ",
                "Test addNewSubtask description", Duration.ofMinutes(5),
                LocalDateTime.of(2022, 10, 11, 14, 12));

        Subtask subtask2 = taskManager.createSubtask(epic.getId(), "Test addNewSubtask2 ",
                "Test addNewSubtask description2", Duration.ofMinutes(7),
                LocalDateTime.of(2022, 10, 12, 14, 12));

        Subtask subtask3 = taskManager.createSubtask(epic.getId(), "Test addNewSubtask3",
                "Test addNewSubtask description3", Duration.ofMinutes(5),
                LocalDateTime.of(2022, 10, 11, 15, 12));


        List<Epic> epics = taskManager.getEpics();
        List<Subtask> subtasks = taskManager.getSubtasks(epic.getId());

        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(3, subtasks.size(), "Неверное количество сабтасков.");

        taskManager.removeSubtaskByIdEpic(epic.getId(), subtask3.getId());
        List<Epic> epics2 = taskManager.getEpics();
        List<Subtask> subtasks2 = taskManager.getSubtasks(epic.getId());

        assertEquals(1, epics2.size(), "Неверное количество эпиков.");
        assertEquals(2, subtasks2.size(), "Неверное количество сабтасков.");
    }

    @Test
    void removeSubtaskByWrongIdEpic() {
        Epic epic = taskManager.createEpic("Test addNewEpic", "Test addNewEpic description"
        );

        Subtask subtask1 = taskManager.createSubtask(epic.getId(), "Test addNewSubtask ",
                "Test addNewSubtask description", Duration.ofMinutes(5),
                LocalDateTime.of(2022, 10, 11, 14, 12));

        Subtask subtask2 = taskManager.createSubtask(epic.getId(), "Test addNewSubtask2 ",
                "Test addNewSubtask description2", Duration.ofMinutes(7),
                LocalDateTime.of(2022, 10, 12, 14, 12));

        Subtask subtask3 = taskManager.createSubtask(epic.getId(), "Test addNewSubtask3",
                "Test addNewSubtask description3", Duration.ofMinutes(5),
                LocalDateTime.of(2022, 10, 11, 15, 12));


        List<Epic> epics = taskManager.getEpics();
        List<Subtask> subtasks = taskManager.getSubtasks(epic.getId());
        taskManager.removeSubtaskByIdEpic(epic.getId(), 10);

        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(3, subtasks.size(), "Произошло удаление сабтаска по неверному id.");
    }

    @Test
    void updateTaskTest() {
        final Task task = taskManager.createTask("Test addNewTask", "Test addNewTask description",
                Duration.ofMinutes(5), LocalDateTime.of(2000, 1, 1, 12, 13));

        final Task task1 = taskManager.createTask("Test addNewTask1", "Test addNewTask1 description",
                Duration.ofMinutes(5), LocalDateTime.of(2001, 1, 1, 12, 13));

        final Task task2 = taskManager.createTask("Test addNewTask2", "Test addNewTask description2",
                Duration.ofMinutes(5), LocalDateTime.of(2002, 1, 1, 12, 13));

        final Task task4 = new Task(task1.getId(), TASK, "UPDATED_TASK", DONE, "UPDATED_TASK DESCRIPTION",
                Duration.ofMinutes(5), LocalDateTime.of(2007, 12, 3, 14, 0));

        taskManager.updateTask(task4);
        final Task changedTask = taskManager.getTaskById(task1.getId());
        assertEquals(task4.getStatus(), changedTask.getStatus(), "Не произошел апдейт задачи.");
    }


    @Test
    void updateEpicTest() {
        Epic epic = taskManager.createEpic("Test addNewEpic", "Test addNewEpic description"
        );

        Epic epic1 = taskManager.createEpic("Test addNewEpic1", "Test addNewEpic1 description"
        );

        Epic epic2 = taskManager.createEpic("Test addNewEpic2", "Test addNewEpic2 description"
        );

        final Epic epic3 = new Epic(epic1.getId(), EPIC, "UPDATED_EPIC_NEW", NEW, "UPDATED_EPIC DESCRIPTION");
        taskManager.updateEpic(epic3);

        final Epic changedEpic = taskManager.getEpicById(epic1.getId());
        assertEquals(epic3.getName(), changedEpic.getName(), "Не произошел апдейт эпика'.");
    }

    @Test
    void updateEpicWithSubtaskTest() {
        Epic epic = taskManager.createEpic("Test addNewEpic", "Test addNewEpic description"
        );

        Epic epic1 = taskManager.createEpic("Test addNewEpic1", "Test addNewEpic1 description"
        );
        Subtask subtask2 = taskManager.createSubtask(epic.getId(), "Test addNewSubtask2 ",
                "Test addNewSubtask description2", Duration.ofMinutes(7),
                LocalDateTime.of(2022, 10, 12, 14, 12));
        subtask2.setStatus(IN_PROGRESS);
        taskManager.updateSubtask(epic.getId(), subtask2);

        Subtask subtask3 = taskManager.createSubtask(epic.getId(), "Test addNewSubtask3",
                "Test addNewSubtask description3", Duration.ofMinutes(5),
                LocalDateTime.of(2022, 10, 11, 15, 12));
        subtask3.setStatus(IN_PROGRESS);

        Epic epic2 = taskManager.createEpic("Test addNewEpic2", "Test addNewEpic2 description"
        );

        final Epic epic3 = new Epic(epic1.getId(), EPIC, "UPDATED_EPIC_NEW", IN_PROGRESS, "UPDATED_EPIC DESCRIPTION");
        taskManager.updateEpic(epic3);

        final Epic changedEpic = taskManager.getEpicById(epic1.getId());
        assertEquals(epic3.getName(), changedEpic.getName(), "Не произошел апдейт эпика.");
        assertEquals(epic3.getEndTime(), changedEpic.getEndTime(), "Не произошел апдейт эпика.");
    }

    @Test
    void updateSubtaskTest() {
        Epic epic = taskManager.createEpic("Test addNewEpic", "Test addNewEpic description"
        );

        Subtask subtask1 = taskManager.createSubtask(epic.getId(), "Test addNewSubtask ",
                "Test addNewSubtask description", Duration.ofMinutes(5),
                LocalDateTime.of(2022, 10, 11, 14, 12));

        Subtask subtask2 = taskManager.createSubtask(epic.getId(), "Test addNewSubtask2 ",
                "Test addNewSubtask description2", Duration.ofMinutes(7),
                LocalDateTime.of(2022, 10, 12, 14, 12));

        Subtask subtask3 = taskManager.createSubtask(epic.getId(), "Test addNewSubtask3",
                "Test addNewSubtask description3", Duration.ofMinutes(5),
                LocalDateTime.of(2022, 10, 11, 15, 12));

        Subtask subtask4 = new Subtask(subtask2.getId(), SUBTASK, "Test UPDATE", NEW,
                "Test description UPDATE", Duration.ofMinutes(10),
                LocalDateTime.of(2014, 10, 12, 14, 12), epic.getId());
        taskManager.updateSubtask(epic.getId(), subtask4);

        final Subtask changedSubtask = taskManager.getSubtaskByIdEpic(epic.getId(), subtask2.getId());
        assertEquals(subtask4.getStatus(), changedSubtask.getStatus(), "Не произошел апдейт сабтаска.");
    }


    @Test
    void getPrioritizedTasksTest() {
        final Task task = taskManager.createTask("Test addNewTask", "Test addNewTask description",
                Duration.ofMinutes(5), LocalDateTime.of(2004, 1, 1, 12, 13));

        final Task task1 = taskManager.createTask("Test addNewTask1", "Test addNewTask1 description",
                Duration.ofMinutes(5), LocalDateTime.of(2001, 1, 1, 12, 13));

        final Task task2 = taskManager.createTask("Test addNewTask2", "Test addNewTask description2",
                Duration.ofMinutes(5), LocalDateTime.of(2002, 1, 1, 12, 13));

        Set<Task> sortedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
        sortedTasks.add(task);
        sortedTasks.add(task1);
        sortedTasks.add(task2);
        Task[] sortedTaskArray = sortedTasks.toArray(Task[]::new);

        final Task sortedTask = sortedTaskArray[0];
        final Task sortedTask1 = sortedTaskArray[1];
        final Task sortedTask2 = sortedTaskArray[2];

        assertEquals(task.getId(), sortedTask2.getId(), "Сортировка не произошла.");
        assertEquals(task1.getId(), sortedTask.getId(), "Сортировка не произошла.");
        assertEquals(task2.getId(), sortedTask1.getId(), "Сортировка не произошла.");
    }
}

