package manager;

import task.Task;

import java.util.ArrayList;
import java.util.List;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static String historyToString(HistoryManager manager) {
        StringBuilder historyIds = new StringBuilder("");
        for (Task task : manager.getHistory()) {
            historyIds
                    .append(task.getId())
                    .append(",");
        }
        return historyIds.toString();
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> idHistory = new ArrayList<>();
        String[] parts = value.split(",");
        for (String part : parts) {
            idHistory.add(Integer.valueOf(part));
        }
        return idHistory;
    }

    public static FileBackedTasksManager loadFromFile(String pathName) {
        return new FileBackedTasksManager(pathName);
    }
}
