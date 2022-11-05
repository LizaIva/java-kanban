package task;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private int epicId;

    public Subtask(int id, Type type, String name, Status status, String description, Duration duration,
                   LocalDateTime startTime, int epicId) {
        super(id, type, name, status, description, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask() {
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public LocalDateTime getEndTime() {
        return super.getEndTime();
    }

    @Override
    public String toString() {
        return getId() + "," + Type.SUBTASK.name() + "," + getName() + "," + getStatus() + "," + getDescription()
                + "," + getDuration().toMinutes() + ","
                + DateTimeFormatterUtils.formatter.format(getStartTime()) + "," + getEpicId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Subtask subtask = (Subtask) o;

        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + epicId;
        return result;
    }
}
