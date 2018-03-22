package com.example.tonyrobb.groupup;

import android.net.Uri;

import java.util.ArrayList;

public class User {

    private String userId;
    private String email;
    private String firstName;
    private String lastName;
    private String bio;
    private String major;
    private String skills;
    private Uri profilePicUrl;
    private boolean isProf;

    public User(String userId, String email, String firstName, String lastName, Uri profilePicUrl, boolean isProf){
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isProf = isProf;
        bio = "";
        major = "";
        skills = "";
        this.profilePicUrl = profilePicUrl;
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

    public Uri getDefaultProfilePic() { return profilePicUrl; }
}