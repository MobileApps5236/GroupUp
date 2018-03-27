package com.example.tonyrobb.groupup;

import java.util.HashMap;

/**
 * Created by tonyrobb on 3/26/18.
 */

public class MessageThread {
    private String timeCreated;
    private String thread_id;
    private String subject;
    private String creatorId;
    private String sectionId;
    private HashMap<String, Message> messages;

    public MessageThread(){

    }

    public MessageThread(String thread_id, String timeCreated, String subject, String creatorId,
                         String sectionId, HashMap<String, Message> messages) {
        this.timeCreated = timeCreated;
        this.thread_id = thread_id;
        this.subject = subject;
        this.creatorId = creatorId;
        this.sectionId = sectionId;
        this.messages  = messages;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public String getThread_id() {
        return thread_id;
    }

    public String getSubject() {
        return subject;
    }

    public String getCreatorId() { return creatorId; }

    public String getSectionId() {
        return sectionId;
    }
}
