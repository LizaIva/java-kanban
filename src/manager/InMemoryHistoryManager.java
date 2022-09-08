package manager;

import task.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final LinkedList<Task> history = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (history.size() == 10) {
            history.removeFirst();
        }
        history.add(task);

    }

    @Override
    public List<Task> getHistory() {
        LinkedList<Task> revLinkedList = new LinkedList<>();
        for (int i = history.size() - 1; i >= 0; i--) {
            revLinkedList.add(history.get(i));
        }
        return revLinkedList;
    }
}
