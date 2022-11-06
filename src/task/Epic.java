package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Epic extends Task {

    private LocalDateTime endTime;

    public Epic(int id, Type type, String name, Status status, String description) {
        super(id, type, name, status, description, null, null);
        this.endTime = foundEndTime();
    }

    public Epic() {
    }

    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    protected Set<Subtask> sortedSubtask = new TreeSet<>(Comparator.comparing(Subtask::getStartTime));

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public Set<Subtask> getPrioritizedSubtasks() {
        return sortedSubtask;
    }

    public boolean checkStatusSubtasks(Status status) {
        for (Subtask subtask : subtasks.values()) {
            if (!subtask.getStatus().equals(status)) {
                return false;
            }
        }
        return true;
    }

    public Duration getDuration() {
        long allMinutes = 0;
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getDuration() != null) {
                allMinutes += subtask.getDuration().toMinutes();
            }
        }
        return Duration.ofMinutes(allMinutes);
    }

    public LocalDateTime foundStartTime() {
        LocalDateTime startTime = null;
        for (Subtask subtask : subtasks.values()) {
            if (startTime == null) {
                startTime = subtask.getStartTime();
            } else {
                if (startTime.isBefore(subtask.getStartTime())) {
                    startTime = subtask.getStartTime();
                }
            }
        }
        return startTime;
    }

    public LocalDateTime foundEndTime() {
        LocalDateTime endTime = null;
        for (Subtask subtask : subtasks.values()) {
            if (endTime == null) {
                endTime = subtask.getEndTime();
            } else {
                if (endTime.isAfter(subtask.getStartTime())) {
                    endTime = subtask.getStartTime();
                }
            }
        }
        return endTime;
    }


    public List<Subtask> getValuesSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public Subtask getIdSubtaskById(int idSubtask) {
        return subtasks.get(idSubtask);
    }

    public void removeAllSubtasks() {
        subtasks.clear();

        sortedSubtask.clear();

        this.setDuration(null);
        this.setStartTime(null);
        this.endTime = null;
    }

    public void removeSubtaskById(int idSubtask) {
        Subtask removed = subtasks.remove(idSubtask);
        if (removed != null) {
            sortedSubtask.remove(removed);
        }
        this.setDuration(getDuration());
        this.setStartTime(foundStartTime());
        this.endTime = foundEndTime();
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);

        this.setDuration(getDuration());
        this.setStartTime(foundStartTime());
        this.endTime = foundEndTime();

        sortedSubtask.add(subtask);
    }

    @Override
    public String toString() {
        return getId() + "," + Type.EPIC.name() + "," + getName() + "," + getStatus() + "," + getDescription();
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
