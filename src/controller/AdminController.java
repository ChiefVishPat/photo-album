package controller;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import model.User;
import model.UserList;

/**
 * @authors Avinash Paluri and Vishal Patel
 *
 * Class that controls admin fxml
 */

public class AdminController {
    
    @FXML
    private ListView<User> userList;
    
    @FXML
    private TextField userInput;
    
    @FXML
    private Button addButton;
    
    @FXML
    private Button deleteButton;

    @FXML
    private Button logoutButton;
    
    private UserList users;

    /** 
     * @param users
     * 
     * gives controller ability to access all users
     */
    private void loadUsers() {
        users.load();
        ObservableList<User> userListData = FXCollections.observableArrayList(users.getUserList());
        userList.setItems(userListData);
    }
    
   /** 
     * includes all setOnAction methods for all the buttons/textfields
     */
    @FXML
    private void initialize() {
        users = new UserList();
        loadUsers();
        addButton.setOnAction(e -> {
            String username = userInput.getText();
            if (username.trim().isEmpty()) {
                Alert alert = new Alert(AlertType.ERROR, "Please enter a username.");
                alert.showAndWait();
                return;
            }
            if (users.getUserList().stream().anyMatch(user -> user.getUsername().equalsIgnoreCase(username))) {
                Alert alert = new Alert(AlertType.ERROR, "Username already exists.");
                alert.showAndWait();
                return;
            }
            User newUser = new User(username);
            users.addUser(newUser);
            users.save();
            loadUsers();
            userInput.setText("");
        });
        
        deleteButton.setOnAction(e -> {
            User selectedUser = userList.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                if (!selectedUser.getUsername().equals("admin")) {
                    users.removeUser(selectedUser);
                    users.save();
                    loadUsers();
                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("You cannot delete the admin user.");
                    alert.showAndWait();
                }
            }
        });

        logoutButton.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserLogin.fxml"));
                Parent root = loader.load();
                LoginController controller = loader.getController();
                // Pass the User object to the AdminController if needed
                // controller.setUser(u);
                Scene scene = new Scene(root);
                Stage stage = (Stage) logoutButton.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
                return;
            }catch(IOException ex) {
                ex.printStackTrace();
            }
        });
    }
    
}
