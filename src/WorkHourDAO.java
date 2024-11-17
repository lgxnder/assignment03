import java.sql.*;
import javafx.scene.control.TextArea;
import java.util.ArrayList;
import java.util.List;

/// Created by Alexander Stasyna (n01627582) & Andreya De Luca (n01611541) ///

public class WorkHourDAO {
	
    private Connection connection; // initialize connection

    public WorkHourDAO(Connection connection) { // get connection 
        this.connection = connection;
    }
    
    // initialize Entry List
    List<WorkHourEntry> entries = new ArrayList<>();

    // insert new entry into database
    public void create(WorkHourEntry entry, TextArea textArea) {
        String query = "INSERT INTO WorkHours (ID, Name, Role, Hours) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            
            try { // check if name is string
                Integer.parseInt(entry.getName());
                textArea.appendText("Name must be a string \n");
                return;
            } catch (NumberFormatException e) {
                stmt.setString(2, entry.getName());
            }

            try { // check if role is String
                Integer.parseInt(entry.getRole());
                textArea.appendText("Role must be a string \n");
                return;
            } catch (NumberFormatException e) {
                stmt.setString(3, entry.getRole());
            }
            
            // set ID and Hours, and add entry to list
            stmt.setInt(1, entry.getId());
            stmt.setInt(4, entry.getHours());
            entries.add(entry);
            
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
        // clear current list of entries to avoid duplication 
        entries.clear();  

        String query = "SELECT * FROM WorkHours";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                // print out all of the results from the entries
                int id = rs.getInt("ID");
                String name = rs.getString("Name");
                String role = rs.getString("Role");
                int hours = rs.getInt("Hours");

                // add entry to list
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
            // check error thrown if the ID doesnt exist in exception
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // extract data from results
                    String name = resultSet.getString("Name");
                    String role = resultSet.getString("Role");
                    int hours = resultSet.getInt("Hours");
                    
                    // add entries to list and display result
                    entries.add(new WorkHourEntry(id, name, role, hours));
                    textArea.appendText(id + " | " + name + " | " + role + " | " + hours + " \n"  );
                } else {
                    textArea.appendText("Entry not found for ID: " + id + "\n");
                }
            } catch (SQLException e) {
                textArea.appendText("Error Loading Entry: " + e.getMessage() + "\n");
            }    
            
        } catch (SQLException e) {
            textArea.appendText("Error Loading Entry " + e.getMessage() + "\n");
        }
    }
    
    
    // update entry in database
    public void update(int id, WorkHourEntry entry, TextArea textArea) {
        String query = "UPDATE WorkHours SET Name = ?, Role = ?, Hours = ? WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            try  { // validate name 
                Integer.parseInt(entry.getName());
                textArea.appendText("Name must be a string \n");
                return;
            } catch (NumberFormatException e) {
                stmt.setString(1, entry.getName());
            }
            
            try { // validate role
                Integer.parseInt(entry.getRole());
                textArea.appendText("Role must be a string \n");
                return;
            } catch (NumberFormatException e) {
                stmt.setString(2, entry.getRole());
            }
            
            // set ID and Hours
            stmt.setInt(3, entry.getHours());
            stmt.setInt(4, id);
                        
            int rowsAffected = stmt.executeUpdate(); // execute
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