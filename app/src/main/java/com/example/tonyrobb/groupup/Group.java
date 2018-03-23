package com.example.tonyrobb.groupup;

import java.util.ArrayList;
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
    private List<String> groupMembers;

    public Group() {

    }

    public Group(String groupId, String groupName, int groupLimit, String groupOwnerUId, String sectionId, List<String> groupMembers) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupLimit = groupLimit;
        this.groupOwnerUId = groupOwnerUId;
        this.sectionId = sectionId;
        this.groupMembers = new ArrayList<String>();
    }

    public String getGroupId() { return groupId; }

    public String getGroupName() { return groupName; }

    public int getGroupLimit() { return groupLimit; }

    public String getGroupOwnerUId() { return groupOwnerUId; }

    public String getSectionId() { return sectionId; }

    public List<String> getGroupMembers() { return groupMembers; }

    public void addMember(String addedMemberUId) {
        groupMembers.add(addedMemberUId);
    }

    public boolean removeMember(String removedMemberId) {
        boolean isRemoved = false;
        if (groupMembers.contains(removedMemberId)) {
            groupMembers.remove(groupMembers.indexOf(removedMemberId));
            isRemoved = true;
        }

        return isRemoved;
    }
}
