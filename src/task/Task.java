package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.TreeSet;

public class Task {
    protected int id;
    private String name;
    private String description;
    private Status status;
    private Type type;
    private Duration duration;
    private LocalDateTime startTime;


    public Task(int id, Type type, String name, Status status, String description, Duration duration,
                LocalDateTime startTime) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.status = status;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task() {
    }


    public LocalDateTime getEndTime() {
        if (getDuration() != null && getStartTime() != null) {
            return startTime.plusMinutes(duration.toMinutes());
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return id + "," + Type.TASK.name() + "," + name + "," + status + "," + description + "," + duration.toMinutes()
                + "," + DateTimeFormatterUtils.formatter.format(startTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (id != task.id) return false;
        if (!Objects.equals(name, task.name)) return false;
        if (!Objects.equals(description, task.description)) return false;
        if (status != task.status) return false;
        if (type != task.type) return false;
        if (!Objects.equals(duration, task.duration)) return false;
        return Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        return result;
    }
}