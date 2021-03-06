package com.example.tonyrobb.groupup;

import java.util.HashMap;

/**
 * Created by tonyrobb on 3/20/18.
 */

public class Section {
    private String sectionId;
    private int sectionNumber;
    private HashMap<String, User> enrolledUsers;
    private HashMap<String,Group> groupsMade;
    private String classId;

    public Section(){

    }

    public Section(String sectionId, int sectionNumber, String classId,HashMap<String, User> enrolledUsers, HashMap<String, Group> groupsMade) {
        this.sectionId = sectionId;
        this.sectionNumber = sectionNumber;
        this.enrolledUsers = enrolledUsers;
        this.groupsMade = groupsMade;
        this.classId = classId;

    }

    public String getSectionId() {
        return sectionId;
    }

    public int getSectionNumber() {
        return sectionNumber;
    }

    public HashMap<String, User> getEnrolledUsers() {
        return enrolledUsers;
    }

    public HashMap<String, Group> getGroupsMade() { return groupsMade;}

    public String getClassId() {
        return classId;
    }
}
