package com.novartis.winnovators.chat.users;

public class User_item {

private final int id;
    private final String  name, socket_id, online, updated_at,img_profile;

    public User_item(int id, String name, String socket_id, String online, String updated_at, String img_profile) {
        this.id = id;
        this.name = name;
        this.socket_id = socket_id;
        this.online = online;
        this.updated_at = updated_at;
        this.img_profile = img_profile;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSocket_id() {
        return socket_id;
    }

    public String getOnline() {
        return online;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getImg_profile() {
        return img_profile;
    }
}
