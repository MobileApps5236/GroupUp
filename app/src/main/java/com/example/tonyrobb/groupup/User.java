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
    //private Uri profilePicUrl;
    private boolean isProf;
    private HashMap<String,Section> sectionsEnrolledIn;

    public User(){

    }

    public User(String userId, String email, String firstName, String lastName, boolean isProf, HashMap<String,Section> sectionsEnrolledIn){
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isProf = isProf;
        bio = "";
        major = "";
        skills = "";
        //this.profilePicUrl = Uri.parse("https://firebasestorage.googleapis.com/v0/b/groupup-cc709.appspot.com/o/profile_pictures%2Fdefault_pic.jpg?alt=media&token=447fe1a9-ffd1-41f9-ac45-d6937b0fe825");
        this.sectionsEnrolledIn = sectionsEnrolledIn;
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

    //public Uri getDefaultProfilePic() { return profilePicUrl; }

    public HashMap<String,Section> getSectionsEnrolledIn() {
        return sectionsEnrolledIn;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }
}
