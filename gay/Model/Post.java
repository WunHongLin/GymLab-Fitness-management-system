package com.example.gay.Model;

import java.util.Date;

public class Post extends PostId{
    private String postImage;
    private String postUser;
    private String PostContent;
    private String postDate;
    private Date Time;

    public Date getTime() {
        return Time;
    }

    public void setTime(Date time) {
        Time = time;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostUser() {
        return postUser;
    }

    public void setPostUser(String postUser) {
        this.postUser = postUser;
    }

    public String getPostContent() {
        return PostContent;
    }

    public void setPostContent(String postContent) {
        this.PostContent = postContent;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }
}
