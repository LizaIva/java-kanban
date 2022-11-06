package test.subtask;

import manager.TaskManager;
import org.junit.jupiter.api.Test;
import task.Subtask;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNull;

public abstract class SubtaskTest {
    protected TaskManager taskManager;

    @Test
    void unableToCreateSubtaskWithoutEpic() {
        Subtask subtaskWithoutEpic = taskManager.createSubtask(10, "Test addNewSubtask ",
                "Test addNewSubtask description", Duration.ofMinutes(5),
                LocalDateTime.of(2022, 10, 11, 14, 12));

        assertNull(subtaskWithoutEpic, "Создан сабтаск без эпика");
    }
}
