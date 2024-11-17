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


/// Created by Alexander Stasyna (n01627582) & Andreya De Luca (n01611541) ///

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

        // load single entry by ID event
        loadSingle.setOnAction(event -> {
            if (con != null) { // check if connection is null

                String idStr = IDField.getText();
                
                // check if ID is empty
                if (idStr.isEmpty()) {
                    displayError("ID Must be filled out", textArea);
                    return;
                }

                // check if ID is greater than zero, and read ID
                try {
                    int id = Integer.parseInt(idStr);
                    if (id > 0) {
                        textArea.setText("");
                        dao.loadEntryByID(id, textArea);
                    } else {
                        displayError("ID Must be greater than 0", textArea);
                        return;
                    }
                    
                } catch (NumberFormatException e) { // catch error
                displayError("ID must be an integer.", textArea);
                return;
            }
            } else { // throw not connected error
                textArea.appendText("Database Not Connected. Please Connect.");
                return;
            }

        });
        

        // add entries button (create a new record)
        addButton.setOnAction(event -> {
            if (con != null) { // check if connection is null
                
            // extract data from each input field    
            String idStr = IDField.getText();    
            String name = nameField.getText();
            String role = roleField.getText();
            String hoursStr = hoursField.getText();

            // validate if inputs are initially empty
            boolean valid = validate(idStr, name, role, hoursStr, textArea);
            if (valid == false) {
                return;
            }

            int hours;
            int id;
            try { // check if hours and ID are valid
                hours = Integer.parseInt(hoursStr);
                id = Integer.parseInt(idStr);
                if (hours < 0) {
                    displayError("Hours must be a non-negative number.", textArea);
                    return;
                }
                
                if (id < 0) {
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
            if (con != null) { // check if connection is null
                
                // extract data from each input field
                String idStr = IDField.getText();
                String name = nameField.getText();
                String role = roleField.getText();
                String hoursStr = hoursField.getText();
                
                // validate if input fields are empty
                boolean valid = validate(idStr, name, role, hoursStr, textArea);
                if (valid == false) {
                    return;
                }
                
                // get ID and hours
                
                int id = Integer.parseInt(idStr);
                int hours = Integer.parseInt(hoursStr);
                
                try { // check if hours and ID are valid
                    if (hours < 0) {
                        displayError("Hours must be a non-negative number.", textArea);
                        return;
                    }

                    if (id < 0) {
                        displayError("ID Must be a non-negative number and greater than 0", textArea);
                        return;
                    }

                } catch (NumberFormatException e) {
                    displayError("Hours and ID must both be integers.", textArea);
                    return;
                }
                
                // update entry
                WorkHourEntry entry = new WorkHourEntry(id, name, role, hours);
                dao.update(id, entry, textArea);
 
                IDField.clear();
                nameField.clear();
                roleField.clear();
                hoursField.clear();
            } else {
                textArea.appendText("Database Not Connected. Please Connect.");
            }
            
        });
        
        
        // delete button event
        deleteButton.setOnAction(event -> {
            if (con != null) { // check if connection is null
                // get id from input field
                String idStr = IDField.getText();

                // validate id is not empty
                if (idStr.isEmpty()) {
                    displayError("ID must be filled out.", textArea);
                    return;
                }

                try { // validate id is greater than 0
                    int id = Integer.parseInt(idStr);
                    if (id < 0) {
                        displayError("ID must be greater than 0.", textArea);
                        return;
                    }
        
                    // call dao to delete record
                    dao.delete(id, textArea);
        
                } catch (NumberFormatException e) {
                    displayError("ID must be a valid integer.", textArea);
                }

            } else {
                textArea.appendText("Database Not Connected. Please Connect.");
            }
        });
        
        
        
        //// Create new scene, put it in the current stage ////
        Scene scene = new Scene(pane);
        stage.setTitle("Work Hours Management System");
        stage.setScene(scene);
        stage.show();
        
        // close connection when stage (Interface) is closed
        stage.setOnCloseRequest(event -> {
            try {
                if (con != null) {
                   con.close();
                }
            } catch (SQLException e) {
                System.out.println("Error Closing Connection");
            }
        });
    }

    // validate if all input fields are empty
    private boolean validate(String id, String name, String role, String hours, TextArea textArea ) {
        if (name.isEmpty() || role.isEmpty() || hours.isEmpty() || id.isEmpty()) {
            displayError("All fields must be filled out.", textArea);
            return false;
        }
        return true;
    }
    
    // load entries from database
    private void loadEntries(TextArea textArea) {
        textArea.setText("");  // clear previous entries
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
        launch(); // launch application
    }
}