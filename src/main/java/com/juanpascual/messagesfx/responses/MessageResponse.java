package com.juanpascual.messagesfx.responses;

import com.google.gson.annotations.SerializedName;
import com.juanpascual.messagesfx.models.Message;

import java.util.List;

public class MessageResponse {
    @SerializedName("ok")
    private boolean ok;
    @SerializedName("error")
    private String error;
    @SerializedName("messages")
    private List<Message> messages;
    @SerializedName("newMessage")
    private Message newMessage;

    public MessageResponse(boolean ok){
        this.ok = ok;
    }

    public MessageResponse(boolean ok, List<Message> messages){
        this.ok = ok;
        this.messages = messages;
    }

    public MessageResponse(boolean ok, Message newMessage){
        this.ok = ok;
        this.newMessage = newMessage;
    }

    public MessageResponse(boolean ok, String error){
        this.ok = ok;
        this.error = error;
    }

    public boolean getOk() {
        return ok;
    }

    public String getError() {
        return error;
    }

    public List<Message> getMessages() {
        return messages;
    }
}
