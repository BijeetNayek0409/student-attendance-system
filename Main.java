import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Main {

    public static void main(String[] args) {

        DBConnection.connect();

        JFrame frame = new JFrame("Student Attendance System");
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout());


        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JTextField nameField = new JTextField(10);
        JTextField classField = new JTextField(5);

        JButton addStudentBtn = new JButton("Add Student");
        JButton presentBtn = new JButton("Present");
        JButton absentBtn = new JButton("Absent");
        JButton saveBtn = new JButton("Save Attendance");

        JTextField dateField = new JTextField("2026-03-30", 10);

        topPanel.add(new JLabel("Name:"));
        topPanel.add(nameField);

        topPanel.add(new JLabel("Class:"));
        topPanel.add(classField);

        topPanel.add(addStudentBtn);
        topPanel.add(presentBtn);
        topPanel.add(absentBtn);
        topPanel.add(saveBtn);

        topPanel.add(new JLabel("Date:"));
        topPanel.add(dateField);

       
        JTable table = StudentTable.getTable();
        JScrollPane scrollPane = new JScrollPane(table);

        // 🔷 ADD TO FRAME
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        // 🔥 BUTTON LOGIC

        presentBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                table.setValueAt("Present", row, 3);
            }
        });

        absentBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                table.setValueAt("Absent", row, 3);
            }
        });

        saveBtn.addActionListener(e -> {
            try {
                Connection conn = DBConnection.connect();

                for (int i = 0; i < table.getRowCount(); i++) {

                    int studentId = (int) table.getValueAt(i, 0);
                    String status = (String) table.getValueAt(i, 3);

                    PreparedStatement ps = conn.prepareStatement(
                            "INSERT INTO attendance (student_id, date, status) VALUES (?, ?, ?)"
                    );

                    ps.setInt(1, studentId);
                    ps.setDate(2, java.sql.Date.valueOf(dateField.getText()));
                    ps.setString(3, status);

                    ps.executeUpdate();
                }

                JOptionPane.showMessageDialog(frame, "Attendance Saved!");

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        addStudentBtn.addActionListener(e -> {
            try {
                String name = nameField.getText();
                String className = classField.getText();

                if (name.isEmpty() || className.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Enter all fields!");
                    return;
                }

                Connection conn = DBConnection.connect();

                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO students (name, class) VALUES (?, ?)"
                );

                ps.setString(1, name);
                ps.setString(2, className);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(frame, "Student Added!");

                StudentTable.loadStudents();

                nameField.setText("");
                classField.setText("");

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
