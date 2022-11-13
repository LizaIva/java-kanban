package manager;

import com.google.gson.reflect.TypeToken;
import kvserver.KVTaskClient;
import task.Epic;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static http_server.HttpUtils.gson;

public class HTTPTaskManager extends FileBackedTasksManager {

    private final KVTaskClient kvTaskClient;

    public HTTPTaskManager(String url) {
        super(url);
        int maxId = -1;
        this.kvTaskClient = new KVTaskClient(url);
        HashMap<Integer, Task> tasks = gson.fromJson(kvTaskClient.load("task"), new TypeToken<>() {
        });
        HashMap<Integer, Epic> epics = gson.fromJson(kvTaskClient.load("epic"), new TypeToken<>() {
        });
        List<Integer> history = gson.fromJson(kvTaskClient.load("history"), new TypeToken<>() {
        });

        int maxIdTask = foundMaxId(tasks);
        int maxIdEpic = foundMaxId(epics);
        if (maxIdTask > maxIdEpic) {
            maxId = maxIdTask;
        } else {
            maxId = maxIdEpic;
        }

        counter = maxId + 1;

        loadTask(tasks);
        loadEpic(epics);
        loadHistory(history);
    }

    public int foundMaxId(HashMap<Integer, ? extends Task> tasks) {
        int maxId = -1;
        if (tasks == null) {
            return maxId;
        }
        for (Task task : tasks.values()) {
            int taskId = task.getId();
            if (taskId > maxId) {
                maxId = taskId;
            }
        }
        return maxId;
    }

    public void loadTask(HashMap<Integer, Task> tasks) {
        if (tasks == null) {
            return;
        }
        for (Task task : tasks.values()) {
            if (task == null) {
                return;
            }
            super.updateTask(task);
        }
    }

    public void loadEpic(HashMap<Integer, Epic> epics) {
        if (epics == null) {
            return;
        }
        for (Epic epic : epics.values()) {
            if (epic == null) {
                return;
            }
            super.updateEpic(epic);
        }
    }

    @Override
    protected void save() {
        kvTaskClient.put("task", gson.toJson(tasks));
        kvTaskClient.put("epic", gson.toJson(epics));

        List<Integer> historyIds = new ArrayList<>();
        List<Task> tasks = historyManger.getHistory();
        for (Task task : tasks) {
            Integer id = task.getId();
            historyIds.add(id);
        }

        kvTaskClient.put("history", gson.toJson(historyIds));
    }
}
