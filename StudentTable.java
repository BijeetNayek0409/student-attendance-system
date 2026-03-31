import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class StudentTable {

    public static JTable getTable() {

        String[] columns = {"ID", "Name", "Class", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        try {
            Connection conn = DBConnection.connect();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM public.students");

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("class"),
                        "Absent"
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new JTable(model);
    }
}