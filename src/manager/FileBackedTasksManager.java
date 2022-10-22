package manager;

import exception.ManagerSaveException;
import task.*;

import java.io.*;
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
                Task task = new Task(id, type, parts[2], Status.valueOf(parts[3]), parts[4]);
                super.updateTask(task);
                return task;
            case EPIC:
                Epic epic = new Epic(id, type, parts[2], Status.valueOf(parts[3]), parts[4]);
                super.updateEpic(epic);
                return epic;
            default:
                Subtask subtask = new Subtask(id, type, parts[2], Status.valueOf(parts[3]), parts[4], Integer.parseInt(parts[5]));
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
            try (FileWriter out = new FileWriter(file)) {
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

    public void historyFromString(String line) {
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

    public static void main(String[] args) {
        File file = new File("/Users/elizavetaivanova/Prakticum/java-kanban/src/manager.csv");
        TaskManager taskManager = Managers.loadFromFile(file);
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

        //Создание нового FileBackedTasksManager из этого же файла
        TaskManager taskManager2 = Managers.loadFromFile(file);
        System.out.println(taskManager2.getHistory());
    }
}
