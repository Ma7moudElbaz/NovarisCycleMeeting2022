package com.novartis.winnovators.notifications;

public class Notification_item {

    final private String id,title, message, type,created_at;

    public Notification_item(String id, String title, String message, String type, String created_at) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.type = type;
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    public String getCreated_at() {
        return created_at;
    }
}
