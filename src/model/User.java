package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @authors Avinash Paluri and Vishal Patel
 *
 * Class that creates the User object
 */

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Album> albums;
    private String dataFile = "/data/userlist.dat";
    private String username;

    public User(String username) {
        this.username = username;
        albums = new ArrayList<>();
    }

    
    /** 
     * @param album
     * 
     * adds album to user
     */
    public void addAlbum(Album album) {
        albums.add(album);
    }

    
    /** 
     * @param album
     * 
     * removes album from user
     */
    public void removeAlbum(Album album) {
        albums.remove(album);
    }

    
    /** 
     * @return List<Album>
     * 
     * gets all of the users' albums
     */
    public List<Album> getAlbums() {
        return albums;
    }

    
    /** 
     * @return String
     * 
     * get username
     */
    public String getUsername()
    {
        return username;
    }

    
    /** 
     * @return String
     */
    public String toString()
    {
        return username;
    }

    
    /** 
     * @return List<Photo>
     * 
     * gets all the photos that belong to the user
     */
    public List<Photo> getPhotos() {
        List<Photo> photos = new ArrayList<>();
        for (Album album : albums) {
            photos.addAll(album.getPhotos());
        }
        return photos;
    }
    
}