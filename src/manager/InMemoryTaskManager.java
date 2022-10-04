package manager;

import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int counter;
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HistoryManager historyManger = Managers.getDefaultHistory();


    @Override
    public Task createTask(String name, String description) {
        Task task = new Task();
        task.setId(counter++);
        task.setName(name);
        task.setDescription(description);
        task.setStatus(Status.NEW);

        tasks.put(task.getId(), task);

        return task;
    }

    @Override
    public Epic createEpic(String name, String description) {
        Epic epic = new Epic();
        epic.setId(counter++);
        epic.setName(name);
        epic.setDescription(description);
        epic.setStatus(Status.NEW);

        epics.put(epic.getId(), epic);

        return epic;
    }

    @Override
    public Subtask createSubtask(int epicId, String name, String description) {

        Epic epic = epics.get(epicId);
        HashMap<Integer, Subtask> subtasks = epic.getSubtasks();

        Subtask subtask = new Subtask();
        subtask.setEpicId(epic.getId());
        subtask.setId(counter++);
        subtask.setName(name);
        subtask.setDescription(description);
        subtask.setStatus(Status.NEW);

        subtasks.put(subtask.getId(), subtask);

        return subtask;
    }

    @Override
    public Collection<Task> getTasks() {
        return tasks.values();
    }

    @Override
    public Collection<Epic> getEpics() {
        return epics.values();
    }

    @Override
    public Collection<Subtask> getSubtasks(int epicId) {
        Epic epic = epics.get(epicId);
        return epic.getValuesSubtasks();
    }

    @Override
    public Task getTaskById(int taskId) {
        Task task = tasks.get(taskId);
        historyManger.add(task);
        return task;
    }

    @Override
    public Epic getEpicById(int epicId) {
        Epic epic = epics.get(epicId);
        historyManger.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtaskByIdEpic(int epicId, int subtaskId) {
        Epic epic = epics.get(epicId);
        Subtask subtask = epic.getIdSubtaskById(subtaskId);
        historyManger.add(subtask);
        return subtask;
    }

    @Override
    public void removeAllTasks() {
        for (Integer id : tasks.keySet()) {
            historyManger.remove(id);
        }
        tasks.clear();
    }

    @Override
    public void removeAllEpics() {
        for (Epic epic : epics.values()) {
            historyManger.remove(epic.getId());
            epic.removeAllSubtasks();
        }
        epics.clear();
    }

    @Override
    public void removeAllEpicSubtasks(int epicId) {
        Epic epic = epics.get(epicId);

        for (Integer id : epic.getSubtasks().keySet()) {
            historyManger.remove(id);
        }

        epic.removeAllSubtasks();
    }

    @Override
    public void removeTaskById(int taskId) {
        historyManger.remove(taskId);
        tasks.remove(taskId);
    }

    @Override
    public void removeEpicById(int epicId) {
        historyManger.remove(epicId);
        epics.remove(epicId);
    }

    @Override
    public void removeSubtaskByIdEpic(int epicId, int subtaskId) {
        historyManger.remove(subtaskId);
        Epic epic = epics.get(epicId);
        epic.removeSubtaskById(subtaskId);
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateSubtask(int epicId, Subtask subtask) {
        Epic epic = epics.get(epicId);
        epic.updateSubtask(subtask);
        updateEpicStatus(epic, subtask.getStatus());
    }

    @Override
    public List<Task> getHistory() {
        return historyManger.getHistory();
    }
    private void updateEpicStatus(Epic epic, Status newSubtaskStatus) {
        switch (newSubtaskStatus) {
            case IN_PROGRESS:
                if (!epic.getStatus().equals(Status.IN_PROGRESS)) {
                    epic.setStatus(Status.IN_PROGRESS);
                }
                break;
            case DONE:
                if (epic.checkStatusSubtasks(Status.DONE)) {
                    epic.setStatus(Status.DONE);
                } else {
                    epic.setStatus(Status.IN_PROGRESS);
                }
        }
    }
}

