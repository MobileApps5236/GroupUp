package com.example.tonyrobb.groupup;

/**
 * Created by tonyrobb on 3/20/18.
 */

public class Class {
    private String classId;
    private String className;
    private String department;
    private int classNumber;

    public Class(){

    }

    public Class(String lassId, String className, String department, int classNumber) {
        this.classId = classId;
        this.className = className;
        this.department = department;
        this.classNumber = classNumber;
    }

    public String getClassId() {
        return classId;
    }

    public String getClassName() {
        return className;
    }

    public String getDepartment() {
        return department;
    }

    public int getClassNumber() {
        return classNumber;
    }
}
