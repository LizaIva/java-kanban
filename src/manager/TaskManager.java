package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.Collection;
import java.util.List;

public interface TaskManager {


    Task createTask(String name, String description);

    Epic createEpic(String name, String description);

    Subtask createSubtask(int idEpic, String name, String description);

    Collection<Task> getTasks();

    Collection<Epic> getEpics();

    Collection<Subtask> getSubtasks(int idEpic);

    Task getTaskById(int idTask);

    Epic getEpicById(int idEpic);

    Subtask getSubtaskByIdEpic(int idEpic, int idSubtask);

    void removeAllTasks();

    void removeAllEpics();

    void removeAllEpicSubtasks(int idEpic);

    void removeTaskById(int idTask);

    void removeEpicById(int idEpic);

    void removeSubtaskByIdEpic(int idEpic, int idSubtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(int idEpic, Subtask subtask);

    List<Task> getHistory();

}
