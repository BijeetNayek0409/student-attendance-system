import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Main {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Student Attendance System");

        JButton loadBtn = new JButton("Load Students");
        JButton presentBtn = new JButton("Present");
        JButton absentBtn = new JButton("Absent");
        JButton saveBtn = new JButton("Save");
        JButton reportBtn = new JButton("Report");

        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        JTextField dateField = new JTextField("2026-03-30", 10);

        frame.setLayout(new FlowLayout());
        frame.setSize(800, 600);

        presentBtn.setBackground(Color.GREEN);
        absentBtn.setBackground(Color.RED);
        saveBtn.setBackground(Color.CYAN);

        frame.add(dateLabel);
        frame.add(dateField);
        frame.add(loadBtn);
        frame.add(presentBtn);
        frame.add(absentBtn);
        frame.add(saveBtn);
        frame.add(reportBtn);

        final JTable[] table = new JTable[1];

        // LOAD
        loadBtn.addActionListener(e -> {
            table[0] = StudentTable.getTable();
            JScrollPane sp = new JScrollPane(table[0]);
            sp.setPreferredSize(new Dimension(700, 300));
            frame.add(sp);
            frame.revalidate();
        });

        // PRESENT
        presentBtn.addActionListener(e -> {
            if (table[0] != null) {
                int row = table[0].getSelectedRow();
                if (row != -1) {
                    table[0].setValueAt("Present", row, 3);
                }
            }
        });

        // ABSENT
        absentBtn.addActionListener(e -> {
            if (table[0] != null) {
                int row = table[0].getSelectedRow();
                if (row != -1) {
                    table[0].setValueAt("Absent", row, 3);
                }
            }
        });

        // SAVE
        saveBtn.addActionListener(e -> {
            try {
                Connection conn = DBConnection.connect();

                for (int i = 0; i < table[0].getRowCount(); i++) {

                    int studentId = (int) table[0].getValueAt(i, 0);
                    String status = (String) table[0].getValueAt(i, 3);

                    PreparedStatement ps = conn.prepareStatement(
                            "INSERT INTO attendance (student_id, date, status) VALUES (?, ?, ?)"
                    );

                    ps.setInt(1, studentId);
                    ps.setDate(2, java.sql.Date.valueOf(dateField.getText()));
                    ps.setString(3, status);

                    ps.executeUpdate();
                }

                JOptionPane.showMessageDialog(frame, "Saved!");

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // REPORT
        reportBtn.addActionListener(e -> {
            try {
                Connection conn = DBConnection.connect();
                Statement st = conn.createStatement();

                ResultSet rs = st.executeQuery(
                        "SELECT student_id, COUNT(*) total, " +
                                "SUM(CASE WHEN status='Present' THEN 1 ELSE 0 END) present " +
                                "FROM attendance GROUP BY student_id"
                );

                StringBuilder report = new StringBuilder();

                while (rs.next()) {
                    int id = rs.getInt("student_id");
                    int total = rs.getInt("total");
                    int present = rs.getInt("present");

                    double percent = (present * 100.0) / total;

                    report.append("ID: ").append(id)
                            .append(" → ")
                            .append(String.format("%.2f", percent))
                            .append("%\n");
                }

                JOptionPane.showMessageDialog(frame, report.toString());

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}