package com.example.novariscyclemeeting2022.wall_posts.comments;

public class Comment_item {

    final private int id;
    final private String user_name, profile_img_url, content;

    public Comment_item(int id, String user_name, String profile_img_url, String content) {
        this.id = id;
        this.user_name = user_name;
        this.profile_img_url = profile_img_url;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getProfile_img_url() {
        return profile_img_url;
    }

    public String getContent() {
        return content;
    }
}
