package manager;

import exception.ManagerSaveException;
import task.*;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;

        int maxId = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();

            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    line = reader.readLine();
                    historyFromString(line);
                    break;
                }

                Task task = fromString(line);

                if (maxId < task.getId()) {
                    maxId = task.getId();
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }

        counter = maxId + 1;
    }

    private Task fromString(String line) {
        String[] parts = line.split(",");

        int id = Integer.parseInt(parts[0]);
        Type type = Type.valueOf(parts[1]);

        switch (type) {
            case TASK:
                Duration taskDuration = Duration.ofMinutes(Long.parseLong(parts[5]));
                LocalDateTime taskDateTime = LocalDateTime.parse(parts[6], DateTimeFormatterUtils.formatter);
                Task task = new Task(id, type, parts[2], Status.valueOf(parts[3]), parts[4], taskDuration, taskDateTime);
                super.updateTask(task);
                return task;
            case EPIC:
                Epic epic = new Epic(id, type, parts[2], Status.valueOf(parts[3]), parts[4]);
                super.updateEpic(epic);
                return epic;
            default:
                Duration duration = Duration.ofMinutes(Long.parseLong(parts[5]));
                LocalDateTime dateTime = LocalDateTime.parse(parts[6], DateTimeFormatterUtils.formatter);
                Subtask subtask = new Subtask(id, type, parts[2], Status.valueOf(parts[3]), parts[4],
                        duration, dateTime, Integer.parseInt(parts[7]));
                super.updateSubtask(subtask.getEpicId(), subtask);
                return subtask;
        }
    }

    @Override
    public Task createTask(String name, String description, Duration duration, LocalDateTime startTime) {
        Task task = super.createTask(name, description, duration, startTime);
        save();

        return task;
    }

    @Override
    public Epic createEpic(String name, String description) {
        Epic epic = super.createEpic(name, description);
        save();
        return epic;
    }

    @Override
    public Subtask createSubtask(int epicId, String name, String description, Duration duration, LocalDateTime startTime) {
        Subtask subtask = super.createSubtask(epicId, name, description, duration, startTime);
        save();
        return subtask;
    }

    private void save() {
        try {
            try (FileWriter out = new FileWriter(file)) {
                out.write("id,type,name,status,description,duration,startTime,epic\n");

                for (Task task : tasks.values()) {
                    out.write(task.toString());
                    out.write("\n");
                }

                for (Epic epic : epics.values()) {
                    out.write(epic.toString());
                    out.write("\n");
                    for (Subtask subtask : epic.getSubtasks().values()) {
                        out.write(subtask.toString());
                        out.write("\n");
                    }
                }


                out.write("\n");
                out.write(Managers.historyToString(historyManger));
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }

    }

    @Override
    public Task getTaskById(int taskId) {
        Task task = super.getTaskById(taskId);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int epicId) {
        Epic epic = super.getEpicById(epicId);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskByIdEpic(int epicId, int subtaskId) {
        Subtask subtask = super.getSubtaskByIdEpic(epicId, subtaskId);
        save();
        return subtask;
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeAllEpicSubtasks(int epicId) {
        super.removeAllEpicSubtasks(epicId);
        save();
    }

    @Override
    public void removeTaskById(int taskId) {
        super.removeTaskById(taskId);
        save();
    }

    @Override
    public void removeEpicById(int epicId) {
        super.removeEpicById(epicId);
        save();
    }

    @Override
    public void removeSubtaskByIdEpic(int epicId, int subtaskId) {
        super.removeSubtaskByIdEpic(epicId, subtaskId);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(int epicId, Subtask subtask) {
        super.updateSubtask(epicId, subtask);
        save();
    }

    public void historyFromString(String line) {
        if (line == null || line.isBlank()) {
            return;
        }

        List<Integer> historyIds = Managers.historyFromString(line);
        for (Integer id : historyIds) {
            if (tasks.containsKey(id)) {
                historyManger.add(tasks.get(id));
            } else if (epics.containsKey(id)) {
                historyManger.add(epics.get(id));
            } else {
                for (Epic epic : epics.values()) {
                    Subtask subtask = epic.getIdSubtaskById(id);
                    if (subtask != null) {
                        historyManger.add(subtask);
                        break;
                    }
                }
            }
        }
    }
}
