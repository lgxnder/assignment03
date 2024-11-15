//imports

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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import javafx.scene.layout.HBox;
import java.sql.*;


public class WorkManagementInterface extends Application {
   
    // connection Class
    DatabaseConnection connection = new DatabaseConnection();
       
    // move these to its own class
    String name = "";
    String role = "";
    String hoursStr = "";
    
    private void addEntry(TextArea textArea) {

        // validate inputs 
        //**note** replace these with the getters for when the class is made, thanks :D
        if (role.isEmpty() || name.isEmpty() || hoursStr.isEmpty()) {
            displayError(" All fields must be filled out.", textArea);
            return;
        }

        try {  // ensure that hours is a non-negative integer
            if (Integer.parseInt(hoursStr) < 0) {
                displayError("Hours must be a non-negative number.", textArea);
                return;
            }
        } catch (NumberFormatException e) {
            displayError("Hours must be an integer value " + e.getMessage(), textArea);
            return;
        }

        // write to file
        try (PrintWriter writer = new PrintWriter(new FileWriter("work_hours.txt", true))) {
            writer.println(role + " " + name + " " + hoursStr);

            textArea.appendText("Added: " + role + " " + name + " " + hoursStr + "\n");
        } catch (IOException e) {
            displayError("Error writing to file: " + e.getMessage(), textArea);
        }
    }

    private void loadEntries(TextArea textArea) {
        textArea.setText("");
        File file = new File("work_hours.txt");

        // check if file exists
        if (!file.exists()) {
            displayError("File does not exist. Please add entries first.", textArea);
            return;
        }

        // read file, append information to end of file
        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                textArea.appendText(line + "\n");
            }
        } catch (FileNotFoundException e) {
            displayError("Error reading file: " + e.getMessage(), textArea);
        }
    }

    // display error messages in the text area
    private void displayError(String message, TextArea textArea) {
        textArea.appendText("Error: " + message + "\n");
    }

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
        TextField nameField = new TextField();
        TextField roleField = new TextField();
        TextField hoursField = new TextField();

        //Button Node Declarations
        Button loadButton = new Button("Load Entries");
        Button addButton = new Button("Add Entry");
        Button updateButton = new Button("Modify Entries");
        Button deleteButton = new Button("Delete Entry By ID");
        
        //// Place information nodes ------ col, row
        pane.add(new Label("Name: "), 0, 0);
        pane.add(nameField, 1, 0);

        pane.add(new Label("Role: "), 0, 1);
        pane.add(roleField, 1, 1);

        pane.add(new Label("Hours Worked: "), 0, 2);
        pane.add(hoursField, 1, 2);


        // add buttons to button box, and add buttonbox to gridpane
        btnBox.getChildren().addAll(loadButton, addButton, updateButton, deleteButton);
        pane.add(btnBox, 1, 4);

        textArea.setPrefSize(200, 100);
        pane.add(textArea, 1, 5);

        loadButton.setOnAction(event -> {
            // add crud operation here
        });
        addButton.setOnAction(event -> {
            // add crud operation method here
        });
        
        updateButton.setOnAction(event -> {
            // add crud operation method here
        });
        
        deleteButton.setOnAction(event -> {
            // add crud operation method here
        });
        

        //// Create new scene, put it in the current stage ////
        Scene scene = new Scene(pane);
        stage.setTitle("Work Hours Management System");
        stage.setScene(scene);
        stage.show();
        
        // create connection and get returned value
        Connection con = connection.connectToDB(textArea);

        loadEntries(textArea);
        
        
        stage.setOnCloseRequest(event -> { // on Interface close, Close connection
            try {
                if (con != null) {
                   con.close();
                }
            } catch (SQLException e) {
                System.out.println("Error Closing Connection");
            }
        });
        
        
    }

    public static void main(String[] args) throws SQLException {
        
        launch();

    }

}
