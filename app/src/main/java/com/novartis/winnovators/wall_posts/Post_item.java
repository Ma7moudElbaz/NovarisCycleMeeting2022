package com.novartis.winnovators.wall_posts;

public class Post_item {

    final private int id;
    private int isLiked;
    final private String user_name, profile_img_url,date, content,img_url,comments_no;
    private String likes_no;

    public Post_item(int id, int isLiked, String user_name, String profile_img_url, String date, String content, String img_url, String likes_no, String comments_no) {
        this.id = id;
        this.isLiked = isLiked;
        this.user_name = user_name;
        this.profile_img_url = profile_img_url;
        this.date = date;
        this.content = content;
        this.img_url = img_url;
        this.likes_no = likes_no;
        this.comments_no = comments_no;
    }

    public int getId() {
        return id;
    }

    public int getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(int isLiked) {
        this.isLiked = isLiked;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getProfile_img_url() {
        return profile_img_url;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getLikes_no() {
        return likes_no;
    }

    public void setLikes_no(String likes_no) {
        this.likes_no = likes_no;
    }

    public String getComments_no() {
        return comments_no;
    }


}
