package com.example.demo.model.data;
public class ProfileData {
    private String username;
    private String bio;
    private String image;
    private int follower;

    public int getFollower() {
        return follower;
    }

    public void setFollower(int follower) {
        this.follower = follower;
    }

    private Boolean following;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getFollowing() {
        return following;
    }

    public void setFollowing(Boolean following) {
        this.following = following;
    }

    public ProfileData(String username, String bio, String image, int follower, Boolean following) {
        this.username = username;
        this.bio = bio;
        this.image = image;
        this.follower = follower;
        this.following = following;
    }

    public ProfileData() {

    }
}
