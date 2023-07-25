package model;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @authors Avinash Paluri and Vishal Patel
 *
 * Class that creates Album object
 */

public class Album implements Serializable {

    private List<Photo> photos;
    private String dataFile = "data/userlist.dat";
    private String albumName;
    
    public Album(String name) {
        this.albumName = name;
        photos = new ArrayList<>();
    }
    
    
    /** 
     * @param photo
     * 
     * adds a photo to the album
     */
    public void addPhoto(Photo photo) {
        photos.add(photo);
    }
    
    
    /** 
     * @param photo
     * 
     * removes a photo from album
     */
    public void removePhoto(Photo photo) {
        photos.remove(photo);
    }
    
    
    /** 
     * @param name
     * 
     * changes name of album
     */
    public void changeName(String name) {
        this.albumName = name;
    }
    
    
    /** 
     * @return String
     * 
     * returns album name
     */
    public String getName() {
        return this.albumName;
    }
    
    
    /** 
     * @return List<Photo>
     * 
     * returns list of photos
     */
    public List<Photo> getPhotos() {
        return photos;
    }
    
    
    /** 
     * @return String
     */
    public String toString() {
        return albumName;
    }
    
    
    /** 
     * @return String
     * 
     * returns the range of dates of photos in album
     */
    public String getDateRange() {
        if (photos.isEmpty()) {
            return "";
        }
        Calendar earliestDate = photos.get(0).getDate();
        Calendar latestDate = photos.get(0).getDate();
        for (Photo photo : photos) {
            Calendar date = photo.getDate();
            if (date.before(earliestDate)) {
                earliestDate = date;
            }
            if (date.after(latestDate)) {
                latestDate = date;
            }
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return dateFormat.format(earliestDate.getTime()) + " - " + dateFormat.format(latestDate.getTime());
    }    
}

