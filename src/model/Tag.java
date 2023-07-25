package model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @authors Avinash Paluri and Vishal Patel
 *
 * Class that creates Tag object
 */

public class Tag implements Serializable {
    
    private String name;
    private String value;
    private static Map<String, Boolean> tagTypes = new HashMap<>();
    private static Set<String> defaultTags = new HashSet<>(Arrays.asList("location", "person"));
    
    public Tag(String name, String value) {
        this.name = name.toLowerCase();
        this.value = value.toLowerCase();
    }
    
    
    /** 
     * @return String
     * 
     * returns name of album
     */
    public String getName() {
        return name;
    }
    
    
    /** 
     * @return String
     * 
     * gets value of tag
     */
    public String getValue() {
        return value;
    }
    
    
    /** 
     * @param name
     * 
     * gets name of tag
     */
    public void setName(String name) {
        this.name = name;
    }
    
    
    /** 
     * @param value
     * 
     * sets value
     */
    public void setValue(String value) {
        this.value = value;
    }
    
    
    /** 
     * @param obj
     * @return boolean
     * 
     * compares two tags to see if they are the same
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Tag))
            return false;

        Tag otherTag = (Tag) obj;
        return otherTag.getName().toLowerCase().equals(this.name) && otherTag.getValue().toLowerCase().equals(this.value);
    }
    
    
    /** 
     * @param type
     * @param multipleValuesAllowed
     * 
     * checks to see if a tag being added can have multiple values
     */
    public static void addTagType(String type, boolean multipleValuesAllowed) {
        tagTypes.put(type, multipleValuesAllowed);
    }
    
    
    /** 
     * @return Map<String, Boolean>
     * 
     * returns all the types of tags
     */
    public static Map<String, Boolean> getTagTypes() {
        return tagTypes;
    }
    
    
    /** 
     * @param tag
     * 
     * adds a tag to be a default
     */
    public static void addDefaultTag(String tag) {
        defaultTags.add(tag);
    }
    
    
    /** 
     * @return Set<Tag>
     * 
     * returns a list of the default tags
     */
    public static Set<String> getDefaultTags() {
        return defaultTags;
    }

	
    /** 
     * @return String
     */
    public String toString() {
		return name+": "+value;
	}
}
