package com.juanpascual.messagesfx.responses;

import com.google.gson.annotations.SerializedName;
import com.juanpascual.messagesfx.models.User;

import java.util.List;

public class UserResponse {
    @SerializedName("ok")
    private boolean ok;
    @SerializedName("error")
    private String error;
    @SerializedName("name")
    private String name;
    @SerializedName("image")
    private String image;
    @SerializedName("token")
    private String token;
    @SerializedName("users")
    private List<User> users;

    public UserResponse(boolean ok){
        this.ok = ok;
    }

    public UserResponse(boolean ok, String error){
        this.ok = ok;
        this.error = error;
    }

    public UserResponse(boolean ok, String name, String image, String token){
        this.ok = ok;
        this.name = name;
        this.image = image;
        this.token = token;
    }

    public UserResponse(boolean ok, List<User> users){
        this.ok = ok;
        this.users = users;
    }

    public boolean getOk(){
        return ok;
    }

    public String getError() {
        return error;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getToken() {
        return token;
    }

    public List<User> getUsers() {
        return users;
    }
}
