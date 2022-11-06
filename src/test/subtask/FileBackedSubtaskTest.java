package test.subtask;

import manager.Managers;
import org.junit.jupiter.api.BeforeEach;

public class FileBackedSubtaskTest extends SubtaskTest {
    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
    }
}
