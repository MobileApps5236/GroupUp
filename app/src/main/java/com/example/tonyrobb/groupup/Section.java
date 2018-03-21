package com.example.tonyrobb.groupup;

/**
 * Created by tonyrobb on 3/20/18.
 */

public class Section {
    private String sectionId;
    private int sectionNumber;

    public Section(){

    }

    public Section(String sectionId, int sectionNumber) {
        this.sectionId = sectionId;
        this.sectionNumber = sectionNumber;
    }

    public String getSectionId() {
        return sectionId;
    }

    public int getSectionNumber() {
        return sectionNumber;
    }
}
