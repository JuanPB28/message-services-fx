package com.juanpascual.messagesfx.models;

import javafx.scene.image.Image;

public class DataModel {
    public static String uri = "http://localhost:3000";
    public static String uriImages = uri + "/";
    public static String uriLogin = uri + "/login";
    public static String uriRegister = uri + "/register";
    public static String uriUsers = uri + "/users";
    public static String uriMessages = uri + "/messages";

    private String username;
    private Image userImage;

    public String getUsername() {
        return username;
    }

    public Image getUserImage() {
        return userImage;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserImage(Image userImage) {
        this.userImage = userImage;
    }

}
