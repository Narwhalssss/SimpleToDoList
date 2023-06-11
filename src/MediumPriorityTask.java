import java.time.LocalDateTime;

public class MediumPriorityTask extends Task {
    public MediumPriorityTask(String taskName, LocalDateTime dateTime) {
        super(taskName, dateTime);
    }

    @Override
    public String getPriority() {
        return "Medium";
    }
}