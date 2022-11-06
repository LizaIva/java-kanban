package test.epic;

import manager.Managers;
import org.junit.jupiter.api.BeforeEach;

public class FileBackedEpicTest extends EpicTest{
    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
    }
}
