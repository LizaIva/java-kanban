package manager;

import task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node<Task>> history = new LinkedHashMap<>();
    private Node<Task> head = null;
    private Node<Task> tail = null;

    @Override
    public void add(Task task) {
        if (head == null && tail == null) {
            head = new Node<>(task);
            history.put(task.getId(), head);
        } else if (tail == null) {
            tail = new Node<>(task);
            tail.setPrev(head);
            head.setNext(tail);
            history.put(task.getId(), tail);
        } else {
            if (history.containsKey(task.getId())) {
                remove(task.getId());
            }

            Node<Task> newNode = new Node<>(task);
            newNode.setPrev(tail);
            tail.setNext(newNode);
            tail = newNode;
            history.put(task.getId(), newNode);
        }
    }

    @Override
    public void remove(int id) {
        Node<Task> currentNode = history.get(id);

        if (currentNode != null) {
            if (currentNode.getPrev() == null && currentNode.getNext() == null) {
                head = null;
                tail = null;
            } else if (currentNode.getPrev() == null) {
                Node<Task> next = currentNode.getNext();
                next.setPrev(null);
                head = next;
            } else if (currentNode.getNext() == null) {
                Node<Task> prev = currentNode.getPrev();
                prev.setNext(null);
            } else {
                Node<Task> prev = currentNode.getPrev();
                Node<Task> next = currentNode.getNext();
                prev.setNext(next);
                next.setPrev(prev);
            }
            history.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        ArrayList<Task> taskList = new ArrayList<>();
        Node<Task> currentNode = head;

        if (head != null) {
            while (currentNode.getNext() != null) {
                taskList.add(currentNode.getData());
                currentNode = currentNode.getNext();
            }
            if (currentNode != null) {
                taskList.add(currentNode.getData());
            }
        }
        return taskList;
    }
}
