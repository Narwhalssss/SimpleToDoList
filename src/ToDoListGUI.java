
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.List;


public class ToDoListGUI {
    private JFrame frame;
    private JTextField taskNameField;
    private JButton addButton;
    private JButton markCompletedButton;
    private JButton deleteButton;
    private JButton editButton;
    private JButton saveButton;
    private JButton loadButton;
    private JList<String> taskList;
    private DefaultListModel<String> taskListModel;
    private List<Task> tasks;

    public ToDoListGUI() {
        tasks = new ArrayList<>();
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("ToDo List Application");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new FlowLayout());
        JLabel taskNameLabel = new JLabel("Task Name:");
        taskNameField = new JTextField(20);
        addButton = new JButton("Add Task");
        markCompletedButton = new JButton("Mark as Completed");
        deleteButton = new JButton("Delete Task");
        editButton = new JButton("Edit Task");
        saveButton = new JButton("Save Tasks");
        loadButton = new JButton("Load Tasks");

        inputPanel.add(taskNameLabel);
        inputPanel.add(taskNameField);
        inputPanel.add(addButton);
        inputPanel.add(markCompletedButton);
        inputPanel.add(deleteButton);
        inputPanel.add(editButton);
        inputPanel.add(saveButton);
        inputPanel.add(loadButton);

        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(taskList), BorderLayout.CENTER);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String taskName = taskNameField.getText().trim();
                if (!taskName.isEmpty()) {
                    LocalDateTime dateTime = LocalDateTime.now(); // Default value for date and time
                    String priority = "Normal"; // Default value for priority

                    // Show input dialogs to set date, time, and priority
                    String dateString = JOptionPane.showInputDialog(frame, "Enter date (YYYY-MM-DD):");
                    String timeString = JOptionPane.showInputDialog(frame, "Enter time (HH:MM):");
                    String priorityString = JOptionPane.showInputDialog(frame, "Enter priority (High/Medium/Low):");

                    // Parse date and time strings into LocalDateTime
                    if (dateString != null && !dateString.trim().isEmpty() &&
                            timeString != null && !timeString.trim().isEmpty()) {
                        try {
                            dateTime = LocalDateTime.of(
                                    LocalDate.parse(dateString.trim()),
                                    LocalTime.parse(timeString.trim())
                            );
                        } catch (DateTimeParseException ex) {
                            JOptionPane.showMessageDialog(frame, "Invalid date or time format!");
                        }
                    }

                    // Create the appropriate Task based on the priority
                    Task task;
                    if (priorityString != null && !priorityString.trim().isEmpty()) {
                        priority = priorityString.trim();
                        if (priority.equalsIgnoreCase("High")) {
                            task = new HighPriorityTask(taskName, dateTime);
                        } else if (priority.equalsIgnoreCase("Medium")) {
                            task = new MediumPriorityTask(taskName, dateTime);
                        } else if (priority.equalsIgnoreCase("Low")) {
                            task = new LowPriorityTask(taskName, dateTime);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Invalid priority! Using default 'Normal' priority.");
                            task = new TaskImpl(taskName, dateTime, priority);
                        }
                    } else {
                        task = new TaskImpl(taskName, dateTime, priority);
                    }

                    tasks.add(task);
                    displayTasks();
                    taskNameField.setText("");
                }
            }
        });


        markCompletedButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = taskList.getSelectedIndex();
                if (selectedIndex != -1) {
                    Task task = tasks.get(selectedIndex);
                    task.setCompleted(true);
                    displayTasks();
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = taskList.getSelectedIndex();
                if (selectedIndex != -1) {
                    tasks.remove(selectedIndex);
                    displayTasks();
                }
            }
        });

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = taskList.getSelectedIndex();
                if (selectedIndex != -1) {
                    Task task = tasks.get(selectedIndex);

                    // Show input dialogs to modify task details
                    String newTaskName = JOptionPane.showInputDialog(frame, "Enter new task name:");
                    String newDateString = JOptionPane.showInputDialog(frame, "Enter new date (YYYY-MM-DD):");
                    String newTimeString = JOptionPane.showInputDialog(frame, "Enter new time (HH:MM):");
                    String newPriorityString = JOptionPane.showInputDialog(frame, "Enter new priority (High/Medium/Low):");

                    // Update task details if provided
                    if (newTaskName != null && !newTaskName.trim().isEmpty()) {
                        task.setTaskName(newTaskName.trim());
                    }
                    if (newDateString != null && !newDateString.trim().isEmpty() &&
                            newTimeString != null && !newTimeString.trim().isEmpty()) {
                        try {
                            LocalDateTime newDateTime = LocalDateTime.of(
                                    LocalDate.parse(newDateString.trim()),
                                    LocalTime.parse(newTimeString.trim())
                            );
                            task.setDateTime(newDateTime);
                        } catch (DateTimeParseException ex) {
                            JOptionPane.showMessageDialog(frame, "Invalid date or time format!");
                        }
                    }
                    if (newPriorityString != null && !newPriorityString.trim().isEmpty()) {
                        String newPriority = newPriorityString.trim();
                        if (newPriority.equalsIgnoreCase("High")) {
                            if (!(task instanceof HighPriorityTask)) {
                                task = new HighPriorityTask(task.getTaskName(), task.getDateTime());
                                tasks.set(selectedIndex, task);
                            }
                        } else if (newPriority.equalsIgnoreCase("Medium")) {
                            if (!(task instanceof MediumPriorityTask)) {
                                task = new MediumPriorityTask(task.getTaskName(), task.getDateTime());
                                tasks.set(selectedIndex, task);
                            }
                        } else if (newPriority.equalsIgnoreCase("Low")) {
                            if (!(task instanceof LowPriorityTask)) {
                                task = new LowPriorityTask(task.getTaskName(), task.getDateTime());
                                tasks.set(selectedIndex, task);
                            }
                        } else {
                            JOptionPane.showMessageDialog(frame, "Invalid priority! Task priority remains unchanged.");
                        }
                    }

                    displayTasks();
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showSaveDialog(frame);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    String filePath = file.getAbsolutePath();
                    saveTasksToFile(filePath);
                }
            }
        });

        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(frame);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String filePath = selectedFile.getAbsolutePath();
                    loadTasksFromFile(filePath);
                }
            }
        });

        frame.setVisible(true);
    }

    private void displayTasks() {
        // Sort tasks based on priority level
        Collections.sort(tasks, (task1, task2) -> {
            // Define the priority order
            List<String> priorityOrder = Arrays.asList("High", "Medium", "Low", "Normal");

            // Get the index of the priority level for each task
            int index1 = priorityOrder.indexOf(task1.getPriority());
            int index2 = priorityOrder.indexOf(task2.getPriority());

            // Compare the indices to determine the sorting order
            return Integer.compare(index1, index2);
        });

        // Clear the existing task list model
        taskListModel.clear();

        // Add the sorted tasks to the task list model
        for (Task task : tasks) {
            taskListModel.addElement(task.toString());
        }
    }

    private void saveTasksToFile(String filePath) {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            for (Task task : tasks) {
                String priority;
                if (task instanceof HighPriorityTask) {
                    priority = "High";
                } else if (task instanceof MediumPriorityTask) {
                    priority = "Medium";
                } else if (task instanceof LowPriorityTask) {
                    priority = "Low";
                } else {
                    // Default priority if the task is not an instance of any specific priority class
                    priority = "Normal";
                }

                String taskLine = String.format("%s,%s,%s,%s",
                        task.getTaskName(),
                        task.getDateTime().toString(),
                        task.isCompleted(),
                        priority);
                writer.println(taskLine);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Failed to save tasks to file: " + filePath);
            return;
        }

        JOptionPane.showMessageDialog(frame, "Tasks saved to file: " + filePath);
    }

    private void loadTasksFromFile(String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            tasks.clear(); // Clear existing tasks before loading from file

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] taskData = line.split(",");

                if (taskData.length == 4) {
                    String taskName = taskData[0];
                    LocalDateTime dateTime = LocalDateTime.parse(taskData[1]);
                    boolean completed = Boolean.parseBoolean(taskData[2]);
                    String priority = taskData[3];

                    Task task;
                    if (priority.equalsIgnoreCase("High")) {
                        task = new HighPriorityTask(taskName, dateTime);
                    } else if (priority.equalsIgnoreCase("Medium")) {
                        task = new MediumPriorityTask(taskName, dateTime);
                    } else if (priority.equalsIgnoreCase("Low")) {
                        task = new LowPriorityTask(taskName, dateTime);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Invalid priority found in the file. Skipping line: " + line);
                        continue;
                    }

                    task.setCompleted(completed);
                    tasks.add(task);
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid task format found in the file. Skipping line: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(frame, "File not found: " + filePath);
            return;
        }

        displayTasks();
        JOptionPane.showMessageDialog(frame, "Tasks loaded from file: " + filePath);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ToDoListGUI();
            }
        });
    }

}
