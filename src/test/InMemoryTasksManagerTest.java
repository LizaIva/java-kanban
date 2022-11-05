package test;

import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;

public class InMemoryTasksManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
    }
}
