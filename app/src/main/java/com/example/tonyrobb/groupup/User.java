package com.example.tonyrobb.groupup;

import java.util.ArrayList;

public class User {

    private int userId;
    private String email;
    private String firstName;
    private String lastName;
    private String bio;
    private String major;
    private String skills;
    private boolean isProf;
    private boolean isDefaultPic;

    private ArrayList<String> enrolledSections;

    public User(int userId, String email, String firstName, String lastName, boolean isProf){
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isProf = isProf;
        this.bio = null;
        this.major = null;
        this.skills = null;
        this.isDefaultPic = true;

        enrolledSections = new ArrayList<>();
    }

    public int getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String first_name) {
        this.firstName = first_name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio){
        this.bio = bio;
    }

    public String getMajor() { return major; }

    public void setMajor(String major){
        this.major = major;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills){
        this.skills = skills;
    }

    public boolean getIsProf() { return isProf; }

    public void setIsProf(boolean isProf) {
        this.isProf = isProf;
    }

    public boolean getDefaultProfilePic() { return isDefaultPic; }

    public void setIsDefaultPic(boolean isDefaultPic) {
        this.isDefaultPic = isDefaultPic;
    }

    public ArrayList<String> getSectionList() { return enrolledSections; }
}
