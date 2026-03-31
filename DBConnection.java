import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection connect() {
        try {
            Class.forName("org.postgresql.Driver");

            Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5433/Attendance_app",
                    "postgres",
                    "......"
            );

            return conn;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}