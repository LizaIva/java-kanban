package manager;

import exception.IntervalIntersectionException;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected int counter;
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HistoryManager historyManger = Managers.getDefaultHistory();

    protected Set<Task> sortedTask = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    @Override
    public Set<Task> getPrioritizedTasks() {
        return sortedTask;
    }

    @Override
    public Task createTask(String name, String description, Duration duration, LocalDateTime startTime) {
        Task task = new Task();
        task.setId(counter++);
        task.setName(name);
        task.setDescription(description);
        task.setStatus(Status.NEW);
        task.setDuration(duration);
        task.setStartTime(startTime);

        checkIntervalIntersection(task, sortedTask);

        tasks.put(task.getId(), task);
        sortedTask.add(task);

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
    public Subtask createSubtask(int epicId, String name, String description,
                                 Duration duration, LocalDateTime startTime) {

        Epic epic = epics.get(epicId);
        if (epic == null) {
            return null;
        }
        HashMap<Integer, Subtask> subtasks = epic.getSubtasks();

        Subtask subtask = new Subtask();
        subtask.setEpicId(epic.getId());
        subtask.setId(counter++);
        subtask.setName(name);
        subtask.setDescription(description);
        subtask.setStatus(Status.NEW);
        subtask.setDuration(duration);
        subtask.setStartTime(startTime);

        checkIntervalIntersection(subtask, epic.getPrioritizedSubtasks());

        subtasks.put(subtask.getId(), subtask);

        return subtask;
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getSubtasks(int epicId) {
        Epic epic = epics.get(epicId);

        if (epic == null) {
            return null;
        }
        return epic.getValuesSubtasks();
    }

    @Override
    public Task getTaskById(int taskId) {
        Task task = tasks.get(taskId);
        if (task != null) {
            historyManger.add(task);
        }
        return task;
    }

    @Override
    public Epic getEpicById(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            historyManger.add(epic);
        }
        return epic;
    }

    @Override
    public Subtask getSubtaskByIdEpic(int epicId, int subtaskId) {
        Epic epic = epics.get(epicId);

        if (epic == null) {
            return null;
        }

        Subtask subtask = epic.getIdSubtaskById(subtaskId);

        if (subtask != null) {
            historyManger.add(subtask);
        }
        return subtask;
    }

    @Override
    public void removeAllTasks() {
        for (Integer id : tasks.keySet()) {
            historyManger.remove(id);
        }
        tasks.clear();
        sortedTask.clear();
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

        if (epic != null) {
            for (Integer id : epic.getSubtasks().keySet()) {
                historyManger.remove(id);
            }

            epic.removeAllSubtasks();
        }
    }

    @Override
    public void removeTaskById(int taskId) {
        historyManger.remove(taskId);
        Task removed = tasks.remove(taskId);
        if (removed != null) {
            sortedTask.remove(removed);
        }
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
        checkIntervalIntersection(task, sortedTask);
        tasks.put(task.getId(), task);
        sortedTask.add(task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateSubtask(int epicId, Subtask subtask) {
        Epic epic = epics.get(epicId);

        checkIntervalIntersection(subtask, epic.getPrioritizedSubtasks());
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

    private void checkIntervalIntersection(Task checkedTask, Set<? extends Task> sortedTask) {
        if (sortedTask.isEmpty()) {
            return;
        }
        LocalDateTime prevEndDateTime = null;
        for (Task task : sortedTask) {
            if (prevEndDateTime == null && checkedTask.getEndTime().isBefore(task.getStartTime())) {
                return;
            } else {
                if (prevEndDateTime == null) {
                    prevEndDateTime = task.getEndTime();
                    continue;
                }

                if (prevEndDateTime.isBefore(checkedTask.getStartTime()) && checkedTask.getEndTime().isBefore(task.getStartTime())) {
                    return;
                } else {
                    prevEndDateTime = task.getEndTime();
                }
            }
        }
        if (prevEndDateTime.isBefore(checkedTask.getStartTime())) {
            return;
        }

        throw new IntervalIntersectionException("Это время уже занято. Выберите другое время");
    }
}

