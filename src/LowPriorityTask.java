import java.time.LocalDateTime;

public class LowPriorityTask extends Task {
    public LowPriorityTask(String taskName, LocalDateTime dateTime) {
        super(taskName, dateTime);
    }

    @Override
    public String getPriority() {
        return "Low";
    }
}
