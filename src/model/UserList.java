package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @authors Avinash Paluri and Vishal Patel
 *
 * Class that creates a UserList object
 */

public class UserList implements Serializable {
    private static final long serialVersionUID = 1L;
    private static List<User> userList;
    private static String dataFile = "data/userlist.dat";

    public UserList() {
        userList = new ArrayList<>();
    }

    
    /** 
     * @param user
     * 
     * adds user to the list
     */
    public void addUser(User user) {
        userList.add(user);
    }

    
    /** 
     * @param user
     * 
     * removes user from list
     */
    public void removeUser(User user) {
        userList.remove(user);
    }

    
    /** 
     * @return List<User>
     * 
     * gets list of users
     */
    public List<User> getUserList() {
        return userList;
    }

    
    /** 
     * @param username
     * @return User
     * 
     * gets user associated to given username
     */
    public User getUser(String username) {

        for(User u : userList)
        {
            if(u.getUsername().equals(username))
                return u;
        }
        return null;
    }  
    
    public void save() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile));
            oos.writeObject(userList);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile));
            userList = (List<User>) ois.readObject();
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}