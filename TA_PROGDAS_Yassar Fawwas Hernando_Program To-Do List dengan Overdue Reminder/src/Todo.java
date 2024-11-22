import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Todo extends JFrame {
    private JTextField taskField;
    private JTextField deadlineField;
    private JPanel taskPanel;
    private ArrayList<TaskItem> tasks;
    private Timer reminderTimer;

    public Todo() {
        setTitle("To-Do List with Reminder");
        setSize(720, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tasks = new ArrayList<>();
        initUI();
        startReminder();
    }

    private void initUI() {

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Margin antar komponen

        // Background Image
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/gambar 1.jpg"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel background = new JLabel(scaledIcon);
        background.setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.setOpaque(false);

        JLabel taskLabel = new JLabel("Task:");
        taskField = new JTextField(10);

        JLabel deadlineLabel = new JLabel("Deadline (MM-dd HH:mm):");
        deadlineField = new JTextField(10);

        JButton addButton = new JButton("Add Task");

        inputPanel.add(taskLabel);
        inputPanel.add(taskField);
        inputPanel.add(deadlineLabel);
        inputPanel.add(deadlineField);
        inputPanel.add(addButton);

        // Task Panel
        taskPanel = new JPanel();
        taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(taskPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);

        JButton deleteButton = new JButton("Delete Task");
        JButton markDoneButton = new JButton("Mark as Done");

        buttonPanel.add(deleteButton);
        buttonPanel.add(markDoneButton);

        // Background layout serta menambahkan panel panel ke bckground
        background.add(inputPanel, BorderLayout.NORTH);
        background.add(scrollPane, BorderLayout.CENTER);
        background.add(buttonPanel, BorderLayout.SOUTH);

        // set Background
        setContentPane(background);

        // Menambahkan Task ke list
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String task = taskField.getText();
                String deadlineText = deadlineField.getText();
                if (!task.isEmpty() && !deadlineText.isEmpty()) {
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm");
                        Date deadline = dateFormat.parse(deadlineText);
                        Date now = new Date();
                        deadline.setYear(now.getYear());
                        addTask(task, deadline);
                        taskField.setText("");
                        deadlineField.setText("");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(Todo.this, "Invalid date format. Use MM-dd HH:mm");
                    }
                } else {
                    JOptionPane.showMessageDialog(Todo.this, "Please enter a task and deadline.");
                }
            }
        });

        // Menhapus Task
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeSelectedTask();
            }
        });

        // Menandai telah selesai
        markDoneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                markTaskAsDone();
            }
        });

        JButton editButton = new JButton("Edit Task");
        buttonPanel.add(editButton);

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editSelectedTask();
            }
        });
    }

    private void addTask(String taskName, Date deadline) {
        JCheckBox taskCheckBox = new JCheckBox(taskName + " (Deadline: " + new SimpleDateFormat("MM-dd HH:mm").format(deadline) + ")");
        taskCheckBox.setOpaque(false);

        TaskItem taskItem = new TaskItem(taskCheckBox, deadline);
        tasks.add(taskItem);

        taskPanel.add(taskCheckBox);
        taskPanel.revalidate();
        taskPanel.repaint();
    }

    private void removeSelectedTask() {
        for (int i = 0; i < tasks.size(); i++) {
            TaskItem taskItem = tasks.get(i);
            if (taskItem.getCheckBox().isSelected()) {
                taskPanel.remove(taskItem.getCheckBox());
                tasks.remove(i);
                i--;
            }
        }

        taskPanel.revalidate();
        taskPanel.repaint();
    }

    private void markTaskAsDone() {
        for (TaskItem taskItem : tasks) {
            if (taskItem.getCheckBox().isSelected() && !taskItem.getCheckBox().getText().endsWith("(Done)")) {
                taskItem.getCheckBox().setText(taskItem.getCheckBox().getText() + " (Done)");
                taskItem.setDeadline(null);
            }
        }
    }

    private void editSelectedTask() {
        for (TaskItem taskItem : tasks) {
            if (taskItem.getCheckBox().isSelected()) {
                // Ambil teks task lama
                String oldText = taskItem.getCheckBox().getText();

                // Dialog input untuk teks baru
                String newText = JOptionPane.showInputDialog(
                        Todo.this,
                        "Edit Task:",
                        oldText.contains(" (Done)") ? oldText.replace(" (Done)", "") : oldText // Hilangkan "(Done)" jika ada
                );

                if (newText != null && !newText.trim().isEmpty()) {
                    // Jika task sudah selesai, tambahkan "(Done)" ke teks baru
                    if (oldText.endsWith("(Done)")) {
                        newText += " (Done)";
                    }

                    // Update teks pada JCheckBox
                    taskItem.getCheckBox().setText(newText);

                    // Refresh panel
                    taskPanel.revalidate();
                    taskPanel.repaint();
                }
            }
        }
    }


    private void startReminder() {
        reminderTimer = new Timer(1000, e -> { // Timer setiap 1 detik
            Date now = new Date(); // Waktu sekarang

            for (TaskItem taskItem : tasks) {
                Date deadline = taskItem.getDeadline();

                // Periksa jika tenggat waktu valid dan belum selesai
                if (deadline != null && !taskItem.getCheckBox().isSelected()) {
                    if (now.after(deadline)) {
                        // Menampilkan dialog pengingat
                        JOptionPane.showMessageDialog(
                                Todo.this,
                                "Reminder: Task \"" + taskItem.getCheckBox().getText() + "\" is overdue!"
                        );
                        taskItem.setDeadline(null); // Hilangkan deadline setelah pengingat
                    } else {
                        // Log waktu untuk debugging (opsional, dapat dihapus)
                        System.out.println("Task: " + taskItem.getCheckBox().getText());
                        System.out.println("Now: " + now);
                        System.out.println("Deadline: " + deadline);
                    }
                }
            }
        });
        reminderTimer.start();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Todo app = new Todo();
            app.setVisible(true);
        });
    }

    // Menyimpan Task
    private static class TaskItem {
        private JCheckBox checkBox;
        private Date deadline;

        public TaskItem(JCheckBox checkBox, Date deadline) {
            this.checkBox = checkBox;
            this.deadline = deadline;
        }

        public JCheckBox getCheckBox() {
            return checkBox;
        }

        public Date getDeadline() {
            return deadline;
        }

        public void setDeadline(Date deadline) {
            this.deadline = deadline;
        }
    }
}