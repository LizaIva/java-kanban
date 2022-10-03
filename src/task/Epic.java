package task;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public class Epic extends Task {
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public boolean checkStatusSubtasks(Status status) {
        for (Subtask subtask : subtasks.values()) {
            if (!subtask.getStatus().equals(status)) {
                return false;
            }
        }
        return true;
    }

    public Collection<Subtask> getValuesSubtasks() {
        return subtasks.values();
    }

    public Subtask getIdSubtaskById(int idSubtask) {
        return subtasks.get(idSubtask);
    }

    public void removeAllSubtasks() {
        subtasks.clear();
    }

    public void removeSubtaskById(int idSubtask) {
        subtasks.remove(idSubtask);

    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status='" + getStatus() + '\'' +
                ", subtasks=" + subtasks +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Epic epic = (Epic) o;

        return subtasks != null ? subtasks.equals(epic.subtasks) : epic.subtasks == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (subtasks != null ? subtasks.hashCode() : 0);
        return result;
    }
}
