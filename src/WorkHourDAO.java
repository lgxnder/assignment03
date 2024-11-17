import java.sql.*;
import javafx.scene.control.TextArea;
import java.util.ArrayList;
import java.util.List;

public class WorkHourDAO {
	
    private Connection connection;

    public WorkHourDAO(Connection connection) {
        this.connection = connection;
    }

    // insert new entry into database
    public void create(WorkHourEntry entry, TextArea textArea) {
        String query = "INSERT INTO WorkHours (ID, Name, Role, Hours) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, entry.getId());
            stmt.setString(2, entry.getName());
            stmt.setString(3, entry.getRole());
            stmt.setInt(4, entry.getHours());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                textArea.appendText("Entry added successfully.\n");
            }
        } catch (SQLException e) {
            textArea.appendText("Error adding entry: " + e.getMessage() + "\n");
        }
    }

    // read all entries from database
    public List<WorkHourEntry> readAll(TextArea textArea) {
        List<WorkHourEntry> entries = new ArrayList<>();
        String query = "SELECT * FROM WorkHours";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("Name");
                String role = rs.getString("Role");
                int hours = rs.getInt("Hours");
                entries.add(new WorkHourEntry(id, name, role, hours));
            }
        } catch (SQLException e) {
            textArea.appendText("Error fetching entries: " + e.getMessage() + "\n");
        }
        return entries;
    }

    public void loadEntryByID(int id, TextArea textArea) {
        String query = "SELECT * FROM WorkHours Where id = ?";
         
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery(query);
            // check error thrown if the ID doesnt exist in exception
        } catch (SQLException e) {
            textArea.appendText("Error Loading Entry " + e.getMessage());
        }
        
    }
    
    
    // update entry in database
    public void update(int id, WorkHourEntry entry, TextArea textArea) {
        String query = "UPDATE WorkHours SET Name = ?, Role = ?, Hours = ? WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, entry.getName());
            stmt.setString(2, entry.getRole());
            stmt.setInt(3, entry.getHours());
            stmt.setInt(4, id);
            
            System.out.println(entry.getName());
            System.out.println(entry.getRole());
            System.out.println(entry.getHours());
            System.out.println(id);
            
            int rowsAffected = stmt.executeUpdate();

            
            if (rowsAffected > 0) {
                textArea.appendText("Entry updated successfully.\n");
            }
        } catch (SQLException e) {
            textArea.appendText("Error updating entry: " + e.getMessage() + "\n");
        }
    }

    // remove entry in database by id
    public void delete(int id, TextArea textArea) {
        String query = "DELETE FROM WorkHours WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);

            // execute deletion
            int rowsAffected = stmt.executeUpdate();

            // check if rows affected
            if (rowsAffected > 0) {
                textArea.appendText("Entry with ID " + id + " deleted successfully.\n");
            } else {
                // if no rows were affected
                textArea.appendText("No entry found with ID " + id + ".\n");
            }
        } catch (SQLException e) {
            textArea.appendText("Error deleting entry: " + e.getMessage() + "\n");
        }
    }
}