import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Task {
    private String taskName;
    private boolean completed;
    private LocalDateTime dateTime;


    public Task(String taskName, LocalDateTime dateTime) {
        this.taskName = taskName;
        this.dateTime = dateTime;
        completed = false;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {

        this.taskName = taskName;
    }

    public boolean isCompleted() {

        return completed;
    }

    public LocalDateTime getDateTime() {

        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {

        this.dateTime = dateTime;
    }

    public void setCompleted(boolean completed) {

        this.completed = completed;
    }

    public abstract String getPriority();

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = dateTime.format(formatter);
        return "[" + getPriority() + "] " + taskName + (completed ? " (Completed)" : "") + " - " + formattedDateTime;
    }

}