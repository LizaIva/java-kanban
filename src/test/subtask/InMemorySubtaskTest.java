package test.subtask;

import manager.Managers;
import org.junit.jupiter.api.BeforeEach;

public class InMemorySubtaskTest extends SubtaskTest{
    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
    }
}
