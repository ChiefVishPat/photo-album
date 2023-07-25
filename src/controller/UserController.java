package controller;

import java.io.IOException;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Album;
import model.User;
import model.UserList;

/**
 * @authors Avinash Paluri and Vishal Patel
 *
 * Class that controls user fxml
 */

public class UserController {
    
    @FXML
    private ListView<Album> albumList;

    @FXML
    private Button createButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button renameButton;

    @FXML
    private Button searchButton;

    @FXML
    private Text albumNameText;

    @FXML
    private Text numPhotosText;

    @FXML
    private Text dateRangeText;

    @FXML
    private Button openButton;

    @FXML
    private Button logoutButton;

    private User user;

    private UserList users;

    /** 
     * @param users
     * 
     * gives controller ability to access all users
     */
    public void setUserList(UserList users) {
        this.users=users;
    }
    
    
    /** 
     * @param user
     * 
     * gives controller ability to access user in which the search was open from
     */
    public void setUser(User user) {
        this.user=user;
    }
    
    /** 
     * @param stage
     * 
     * includes all setOnAction methods for all the buttons/textfields
     */
    @FXML
    public void start(Stage stage) {
        
        albumList.getItems().addAll(user.getAlbums());     
        albumList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                albumNameText.setText(newSelection.toString());
                numPhotosText.setText(Integer.toString(newSelection.getPhotos().size()) + " photos");
                dateRangeText.setText("Date range: " + newSelection.getDateRange());
                openButton.setOnAction(e -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/OpenAlbum.fxml"));
                        Parent root = loader.load();
                        AlbumController controller = loader.getController();
                        // Pass the User object to the AdminController if needed
                        controller.setUsers(users);
                        controller.setAlbum(newSelection);
                        controller.setUser(user);
                        Scene scene = new Scene(root);
                        Stage sta = (Stage) openButton.getScene().getWindow();
                        controller.start(sta);
                        sta.setScene(scene);
                        sta.show();
                    } catch(IOException ex) {
                        ex.printStackTrace();
                    }
                });
            }
        });

        createButton.setOnAction(event -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Create New Album");
            dialog.setHeaderText("Enter the name of the new album:");
        
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                String albumName = result.get();
        
                Album newAlbum = new Album(albumName);
                user.addAlbum(newAlbum);
                users.save();
        
                albumList.getItems().add(newAlbum);
            }
        });
        deleteButton.setOnAction(event -> {
                Album selectedAlbum = albumList.getSelectionModel().getSelectedItem();
                if (selectedAlbum != null) {
                    Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmAlert.setTitle("Delete Album");
                    confirmAlert.setHeaderText("Delete Album: " + selectedAlbum.toString());
                    confirmAlert.setContentText("Are you sure you want to delete this album and all its contents?");
        
                    Optional<ButtonType> result = confirmAlert.showAndWait();
                    if (result.get() == ButtonType.OK){
                        user.removeAlbum(selectedAlbum);
                        albumList.getItems().remove(selectedAlbum);
                        users.save();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("No Album Selected");
                    alert.setHeaderText("No Album Selected");
                    alert.setContentText("Please select an album to delete.");
                    alert.showAndWait();
                }
        });
        
        renameButton.setOnAction(event -> {
            Album selectedAlbum = albumList.getSelectionModel().getSelectedItem();
            if (selectedAlbum != null) {
                TextInputDialog dialog = new TextInputDialog(selectedAlbum.toString());
                dialog.setTitle("Rename Album");
                dialog.setHeaderText(null);
                dialog.setContentText("Enter new album name:");
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent() && !result.get().isEmpty()) {
                    String newName = result.get();
                    boolean albumExists = user.getAlbums().stream().anyMatch(album -> album.toString().equals(newName));
                    if (!albumExists) {
                        selectedAlbum.changeName(newName);
                        albumList.refresh();
                        users.save();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Duplicate Album Name");
                        alert.setHeaderText(null);
                        alert.setContentText("An album with that name already exists.");
                        alert.showAndWait();
                    }
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
                Stage sta = (Stage) logoutButton.getScene().getWindow();
                sta.setScene(scene);
                sta.show();
                return;
            }catch(IOException ex) {
                ex.printStackTrace();
            }
        });

        searchButton.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SearchPhotos.fxml"));
                Parent root = loader.load();
                SearchController controller = loader.getController();
                // Pass the User object to the AdminController if needed
                controller.setUsers(users);
                controller.setUser(user);
                Scene scene = new Scene(root);
                Stage sta = (Stage) openButton.getScene().getWindow();
                controller.start(sta);
                sta.setScene(scene);
                sta.show();
            } catch(IOException ex) {
                ex.printStackTrace();
            }          
        });

    }
}