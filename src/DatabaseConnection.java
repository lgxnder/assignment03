import java.sql.*;
import javafx.scene.control.TextArea;

public class DatabaseConnection {

        
    public Connection connectToDB(TextArea area) {
        Connection connection = null;
        
        try {
             connection = DriverManager.getConnection("jdbc:oracle:thin:@calvin.humber.ca:1521:grok", "n01611541", "oracle");
        } catch (SQLException e) {
            System.out.println(e);
            area.appendText("Error Connecting to Database"); // not working for some reason
            System.out.println("Failed connection to Database");
        }
        return connection;
    }
}
