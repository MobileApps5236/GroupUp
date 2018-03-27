package com.example.tonyrobb.groupup;

import android.net.Uri;

import java.util.HashMap;

public class User {

    private String userId;
    private String email;
    private String firstName;
    private String lastName;
    private String bio;
    private String major;
    private String skills;
    private String profilePicUrl;
    private boolean isProf;
    private HashMap<String,Section> sectionsEnrolledIn;
    private HashMap<String, Group> enrolledInGroup;

    public User(){

    }

    public User(String userId, String email, String firstName, String lastName, boolean isProf, HashMap<String,Section> sectionsEnrolledIn, HashMap<String, Group> enrolledInGroup){
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isProf = isProf;
        bio = "";
        major = "";
        skills = "";
        profilePicUrl = "";
        this.sectionsEnrolledIn = sectionsEnrolledIn;
        this.enrolledInGroup = enrolledInGroup;
    }


    public String getUserId() { return userId; }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBio() {
        return bio;
    }

    public String getMajor() { return major; }

    public String getSkills() {
        return skills;
    }

    public boolean getIsProf() { return isProf; }

    public String getProfilePicUrl() { return profilePicUrl; }

    public HashMap<String,Section> getSectionsEnrolledIn() {
        return sectionsEnrolledIn;
    }

    public HashMap<String, Group> getEnrolledInGroup() { return enrolledInGroup; }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public void setSectionsEnrolledIn(HashMap<String,Section> sectionsEnrolledIn){
        this.sectionsEnrolledIn = sectionsEnrolledIn;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setEnrolledInGroup(HashMap<String, Group> enrolledInGroup) {
        this.enrolledInGroup = enrolledInGroup;
    }


}
