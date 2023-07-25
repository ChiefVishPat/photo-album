package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import javafx.scene.image.Image;

/**
 * @authors Avinash Paluri and Vishal Patel
 *
 * Class that creates photo object
 */

public class Photo implements Serializable, Comparable<Photo> {

    private ArrayList<Tag> tags;
    private String name, caption;
    private SerializablePhoto image;
    private Calendar date;

    public Photo(String name, SerializablePhoto image, Calendar date) {
        this.name = name;
        this.caption = "";
        this.image = image;
        this.date = date;
        this.tags = new ArrayList<Tag>();
        this.date.set(Calendar.MILLISECOND, 0);
    }

    public Photo(String name, Image image, Calendar date) {
        this.name = name;
        this.caption = "";
        this.image = new SerializablePhoto(image);
        this.date = date;
        this.tags = new ArrayList<Tag>();
        this.date.set(Calendar.MILLISECOND, 0);
    }

    
    /** 
     * @return String
     * 
     * returns name of photo
     */
    public String getName() {
        return name;
    }

    
    /** 
     * @return String
     * 
     * returns caption of a photo
     */
    public String getCaption() {
        return caption;
    }

    
    /** 
     * @return Image
     * 
     * returns the image of the photo
     */
    public Image getImage() {
        return image.getImage();
    }

    
    /** 
     * @return ArrayList<Tag>
     * 
     * returns a list of tags for a photo
     */
    public ArrayList<Tag> getTags() {
        return tags;
    }

    
    /** 
     * @param tag
     * @param user
     * 
     * adds a tag to a photo for all instances for an user
     */
    public void addTag(Tag tag, User user) {
        tags.add(tag);

        // Update caption for all appearances of this photo in all albums
        for (Album album : user.getAlbums()) {
            for (Photo photo : album.getPhotos()) {
                if (photo.getName().equals(this.name)) {
                    if(!photo.tags.contains(tag))
                        photo.tags.add(tag);
                }
            }
        } 
    }

    /** 
     * @param tag
     * @param user
     * 
     * removes a tag to a photo for all instances for an user
     */
    public void removeTag(Tag tag, User user) {
        tags.remove(tag);

        // Update caption for all appearances of this photo in all albums
        for (Album album : user.getAlbums()) {
            for (Photo photo : album.getPhotos()) {
                if (photo.getName().equals(this.name)) {
                    if(photo.tags.contains(tag))
                        photo.tags.remove(tag);
                }
            }
        } 
    }

    
    /** 
     * @return Calendar
     * 
     * returns date of photo
     */
    public Calendar getDate() {
        return date;
    }

    public boolean hasTag(String tagValuePair) {
        String[] tagValue = tagValuePair.split("=");
        if (tagValue.length != 2) {
            return false; // invalid tag-value pair
        }
        String tag = tagValue[0].toLowerCase();
        String value = tagValue[1].toLowerCase();
        Tag t = new Tag(tag, value);
        for (Tag ta : tags) {
            if (ta.equals(t)) {
                return true;
            }
        }
        return false;
    }
    

    
    /** 
     * @param caption
     * @param user
     * 
     * sets a caption to a photo for all instances for an user
     */
    public void setCaption(String caption, User user) {
        this.caption = caption;
    
        // Update caption for all appearances of this photo in all albums
        for (Album album : user.getAlbums()) {
            for (Photo photo : album.getPhotos()) {
                if (photo.getName().equals(this.name)) {
                    photo.caption = caption;
                }
            }
        }   
    }

    
    /** 
     * @param other
     * @return int
     * 
     * compares two dates
     */
    public int compareTo(Photo other) {
        if (this.date.before(other.date)) {
            return -1;
        } else if (this.date.after(other.date)) {
            return 1;
        } else {
            return 0;
        }
    }

    
    /** 
     * @return String
     */
    public String toString() {
        return this.name;
    }
}
