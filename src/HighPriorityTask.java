import java.time.LocalDateTime;

public class HighPriorityTask extends Task {
    public HighPriorityTask(String taskName, LocalDateTime dateTime) {
        super(taskName, dateTime);
    }

    @Override
    public String getPriority() {
        return "High";
    }
}