import java.sql.*;
import javafx.scene.control.TextArea;

public class DatabaseConnection {

    private static final String URL = "jdbc:oracle:thin:@calvin.humber.ca:1521:grok";
    private static final String USERNAME = "n01611541";
    private static final String PASSWORD = "oracle";
        
    public Connection connectToDB(TextArea area) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.out.println(e);
            area.appendText("Error Connecting to Database: " + e.getMessage() + "\n");
        }
        return connection;
    }
}
