package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import model.UserList;

/**
 * @authors Avinash Paluri and Vishal Patel
 *
 * Class that controls login fxml
 */

public class LoginController {
    
    @FXML
    private TextField userLogin;
    
    @FXML
    private Button loginButton;
    
    private UserList users;

    /** 
     * gives controller ability to access all users
     */
    private void loadUsers() {
        users.load();
    }

    /** 
     * includes all setOnAction methods for all the buttons/textfields
     */
    @FXML
    private void initialize() {
        users = new UserList();
        loadUsers();
        loginButton.setOnAction(e -> {
            String username = userLogin.getText();
            try {
                if (username.equals("admin")) {
                    // Perform login logic with the entered username
                    // For example, display a welcome message or switch to another scene
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AdminHome.fxml"));
                    Parent root = loader.load();
                    //AdminController controller = loader.getController();
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                    return;
                }
                User user = users.getUser(username);
                if (user != null) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserHome.fxml"));
                    Parent root = (Parent) loader.load();
                    UserController controller = loader.<UserController>getController();
                    controller.setUser(user);
                    controller.setUserList(users);
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    controller.start(stage);
                    stage.setScene(scene);
                    stage.show();
                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Invalid Login");
                    alert.setHeaderText("Invalid username");
                    alert.setContentText("Please enter a valid username");
                    alert.showAndWait();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
    
}
