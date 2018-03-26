package com.example.tonyrobb.groupup;

/**
 * Created by tonyrobb on 3/26/18.
 */

public class MessageThread {
    private String timeCreated;
    private String thread_id;
    private String subject;
    private String creatorId;
    private String sectionId;

    public MessageThread(){

    }

    public MessageThread(String thread_id, String timeCreated, String subject, String creatorId, String sectionId) {
        this.timeCreated = timeCreated;
        this.thread_id = thread_id;
        this.subject = subject;
        this.creatorId = creatorId;
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
}
