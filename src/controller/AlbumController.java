package controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Album;
import model.Photo;
import model.Tag;
import model.User;
import model.UserList;

/**
 * @authors Avinash Paluri and Vishal Patel
 *
 * Class that controls album fxml
 */

public class AlbumController {
    
    @FXML
    private BorderPane rootPane;
    
    @FXML
    private Button addButton;
    
    @FXML
    private Button removeButton;
    
    @FXML
    private Button captionButton;
    
    @FXML
    private Button tagButton;

    @FXML
    private Button removeTagButton;
    
    @FXML
    private Button copyButton;
    
    @FXML
    private Button moveButton;
    
    @FXML
    private Button slideshowButton;
    
    @FXML
    private ListView<Photo> photoListView;
    
    @FXML
    private ImageView photoImageView;
    
    @FXML
    private TextArea captionTextArea;
    
    @FXML
    private Label dateLabel;
    
    @FXML
    private TextArea tagsTextArea;

    @FXML
    private MenuItem backButton;

    @FXML
    private MenuItem logoutButton;

    private UserList users;
    private User user;
    private Album album;
    
    /** 
     * @param users
     * 
     * gives controller ability to access all users
     */
    public void setUsers(UserList users) {
        this.users=users;
    }
    
    /** 
     * @param album
     * 
     * gives controller ability to access selected album
     */
    public void setAlbum(Album album) {
        this.album=album;
    }
    
