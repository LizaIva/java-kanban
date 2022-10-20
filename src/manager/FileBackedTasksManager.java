package manager;

import exception.ManagerSaveException;
import task.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private String pathName;

    public FileBackedTasksManager(String pathName) {
        this.pathName = pathName;

        String content = null;
        try {
            content = Files.readString(Path.of(pathName));
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
        if (content.isEmpty()) {
            return;
        }
        String[] lines = content.split("\n");
        int maxId = 0;

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            if (line.isBlank()) {
                break;
            }

            Task task = fromString(line);

            if (maxId < task.getId()) {
                maxId = task.getId();
            }
        }


        counter = maxId + 1;

        historyFromString(lines);

    }

    private Task fromString(String line) {
        String[] parts = line.split(",");

        int id = Integer.parseInt(parts[0]);
        Type type = Type.valueOf(parts[1]);
        switch (type) {
            case TASK:
                Task task = new Task();
                task.setId(id);
                task.setType(type);
                task.setName(parts[2]);
                task.setStatus(Status.valueOf(parts[3]));
                task.setDescription(parts[4]);
                super.updateTask(task);
                return task;
            case EPIC:
                Epic epic = new Epic();
                epic.setId(id);
                epic.setType(type);
                epic.setName(parts[2]);
                epic.setStatus(Status.valueOf(parts[3]));
                epic.setDescription(parts[4]);
                super.updateEpic(epic);
                return epic;
            default:
                Subtask subtask = new Subtask();
                subtask.setId(id);
                subtask.setType(type);
                subtask.setName(parts[2]);
                subtask.setStatus(Status.valueOf(parts[3]));
                subtask.setDescription(parts[4]);
                subtask.setEpicId(Integer.parseInt(parts[5]));
                super.updateSubtask(subtask.getEpicId(), subtask);
                return subtask;
        }
    }

    @Override
    public Task createTask(String name, String description) {
        Task task = super.createTask(name, description);
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
    public Subtask createSubtask(int epicId, String name, String description) {
        Subtask subtask = super.createSubtask(epicId, name, description);
        save();
        return subtask;
    }

    private void save() {
        try {
            try (FileWriter out = new FileWriter(pathName)) {
                out.write("id,type,name,status,description,epic\n");

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

    public void historyFromString(String[] lines) {
        List<Integer> historyIds = Managers.historyFromString(lines[lines.length - 1]);
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

    public static void main(String[] args) {
        TaskManager taskManager = Managers.loadFromFile("/Users/elizavetaivanova/Prakticum/java-kanban/src/manager.csv");
        Task task1 = taskManager.createTask("Купить помидоры", "Выбрать в самокате");
        Task task2 = taskManager.createTask("Купить огурцы", "Зайти на рынок");
        Epic epic1 = taskManager.createEpic("Приготовить обед", "Купить продукты");
        Epic epic2 = taskManager.createEpic("Убраться в квартире", "Успеть сделать все до четверга");
        Subtask subtask1 = taskManager.createSubtask(epic2.getId(), "Помыть полы", "Использовать средство");
        Subtask subtask2 = taskManager.createSubtask(epic2.getId(), "Протереть пыль", "Тряпка в шкафу");
        Subtask subtask3 = taskManager.createSubtask(epic2.getId(), "Заказать рис", "Рис Басмати");

        taskManager.getHistory();

        taskManager.getTaskById(task2.getId());
        taskManager.getTaskById(task1.getId());

        taskManager.getEpicById(epic1.getId());
        taskManager.getTaskById(task2.getId());

        taskManager.getTaskById(task1.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getEpicById(epic2.getId());

        taskManager.removeTaskById(task1.getId());

        taskManager.removeEpicById(epic2.getId());

        TaskManager taskManager2 = Managers.loadFromFile("/Users/elizavetaivanova/Prakticum/java-kanban/src/manager.csv");
    }
}
