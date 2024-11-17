import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;

import java.sql.*;


public class WorkManagementInterface extends Application {
   
    // instantiate connection
    DatabaseConnection connection = new DatabaseConnection();
    WorkHourDAO dao; // declare dao for CRUD operations


    @Override
    public void start(Stage stage) {
        //// Create a new grid & Text Area ////
        GridPane pane = new GridPane();
        TextArea textArea = new TextArea();
        HBox btnBox = new HBox(10);
        
        // Set grid properties //
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        pane.setHgap(5.5);
        pane.setVgap(5.5);

        // Textfield node declarations 
        TextField IDField = new TextField();
        TextField nameField = new TextField();
        TextField roleField = new TextField();
        TextField hoursField = new TextField();

        //Button Node Declarations
        Button loadButton = new Button("Load Entries");
        Button loadSingle = new Button ("Load Entry By ID");
        Button addButton = new Button("Add Entry");
        Button updateButton = new Button("Modify Entry");
        Button deleteButton = new Button("Delete Entry By ID");
        
        //// Place information nodes
        
        pane.add(new Label("ID: "), 0, 0);
        pane.add(IDField, 1, 0);
        pane.add(new Label("Name: "), 0, 2);
        pane.add(nameField, 1, 2);
        pane.add(new Label("Role: "), 0, 3);
        pane.add(roleField, 1, 3);
        pane.add(new Label("Hours Worked: "), 0, 4);
        pane.add(hoursField, 1, 4);


        // add buttons to button box, and add buttonbox to gridpane
        btnBox.getChildren().addAll(loadSingle, loadButton, addButton, updateButton, deleteButton);
        pane.add(btnBox, 1, 5);

        textArea.setPrefSize(200, 100);
        pane.add(textArea, 1, 6);

        // database connection setup
        Connection con = connection.connectToDB(textArea);
        dao = new WorkHourDAO(con); // initialize dao with the db connection

        // load entries button (read all records)
        loadButton.setOnAction(event -> {
            loadEntries(textArea);
        });

        loadSingle.setOnAction(event -> {
            if (con != null) {
                String idStr = IDField.getText();
                
                if (idStr.isEmpty()) {
                    displayError("ID Must be filled out", textArea);
                    return;
                }

                try {
                    int id = Integer.parseInt(idStr);
                    if (id > 0) {
                        dao.loadEntryByID(id, textArea);
                    } else {
                        displayError("ID Must be greater than 0", textArea);
                        return;
                    }
                    
                } catch (NumberFormatException e) {
                displayError("ID must be an integer.", textArea);
                return;
            }
            } else {
                textArea.appendText("Database Not Connected. Please Connect.");
                return;
            }

        });
        

        // add entries button (create a new record)
        addButton.setOnAction(event -> {
            if (con != null) {
            String idStr = IDField.getText();    
            String name = nameField.getText();
            String role = roleField.getText();
            String hoursStr = hoursField.getText();

            // validate inputs
            boolean valid = validate(idStr, name, role, hoursStr, textArea);
            
            if (valid == false) {
                return;
            }

            int hours;
            int id;
            try {
                hours = Integer.parseInt(hoursStr);
                id = Integer.parseInt(idStr);
                if (hours < 0) {
                    displayError("Hours must be a non-negative number.", textArea);
                    return;
                }
                
                if (id < 1) {
                    displayError("ID Must be a non-negative number and greater than 0", textArea);
                    return;
                }
                
                
            } catch (NumberFormatException e) {
                displayError("Hours and ID must both be integers.", textArea);
                return;
            }

            // create new entry
            WorkHourEntry entry = new WorkHourEntry(id, name, role, hours);
            dao.create(entry, textArea);
            } else {
                textArea.appendText("Database Not Connected. Please Connect.");
            }
            
        });

        // update entry button (modify an existing record)
        updateButton.setOnAction(event -> {
            if (con != null) {
                String idStr = IDField.getText();
                String name = nameField.getText();
                String role = roleField.getText();
                String hoursStr = hoursField.getText();
                
                boolean valid = validate(idStr, name, role, hoursStr, textArea);
                
                if (valid == false) {
                    return;
                }
                
                int id = Integer.parseInt(idStr);
                int hours = Integer.parseInt(hoursStr);
                
                WorkHourEntry entry = new WorkHourEntry(id, name, role, hours);
                dao.update(id, entry, textArea);
 
                
            } else {
                textArea.appendText("Database Not Connected. Please Connect.");
            }
            
        });
        
        deleteButton.setOnAction(event -> {
            if (con != null) {

            } else {
                textArea.appendText("Database Not Connected. Please Connect.");
            }
            // delete entry stuff here
            // find entry by id and delete
        });
        
        
        
        //// Create new scene, put it in the current stage ////
        Scene scene = new Scene(pane);
        stage.setTitle("Work Hours Management System");
        stage.setScene(scene);
        stage.show();
        
        // close connection when stage is closed
        stage.setOnCloseRequest(event -> {
            try {
                if (con != null) {
                   con.close();
                }
            } catch (SQLException e) {
                System.out.println("Error Closing Connection");
            }
        });
        
        if (con == null) {
            stage.setOnCloseRequest(event -> {
            
            });
        }
        
    }

    private boolean validate(String id, String name, String role, String hours, TextArea textArea ) {
        if (name.isEmpty() || role.isEmpty() || hours.isEmpty() || id.isEmpty()) {
            displayError("All fields must be filled out.", textArea);
            return false;
        }
        return true;
    }
    
    // load entries from database
    private void loadEntries(TextArea textArea) {
        textArea.setText("");  // Clear previous entries
        var entries = dao.readAll(textArea);
        for (WorkHourEntry entry : entries) {
            textArea.appendText(entry.getId() + " | " + entry.getName() + " | " + entry.getRole() + " | " + entry.getHours() + "\n");
        }
    }

    // display error messages in textArea
    private void displayError(String message, TextArea textArea) {
        textArea.appendText("Error: " + message + "\n");
    }

    public static void main(String[] args) {
        launch();
    }
}