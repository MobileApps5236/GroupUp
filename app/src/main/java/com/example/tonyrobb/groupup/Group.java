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
    private String groupMembers;

    public Group() {

    }

    public Group(String groupId, String groupName, int groupLimit, String groupOwnerUId, String sectionId, String groupMembers) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupLimit = groupLimit;
        this.groupOwnerUId = groupOwnerUId;
        this.sectionId = sectionId;
        this.groupMembers = groupMembers;
    }

    public String getGroupId() { return groupId; }

    public String getGroupName() { return groupName; }

    public int getGroupLimit() { return groupLimit; }

    public String getGroupOwnerUId() { return groupOwnerUId; }

    public String getSectionId() { return sectionId; }

    public String getGroupMembers() { return groupMembers; }

    public void addMember(String addedMemberUId) {
        groupMembers += " " + addedMemberUId;
    }

    public boolean removeMember(String removedMemberId) {
        boolean isRemoved = false;
        if (groupMembers.contains(removedMemberId)) {
            String[] groupArray = groupMembers.split(" ");
            String[] newArray = new String[groupArray.length - 1];
            for (int i = 0; i < groupArray.length; i++) {
                if (groupArray[i].equals(removedMemberId)) {
                    // Do nothing
                } else {
                    newArray[i] = groupArray[i];
                }
            }
            String newString = newArray[0];
            for (int i = 1; i < newArray.length; i++) {
                newString += " " + newArray[i];
            }
            isRemoved = true;
        }

        return isRemoved;
    }
}