    /** 
     * @param user
     * 
     * gives controller ability to access user in which the album was open from
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
        // Display photos in the ListView
        ObservableList<Photo> photos = FXCollections.observableArrayList(album.getPhotos());
        photoListView.setItems(photos);
        photoListView.setCellFactory(new Callback<ListView<Photo>, ListCell<Photo>>() {
            @Override
            public ListCell<Photo> call(ListView<Photo> list) {
                return new ListCell<Photo>() {
                    private final ImageView imageView = new ImageView();
                    {
                        imageView.setFitHeight(50);
                        imageView.setFitWidth(50);
                    }
    
                    @Override
                    protected void updateItem(Photo photo, boolean empty) {
                        super.updateItem(photo, empty);
                        if (empty || photo == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(photo.toString());
                            imageView.setImage(photo.getImage());
                            setGraphic(imageView);
                        }
                    }
                };
            }
        });
    
        // Handle adding a photo
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Image File");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"));
                File selectedFile = fileChooser.showOpenDialog(stage);
                if (selectedFile != null) {
                    try {
                        // Create a new Photo object and add it to the album
                        Image image = new Image(selectedFile.toURI().toString());
                        String name = selectedFile.getName();
                        Calendar date = Calendar.getInstance();
                        date.setTimeInMillis(selectedFile.lastModified());
                        Photo photo = new Photo(name, image, date);
                        album.addPhoto(photo);
    
                        // Update the ListView
                        photos.add(photo);
    
                        // Save changes
                        users.save();
                    } catch (Exception e) {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Could not open file");
                        alert.setContentText("The selected file could not be opened.");
                        alert.showAndWait();
                    }
                }
            }
        });
    
        // Handle removing a photo
        removeButton.setOnAction(event -> {
            int selectedIndex = photoListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                // Remove the selected photo from the album
                Photo photo = album.getPhotos().get(selectedIndex);
                album.removePhoto(photo);

                // Update the ListView
                photos.remove(selectedIndex);

                // Remove the image and its texts from the scroll pane
                if (photoImageView.getImage() == photo.getImage()) {
                    photoImageView.setImage(null);
                    captionTextArea.setText("");
                    dateLabel.setText("");
                }

                // Save changes
                users.save();
            } else {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("No selection");
                alert.setHeaderText("No photo selected");
                alert.setContentText("Please select a photo to remove.");
                alert.showAndWait();
            }
        });

    
        // Show selected photo in ImageView and update caption and date label
        photoListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Photo>() {
            @Override
            public void changed(ObservableValue<? extends Photo> observable, Photo oldValue, Photo newValue) {
                if (newValue != null) {
                    photoImageView.setImage(newValue.getImage());
                    captionTextArea.setText(newValue.getCaption());
                    
                    // Format the date to mm/dd/yyyy
                    SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
                    String formattedDate = dateFormatter.format(newValue.getDate().getTime());
                    dateLabel.setText(formattedDate);

                    StringBuilder sb = new StringBuilder();
                    for (Tag t : newValue.getTags()) {
                        sb.append(t.toString()).append("\n");
                    }
                    tagsTextArea.setText(sb.toString());
                }
            }
        });

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
                    Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
                    controller.start(stage);
                    stage.setScene(scene);
                    stage.show();
                } catch(Exception e) {
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

        // Handle tag button action
        tagButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int selectedIndex = photoListView.getSelectionModel().getSelectedIndex();
                if (selectedIndex >= 0) {
                    // Prompt the user to choose a tag name
                    ChoiceDialog<String> dialog = new ChoiceDialog<>();
                    dialog.setTitle("Add Tag");
                    dialog.setHeaderText("Choose a tag name");
                    dialog.setContentText("Tag name:");
                    ObservableList<String> tagNames = FXCollections.observableArrayList("Location", "Person", "Event");
                    dialog.getItems().addAll(tagNames);
                    dialog.getItems().add("Other");
                    Optional<String> result = dialog.showAndWait();
                    if (result.isPresent()) {
                        String tagName = result.get();
                        String tagValue = "";
                        // Prompt the user to enter a tag value if they chose "Other" or entered a custom tag name
                        if (tagName.equals("Other")) {
                            TextInputDialog valueDialog = new TextInputDialog();
                            valueDialog.setTitle("Add Tag");
                            valueDialog.setHeaderText("Enter a tag name and value");
                            valueDialog.setContentText("Tag name:value");
                            Optional<String> valueResult = valueDialog.showAndWait();
                            if (valueResult.isPresent()) {
                                String[] parts = valueResult.get().split(":");
                                tagName = parts[0];
                                tagValue = parts[1];
                            } else {
                                return;
                            }
                        } else {
                            // Prompt the user to enter a tag value
                            TextInputDialog valueDialog = new TextInputDialog();
                            valueDialog.setTitle("Add Tag");
                            valueDialog.setHeaderText("Enter a value for the tag \"" + tagName + "\"");
                            valueDialog.setContentText("Value:");
                            Optional<String> valueResult = valueDialog.showAndWait();
                            if (valueResult.isPresent()) {
                                tagValue = valueResult.get();
                            } else {
                                return;
                            }
                        }

                        // Create a new Tag object and add it to the selected photo
                        Tag tag = new Tag(tagName, tagValue);
                        Photo photo = album.getPhotos().get(selectedIndex);
                        if(tagName.toLowerCase().equals("location"))
                            for(Tag t : photo.getTags()) {
                                if(t.getName().toLowerCase().equals("location")) {
                                    Alert alert = new Alert(AlertType.WARNING);
                                    alert.setTitle("Tag Exists");
                                    alert.setHeaderText("The Location Tag Already Exists");
                                    alert.setContentText("The Tag Already Exists");
                                    alert.showAndWait();
                                    return;
                                }
                            }
                        if (photo.getTags().contains(tag)) {
                            Alert alert = new Alert(AlertType.WARNING);
                            alert.setTitle("Tag Exists");
                            alert.setHeaderText("The Tag Already Exists");
                            alert.setContentText("The Tag Already Exists");
                            alert.showAndWait();
                        } else {
                            photo.addTag(tag, user);

                            // Update the tagsTextArea
                            StringBuilder sb = new StringBuilder();
                            for (Tag t : photo.getTags()) {
                                sb.append(t.toString()).append("\n");
                            }
                            tagsTextArea.setText(sb.toString());
                        }

                        // Save changes
                        users.save();
                    }
                } else {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("No selection");
                    alert.setHeaderText("No photo selected");
                    alert.setContentText("Please select a photo to add a tag to.");
                    alert.showAndWait();
                }
            }
        });

        removeTagButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Get the currently selected photo
                int selectedIndex = photoListView.getSelectionModel().getSelectedIndex();
                if(selectedIndex < 0)
                    return;
                Photo selectedPhoto = album.getPhotos().get(selectedIndex);
                
                // Get the list of tags on the selected photo
                List<Tag> tags = selectedPhoto.getTags();
                
                // Create a ChoiceDialog to display the list of tags
                ChoiceDialog<Tag> dialog = new ChoiceDialog<>(tags.get(0), tags);
                dialog.setTitle("Remove Tag");
                dialog.setHeaderText("Select a tag to remove from the photo:");
                dialog.setContentText("Tag:");
                
                // Show the dialog and wait for the user to make a selection
                Optional<Tag> result = dialog.showAndWait();
                
                // If the user selected a tag, remove it from the photo
                result.ifPresent(tag -> {
                    selectedPhoto.removeTag(tag, user);
                });
                StringBuilder sb = new StringBuilder();
                for (Tag t : selectedPhoto.getTags()) {
                    sb.append(t.toString()).append("\n");
                }
                tagsTextArea.setText(sb.toString());
                users.save();
            }
        });


        // Handle caption button action
        captionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int selectedIndex = photoListView.getSelectionModel().getSelectedIndex();
                if (selectedIndex >= 0) {
                    // Prompt the user to enter a new caption
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Edit Caption");
                    dialog.setHeaderText("Enter a new caption");
                    dialog.setContentText("Caption:");
                    Optional<String> result = dialog.showAndWait();
                    if (result.isPresent()) {
                        // Set the new caption for the selected photo
                        Photo photo = album.getPhotos().get(selectedIndex);
                        photo.setCaption(result.get(), user);

                        // Update the caption text area and save changes
                        captionTextArea.setText(result.get());
                        users.save();
                    }
                } else {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("No selection");
                    alert.setHeaderText("No photo selected");
                    alert.setContentText("Please select a photo to edit the caption of.");
                    alert.showAndWait();
                }
            }
        });


        // Handle move button action
        moveButton.setOnAction(event -> {
            // Get selected photos
            ObservableList<Photo> selectedPhotos = photoListView.getSelectionModel().getSelectedItems();

            // Check if any photos are selected
            if (selectedPhotos.isEmpty()) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("No selection");
                alert.setHeaderText("No photos selected");
                alert.setContentText("Please select photos to move.");
                alert.showAndWait();
                return;
            }

            // Prompt the user to select a destination album
            ChoiceDialog<Album> dialog = new ChoiceDialog<>(user.getAlbums().get(0), user.getAlbums());
            dialog.setTitle("Move Photos");
            dialog.setHeaderText("Select a destination album");
            dialog.setContentText("Album:");
            Optional<Album> result = dialog.showAndWait();

            if (result.isPresent()) {
                Album destAlbum = result.get();

                // Move the selected photos to the destination album
                for (Photo photo : selectedPhotos) {
                    album.getPhotos().remove(photo);
                    destAlbum.addPhoto(photo);

                    // Remove photo from the photoListView
                    photoListView.getItems().remove(photo);
                }

                // Save changes
                users.save();

                // Update the photoListView
                photoListView.setItems(FXCollections.observableArrayList(album.getPhotos()));
            }
        });


        // Handle copy button action
        copyButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Get selected photos
                ObservableList<Photo> selectedPhotos = photoListView.getSelectionModel().getSelectedItems();

                // Check if any photos are selected
                if (selectedPhotos.isEmpty()) {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("No selection");
                    alert.setHeaderText("No photos selected");
                    alert.setContentText("Please select photos to copy.");
                    alert.showAndWait();
                    return;
                }

                // Prompt the user to select a destination album
                ChoiceDialog<Album> dialog = new ChoiceDialog<>(user.getAlbums().get(0), user.getAlbums());
                dialog.setTitle("Copy Photos");
                dialog.setHeaderText("Select a destination album");
                dialog.setContentText("Album:");
                Optional<Album> result = dialog.showAndWait();

                if (result.isPresent()) {
                    Album destAlbum = result.get();

                    // Copy the selected photos to the destination album
                    for (Photo photo : selectedPhotos) {
                        Photo copy = new Photo(photo.getName(), photo.getImage(), photo.getDate());
                        copy.setCaption(photo.getCaption(), user);
                        for (Tag tag : photo.getTags()) {
                            copy.addTag(tag, user);
                        }
                        destAlbum.addPhoto(copy);
                    }

                    // Save changes
                    users.save();

                    // Update photoListView
                    photoListView.setItems(FXCollections.observableArrayList(album.getPhotos()));
                }
            }
        });

        slideshowButton.setOnAction(e -> {
            // get the selected album and photos
            List<Photo> photoList = new ArrayList<>();
            int currentPhotoIndex = 0;
            Album selectedAlbum = album;
            if (selectedAlbum != null) {
                photoList = selectedAlbum.getPhotos();
                if (!photos.isEmpty()) {
                    // set the current photo to the first photo in the album
                    Photo currentPhoto = photos.get(currentPhotoIndex);
                    
                    // create a new stage for the slideshow popup
                    Stage slideshowStage = new Stage();
                    slideshowStage.setTitle("Slideshow - " + selectedAlbum.getName());
                    
                    // create image view and buttons for slideshow popup
                    ImageView imageView = new ImageView(currentPhoto.getImage());
                    imageView.setFitWidth(400);
                    imageView.setFitHeight(400);

                    Button prevButton = new Button("<");
                    Button nextButton = new Button(">");
                    HBox buttonBox = new HBox(10, prevButton, nextButton);
                    VBox vbox = new VBox(10, imageView, buttonBox);
                    vbox.setAlignment(Pos.CENTER);
                    buttonBox.setAlignment(Pos.CENTER);
                    
                    final int[] currentPhotoIndex2 = {0};
                    
                    // add event handlers for the buttons
                    prevButton.setOnAction(event -> {
                        if (currentPhotoIndex2[0] > 0) {
                            currentPhotoIndex2[0]--;
                            imageView.setImage(photos.get(currentPhotoIndex2[0]).getImage());
                        }
                    });
                    
                    nextButton.setOnAction(event -> {
                        if (currentPhotoIndex2[0] < photos.size() - 1) {
                            currentPhotoIndex2[0]++;
                            imageView.setImage(photos.get(currentPhotoIndex2[0]).getImage());
                        }
                    });
                    
                    
                    // set the scene for the slideshow popup
                    Scene slideshowScene = new Scene(vbox, 800, 600);
                    slideshowStage.setScene(slideshowScene);
                    slideshowStage.show();
                }
            }
        });
        
    }
}
