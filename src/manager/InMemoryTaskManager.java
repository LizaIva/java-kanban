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
    public Subtask createSubtask(int idEpic, String name, String description) {

        Epic epic = epics.get(idEpic);
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
    public Collection<Subtask> getSubtasks(int idEpic) {
        Epic epic = epics.get(idEpic);
        return epic.getValuesSubtasks();
    }

    @Override
    public Task getTaskById(int idTask) {
        Task task = tasks.get(idTask);

        System.out.println(idTask);
        System.out.println(task);

        historyManger.add(task);
        return task;
    }

    @Override

    public Epic getEpicById(int idEpic) {
        Epic epic = epics.get(idEpic);
        historyManger.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtaskByIdEpic(int idEpic, int idSubtask) {
        Epic epic = epics.get(idEpic);
        Subtask subtask = epic.getIdSubtaskById(idSubtask);
        historyManger.add(subtask);
        return subtask;
    }

//    private <T extends Task> void updateHistory(T task) {
//    }


    @Override
    public void removeAllTasks() {
        tasks.clear();
    }

    @Override

    public void removeAllEpics() {
        for (Epic epic : epics.values()) {
            epic.removeAllSubtasks();
        }
        epics.clear();
    }

    @Override
    public void removeAllEpicSubtasks(int idEpic) {
        Epic epic = epics.get(idEpic);
        epic.removeAllSubtasks();
    }

    @Override
    public void removeTaskById(int idTask) {
        tasks.remove(idTask);
    }

    @Override
    public void removeEpicById(int idEpic) {
        epics.remove(idEpic);
    }

    @Override
    public void removeSubtaskByIdEpic(int idEpic, int idSubtask) {
        Epic epic = epics.get(idEpic);
        epic.removeSubtaskById(idSubtask);
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
    public void updateSubtask(int idEpic, Subtask subtask) {
        Epic epic = epics.get(idEpic);
        epic.updateSubtask(subtask);
        updateEpicStatus(epic, subtask.getStatus());
    }

    @Override
    public List<Task> getHistory() {
        return historyManger.getHistory();
    }
}

