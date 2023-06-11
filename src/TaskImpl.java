import java.time.LocalDateTime;

public class TaskImpl extends Task {
    private String priority;

    public TaskImpl(String taskName, LocalDateTime dateTime, String priority) {
        super(taskName, dateTime);
        this.priority = priority;
    }

    @Override
    public String getPriority() {
        return priority;
    }
}

