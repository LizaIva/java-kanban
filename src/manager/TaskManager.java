package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface TaskManager {

    Task createTask(String name, String description, Duration duration, LocalDateTime startTime);

    Epic createEpic(String name, String description);

    Subtask createSubtask(int epicId, String name, String description, Duration duration, LocalDateTime startTime);

    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtasks(int epicId);

    Set<Task> getPrioritizedTasks();


    Task getTaskById(int taskId);

    Epic getEpicById(int epicId);

    Subtask getSubtaskByIdEpic(int epicId, int subtaskId);

    void removeAllTasks();

    void removeAllEpics();

    void removeAllEpicSubtasks(int epicId);

    void removeTaskById(int taskId);

    void removeEpicById(int epicId);

    void removeSubtaskByIdEpic(int epicId, int subtaskId);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(int epicId, Subtask subtask);

    List<Task> getHistory();
}
