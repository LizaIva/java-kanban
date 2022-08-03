package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.Collection;
import java.util.HashMap;

public class Manager {
    private int counter;
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Task> tasks = new HashMap<>();

    public Task createTask(String name, String description) {
        Task task = new Task();
        task.setId(counter++);
        task.setName(name);
        task.setDescription(description);
        task.setStatus(Task.NEW_STATUS);

        tasks.put(task.getId(), task);

        return task;
    }

    public Epic createEpic(String name, String description) {
        Epic epic = new Epic();
        epic.setId(counter++);
        epic.setName(name);
        epic.setDescription(description);
        epic.setStatus(Task.NEW_STATUS);

        epics.put(epic.getId(), epic);

        return epic;
    }

    public Subtask createSubtask(int idEpic, String name, String description) {

        Epic epic = epics.get(idEpic);
        HashMap<Integer, Subtask> subtasks = epic.getSubtasks();

        Subtask subtask = new Subtask();
        subtask.setEpicId(epic.getId());
        subtask.setId(counter++);
        subtask.setName(name);
        subtask.setDescription(description);
        subtask.setStatus(Task.NEW_STATUS);

        subtasks.put(subtask.getId(), subtask);

        return subtask;
    }

    public Collection<Task> getTasks() {
        return tasks.values();
    }

    public Collection<Epic> getEpics() {
        return epics.values();
    }

    public Collection<Subtask> getSubtasks(int idEpic) {
        Epic epic = epics.get(idEpic);
        return epic.getValuesSubtasks();
    }

    public Task getTaskById(int idTask) {
        return tasks.get(idTask);
    }

    public Epic getEpicById(int idEpic) {
        return epics.get(idEpic);
    }

    public Subtask getSubtaskByIdEpic(int idEpic, int idSubtask) {
        Epic epic = epics.get(idEpic);
        return epic.getIdSubtaskById(idSubtask);
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public void removeAllEpics() {
        for (Epic epic : epics.values()) {
            epic.removeAllSubtasks();
        }
        epics.clear();
    }

    public void removeAllEpicSubtasks(int idEpic) {
        Epic epic = epics.get(idEpic);
        epic.removeAllSubtasks();
    }

    public void removeTaskById(int idTask) {
        tasks.remove(idTask);
    }

    public void removeEpicById(int idEpic) {
        epics.remove(idEpic);
    }

    public void removeSubtaskByIdEpic(int idEpic, int idSubtask) {
        Epic epic = epics.get(idEpic);
        epic.removeSubtaskById(idSubtask);
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void updateSubtask(int idEpic, Subtask subtask) {
        Epic epic = epics.get(idEpic);
        epic.updateSubtask(subtask);
        updateEpicStatus(epic, subtask.getStatus());
    }

    public void updateEpicStatus(Epic epic, String newSubtaskStatus) {
        switch (newSubtaskStatus) {
            case Task.IN_PROGRESS_STATUS:
                if (!epic.getStatus().equals(Task.IN_PROGRESS_STATUS)) {
                    epic.setStatus(Task.IN_PROGRESS_STATUS);
                }
                break;
            case Task.DONE_STATUS:
                if (epic.checkStatusSubtasks(Task.DONE_STATUS)) {
                    epic.setStatus(Task.DONE_STATUS);
                } else {
                    epic.setStatus(Task.IN_PROGRESS_STATUS);
                }
        }
    }
}

