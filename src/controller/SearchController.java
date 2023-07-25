package controller;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.Tag;
import model.User;
import model.UserList;

/**
 * @authors Avinash Paluri and Vishal Patel
 *
 * Class that controls search fxml
 */

public class SearchController {
    @FXML private TextField fromDateField;
    @FXML private TextField toDateField;
    @FXML private TextField tagValue1TextField;
    @FXML private RadioButton andRadioButton;
    @FXML private RadioButton orRadioButton;
    @FXML private TextField tagValue2TextField;
    @FXML private VBox tagSearchBox;
    @FXML private Button dateSearchButton;
    @FXML private Button tagSearchButton;
    @FXML private Button createAlbumButton;
    @FXML private VBox searchOptionsBox;
    @FXML private ScrollPane photoScrollPane;
    @FXML private VBox photoVBox;
    @FXML private MenuItem backButton;
    @FXML private MenuItem logoutButton;
    
    private UserList users;
    private User user;
    private List<Photo> filteredPhotos = new ArrayList<>();
    private ToggleGroup toggleGroup;
    
    /** 
     * @param users
     * 
     * gives controller ability to access all users
     */
    public void setUsers(UserList users) {
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
        toggleGroup = new ToggleGroup();
        andRadioButton.setToggleGroup(toggleGroup);
        orRadioButton.setToggleGroup(toggleGroup);
        // Set the action for the back button
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserHome.fxml"));
                    Parent root = loader.load();
                    UserController controller = loader.getController();
                    controller.setUserList(users);
                    controller.setUser(user);
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) backButton.getParentPopup().getOwnerWindow();
                    controller.start(stage);
                    stage.setScene(scene);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Set the action for the logout button
        logoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserLogin.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
                    stage.setScene(scene);
                    stage.show();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //set the action for the date search button
        dateSearchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Get the start and end dates from the text fields
                String fromDateStr = fromDateField.getText();
                String toDateStr = toDateField.getText();
        
                Calendar fromDate = Calendar.getInstance();
                Calendar toDate = Calendar.getInstance();
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    Date from = sdf.parse(fromDateStr);
                    Date to = sdf.parse(toDateStr);
                    fromDate.setTime(from);
                    toDate.setTime(to);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Enter the date in the format: 'MM/DD/YYYY'.");
                    alert.showAndWait();
                    return;
                }
                
                // Filter the photos based on the dates
                try{
                    filteredPhotos.clear();
                }catch(Exception e){}
                for (User user : users.getUserList()) {
                    for (Photo photo : user.getPhotos()) {
                        Calendar photoDate = photo.getDate();
                        if ((photoDate.before(toDate)) && photoDate.after(fromDate) 
                            || (photoDate.equals(toDate) || photoDate.equals(fromDate))) {
                            filteredPhotos.add(photo);
                        }
                    }
                }
        
                // Display the filtered photos in the scroll pane
                photoVBox.getChildren().clear();
                for (Photo photo : filteredPhotos) {
                    ImageView imageView = new ImageView(photo.getImage());
                    imageView.setFitWidth(200);
                    imageView.setFitHeight(200);
                    photoVBox.getChildren().add(imageView);
                }
                photoScrollPane.setContent(photoVBox);
            }
        });
        
        //set the action for the tag search button
        tagSearchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String tag1 = tagValue1TextField.getText().trim().toLowerCase();
                String tag2 = tagValue2TextField.getText().trim().toLowerCase();
            
                if (tag1.isEmpty() && tag2.isEmpty()) {
                    return;
                }
                
                try{
                    filteredPhotos.clear();
                }catch(Exception e){}

                if (!tag1.isEmpty() && tag2.isEmpty()) {
                    for (User user : users.getUserList()) {
                        for (Photo photo : user.getPhotos()) {
                            if (photo.hasTag(tag1)) {
                                filteredPhotos.add(photo);
                            }
                        }
                    }
                } else if (tag1.isEmpty() && !tag2.isEmpty()) {
                    for (User user : users.getUserList()) {
                        for (Photo photo : user.getPhotos()) {
                            if (photo.hasTag(tag2)) {
                                filteredPhotos.add(photo);
                            }
                        }
                    }
                } else {
                    boolean isAndSelected = andRadioButton.isSelected();
                    boolean isOrSelected = orRadioButton.isSelected();
            
                    if (isAndSelected) {
                        for (User user : users.getUserList()) {
                            for (Photo photo : user.getPhotos()) {
                                if (photo.hasTag(tag1) && photo.hasTag(tag2)) {
                                    filteredPhotos.add(photo);
                                }
                            }
                        }
                    } else if (isOrSelected) {
                        for (User user : users.getUserList()) {
                            for (Photo photo : user.getPhotos()) {
                                if (photo.hasTag(tag1) || photo.hasTag(tag2)) {
                                    filteredPhotos.add(photo);
                                }
                            }
                        }
                    } else {
                        return;
                    }
                }

                // Display the filtered photos in the scroll pane
                photoVBox.getChildren().clear();
                for (Photo photo : filteredPhotos) {
                    ImageView imageView = new ImageView(photo.getImage());
                    imageView.setFitWidth(200);
                    imageView.setFitHeight(200);
                    photoVBox.getChildren().add(imageView);
                }
                photoScrollPane.setContent(photoVBox);
            }
        });

        createAlbumButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Get the name of the new album from the user
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Create New Album");
                dialog.setHeaderText(null);
                dialog.setContentText("Please enter the name of the new album:");
        
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    String albumName = result.get();
        
                    // Create a new album
                    Album newAlbum = new Album(albumName);
        
                    // Add all the photos from filteredPhotos to the new album
                    for (Photo photo : filteredPhotos) {
                        newAlbum.addPhoto(photo);
                    }
        
                    // Add the new album to the current user's list of albums
                    user.addAlbum(newAlbum);
        
                    // Display a confirmation message to the user
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Album Created");
                    alert.setHeaderText(null);
                    alert.setContentText("The album \"" + albumName + "\" has been created.");
        
                    alert.showAndWait();
                }
            }
        });
        
    }
}
