package com.example.tonyrobb.groupup;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jpp1995 on 3/23/2018.
 */

public class Group {
    private String groupId;
    private String groupName;
    private int groupLimit;
    private String groupOwnerUId;
    private String sectionId;
    private HashMap<String, User> groupMembers;

    public Group() {

    }

    public Group(String groupId, String groupName, int groupLimit, String groupOwnerUId, String sectionId, HashMap<String, User> groupMembers) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupLimit = groupLimit;
        this.groupOwnerUId = groupOwnerUId;
        this.sectionId = sectionId;
        this.groupMembers = groupMembers;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public int getGroupLimit() {
        return groupLimit;
    }

    public String getGroupOwnerUId() {
        return groupOwnerUId;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setGroupOwnerUId(String groupOwnerUId) {
        this.groupOwnerUId = groupOwnerUId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public void setGroupMembers(HashMap<String, User> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public HashMap<String, User> getGroupMembers() {
        return groupMembers;
    }

    public void addMember(User user) {
        if (groupMembers.size() <= groupLimit) {
            groupMembers.put(user.getUserId(), user);
        }
    }

    public void removeMember(User user) {
        groupMembers.remove(user.getUserId());
    }

}
