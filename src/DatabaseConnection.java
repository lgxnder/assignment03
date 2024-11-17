import java.sql.*;
import javafx.scene.control.TextArea;

/// Created by Alexander Stasyna (n01627582) & Andreya De Luca (n01611541) ///

public class DatabaseConnection {

    // define URL, Username, and password for easy changes
    private static final String URL = "jdbc:oracle:thin:@calvin.humber.ca:1521:grok";
    private static final String USERNAME = "n01611541"; // preset to Andreya's user
    private static final String PASSWORD = "oracle";
        
    public Connection connectToDB(TextArea area) { // connect to database
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
