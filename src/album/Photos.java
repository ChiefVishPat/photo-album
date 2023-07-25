package album;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * @authors Avinash Paluri and Vishal Patel
 *
 * Class that sets up the app
 */

public class Photos extends Application {
    
    
    /** 
     * @param primaryStage
     * 
     * creates the scene from UserLogin.fxml
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML file
            Parent root = FXMLLoader.load(getClass().getResource("/view/UserLogin.fxml"));
            
            // Set up the scene
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Photo Album Login");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    /** 
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
