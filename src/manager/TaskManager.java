package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.Collection;
import java.util.List;

public interface TaskManager {

    Task createTask(String name, String description);

    Epic createEpic(String name, String description);

    Subtask createSubtask(int epicId, String name, String description);

    Collection<Task> getTasks();

    Collection<Epic> getEpics();

    Collection<Subtask> getSubtasks(int epicId);

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
