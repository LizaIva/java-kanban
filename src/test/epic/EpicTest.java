package test.epic;

import manager.TaskManager;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static task.Status.*;
import static task.Status.IN_PROGRESS;

public abstract class EpicTest {
    protected TaskManager taskManager;

    @Test
    void checkingChangesEpicStatusIfSubtaskIsEmpty() {
        Epic epic = taskManager.createEpic("Test addNewEpic", "Test addNewEpic description"
        );

        final Epic savedEpic = taskManager.getEpicById(epic.getId());
        assertEquals(savedEpic.getStatus(), NEW, "Cтатусы эпиков не совпадают.");
    }

    @Test
    void checkingChangesEpicStatusIfAllSubtasksAreNew() {
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

        final Epic savedEpic = taskManager.getEpicById(epic.getId());
        assertEquals(savedEpic.getStatus(), NEW, "Cтатусы эпиков не совпадают.");
    }


    @Test
    void checkingChangesEpicStatusIfAllSubtasksArelDone() {
        Epic epic = taskManager.createEpic("Test addNewEpic", "Test addNewEpic description"
        );

        Subtask subtask1 = taskManager.createSubtask(epic.getId(), "Test addNewSubtask ",
                "Test addNewSubtask description", Duration.ofMinutes(5),
                LocalDateTime.of(2022, 10, 11, 14, 12));
        subtask1.setStatus(DONE);
        taskManager.updateSubtask(epic.getId(), subtask1);

        Subtask subtask2 = taskManager.createSubtask(epic.getId(), "Test addNewSubtask2 ",
                "Test addNewSubtask description2", Duration.ofMinutes(7),
                LocalDateTime.of(2022, 10, 12, 14, 12));
        subtask2.setStatus(DONE);
        taskManager.updateSubtask(epic.getId(), subtask2);

        Subtask subtask3 = taskManager.createSubtask(epic.getId(), "Test addNewSubtask3",
                "Test addNewSubtask description3", Duration.ofMinutes(5),
                LocalDateTime.of(2022, 10, 11, 15, 12));
        subtask3.setStatus(DONE);
        taskManager.updateSubtask(epic.getId(), subtask3);

        final Epic savedEpic = taskManager.getEpicById(epic.getId());
        assertEquals(savedEpic.getStatus(), DONE, "Cтатусы эпиков не совпадают.");
    }

    @Test
    void checkingChangesEpicStatusIfSubtasksArelNewAndDone() {
        Epic epic = taskManager.createEpic("Test addNewEpic", "Test addNewEpic description"
        );

        Subtask subtask1 = taskManager.createSubtask(epic.getId(), "Test addNewSubtask ",
                "Test addNewSubtask description", Duration.ofMinutes(5),
                LocalDateTime.of(2022, 10, 11, 14, 12));

        Subtask subtask2 = taskManager.createSubtask(epic.getId(), "Test addNewSubtask2 ",
                "Test addNewSubtask description2", Duration.ofMinutes(7),
                LocalDateTime.of(2022, 10, 12, 14, 12));
        subtask2.setStatus(DONE);
        taskManager.updateSubtask(epic.getId(), subtask2);

        Subtask subtask3 = taskManager.createSubtask(epic.getId(), "Test addNewSubtask3",
                "Test addNewSubtask description3", Duration.ofMinutes(5),
                LocalDateTime.of(2022, 10, 11, 15, 12));
        subtask3.setStatus(DONE);
        taskManager.updateSubtask(epic.getId(), subtask3);

        Subtask subtask4 = taskManager.createSubtask(epic.getId(), "Test addNewSubtask4",
                "Test addNewSubtask description4", Duration.ofMinutes(5),
                LocalDateTime.of(2022, 10, 11, 17, 12));


        final Epic savedEpic = taskManager.getEpicById(epic.getId());
        assertEquals(savedEpic.getStatus(), IN_PROGRESS, "Cтатусы эпиков не совпадают.");
    }

    @Test
    void checkingChangesEpicStatusIfAllSubtasksAreInProgress() {
        Epic epic = taskManager.createEpic("Test addNewEpic", "Test addNewEpic description"
        );

        Subtask subtask1 = taskManager.createSubtask(epic.getId(), "Test addNewSubtask ",
                "Test addNewSubtask description", Duration.ofMinutes(5),
                LocalDateTime.of(2022, 10, 11, 14, 12));
        subtask1.setStatus(IN_PROGRESS);
        taskManager.updateSubtask(epic.getId(), subtask1);

        Subtask subtask2 = taskManager.createSubtask(epic.getId(), "Test addNewSubtask2 ",
                "Test addNewSubtask description2", Duration.ofMinutes(7),
                LocalDateTime.of(2022, 10, 12, 14, 12));
        subtask2.setStatus(IN_PROGRESS);
        taskManager.updateSubtask(epic.getId(), subtask2);

        Subtask subtask3 = taskManager.createSubtask(epic.getId(), "Test addNewSubtask3",
                "Test addNewSubtask description3", Duration.ofMinutes(5),
                LocalDateTime.of(2022, 10, 11, 15, 12));
        subtask3.setStatus(IN_PROGRESS);
        taskManager.updateSubtask(epic.getId(), subtask3);

        final Epic savedEpic = taskManager.getEpicById(epic.getId());
        assertEquals(savedEpic.getStatus(), IN_PROGRESS, "Cтатусы эпиков не совпадают.");
    }
}
