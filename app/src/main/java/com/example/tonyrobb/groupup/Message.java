package com.example.tonyrobb.groupup;

import java.util.HashMap;

/**
 * Created by tonyrobb on 3/26/18.
 */

public class Message {
    String messageId;
    String text;
    String timeCreated;
    boolean isComment;
    HashMap<String, Message> comments;

    public Message(){

    }

    public Message(String messageId, String text, String timeCreated, boolean isComment, HashMap<String, Message> comments){
        this.messageId = messageId;
        this.text = text;
        this.timeCreated = timeCreated;
        this.isComment = isComment;
        this.comments = comments;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getText() {
        return text;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public boolean isComment() {
        return isComment;
    }
}
