package com.juanpascual.messagesfx.controllers;

import com.google.gson.Gson;
import com.juanpascual.messagesfx.models.DataModel;
import com.juanpascual.messagesfx.models.Message;
import com.juanpascual.messagesfx.models.User;
import com.juanpascual.messagesfx.responses.MessageResponse;
import com.juanpascual.messagesfx.responses.UserResponse;
import com.juanpascual.messagesfx.utils.AlertUtils;
import com.juanpascual.messagesfx.utils.ImageUtils;
import com.juanpascual.messagesfx.utils.RequestService;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class MainController {
    public ImageView userImageView;
    public ImageView selectedUserImageView;
    public Text userText;
    public Text selectedUserText;
    public TableView<User> userTable;
    public TableView<Message> messageTable;
    public TextField messageTextField;
    public TableColumn<User, ImageView> imgUserCol;
    public TableColumn<User, String> nameUserCol;
    public TableColumn<Message, ImageView> imageMsgCol;
    public TableColumn<Message, String> messageMsgCol;
    public TableColumn<Message, String> dateMsgCol;
    public Button deleteButton;

    private DataModel model;
    private List<Message> messageList;
    private String userId;
    private String messageId;
    private File image = null;

    public void initialize(){
        // Configurar columnas
        imgUserCol.setCellValueFactory(new PropertyValueFactory<User, ImageView>("imageView"));
        nameUserCol.setCellValueFactory(new PropertyValueFactory<User, String>("name"));

        imageMsgCol.setCellValueFactory(new PropertyValueFactory<Message, ImageView>("imageView"));
        messageMsgCol.setCellValueFactory(new PropertyValueFactory<Message, String>("message"));
        dateMsgCol.setCellValueFactory(new PropertyValueFactory<Message, String>("sent"));

        // Añadir datos
        getUsersData();
        getMessagesData();

        // Configurar el manejador de eventos de selección
        userTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                handleSelection(newSelection);
            }
        });

        messageTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                handleSelection(newSelection);
            }
        });
    }

    public void setData(DataModel model) {
        this.model = model;
        userText.setText(model.getUsername());
        userImageView.setImage(model.getUserImage());
    }

    private void handleSelection(User selectedUser){
        selectedUserText.setText(selectedUser.getName());
        selectedUserImageView.setImage(new Image(DataModel.uriImages + selectedUser.getImage()));
        List<Message> messagesFrom = messageList.stream().filter(message -> message.getFrom().equals(selectedUser.get_id())).collect(Collectors.toList());
        messageTable.getItems().clear();
        messageTable.getItems().addAll(messagesFrom);
        userId = selectedUser.get_id();
    }

    private void handleSelection(Message selectedMessage){
        deleteButton.setDisable(false);
        messageId = selectedMessage.get_id();
    }

    public void onChangeImageClick(ActionEvent actionEvent) {
        try {
            File selectedImage = ImageUtils.jpgChooser();
            if (selectedImage != null) {
                newUserImage(selectedImage);
            }
        } catch (Exception e) {
            AlertUtils.showError("Error", "Error selecting file.");
            e.printStackTrace();
        }
    }

    public void onAddImageClick(ActionEvent actionEvent) {
        try {
            File selectedImage = ImageUtils.jpgChooser();
            if (selectedImage != null) {
                image = selectedImage;
            }
        } catch (Exception e) {
            AlertUtils.showError("Error", "Error selecting file.");
        }
    }

    public void onSendMessageClick(ActionEvent actionEvent) {
        if(userId != null || messageTextField.getText() != null){
            if(image == null) {
                newMessage(userId, messageTextField.getText());
            } else {
                newMessage(userId, messageTextField.getText(), image);
            }
        } else {
            AlertUtils.showError("Error", "Select a user and write a message.");
        }
    }

    public void onUpdateClick(ActionEvent actionEvent) {
        clearFields();
        getUsersData();
        getMessagesData();
        selectedUserText.setText("All");
        selectedUserImageView.setImage(new Image("file:src/main/resources/com/juanpascual/messagesfx/img/placeholder.jpg"));
        deleteButton.setDisable(true);
    }

    public void onDeleteMessageClick(ActionEvent actionEvent) {
        if(!messageId.isBlank()){
            deleteMessage(messageId);
        } else {
            AlertUtils.showError("Error", "Select a message.");
        }
    }

    private void newUserImage(File image){
        String imageData = ImageUtils.toString(image);
        String json = String.format("{\"image\": \"%s\"}", imageData);
        RequestService changeImage = new RequestService(DataModel.uriUsers, json, "PUT");
        changeImage.start();
        changeImage.setOnSucceeded(e -> {
            Gson gson = new Gson();
            UserResponse response = gson.fromJson(changeImage.getValue(), UserResponse.class);
            if (response.getOk()) {
                userImageView.setImage(new Image("file:" + image.getAbsolutePath()));
            } else {
                AlertUtils.showError("Error", response.getError());
            }
        });
        changeImage.setOnFailed(e -> {
            Throwable exception = changeImage.getException();
            if(exception != null){
                AlertUtils.showError("Error", exception.getMessage());
                exception.printStackTrace();
            }
        });
    }

    private void getUsersData(){
        RequestService getUsers = new RequestService(DataModel.uriUsers, null, "GET");
        getUsers.start();
        getUsers.setOnSucceeded(e -> {
            Gson gson = new Gson();
            UserResponse response = gson.fromJson(getUsers.getValue(), UserResponse.class);
            if (response.getOk()) {
                userTable.getItems().clear();
                userTable.getItems().addAll(response.getUsers());
            } else {
                AlertUtils.showError("Error", response.getError());
            }
        });
        getUsers.setOnFailed(e -> {
            Throwable exception = getUsers.getException();
            if(exception != null){
                AlertUtils.showError("Error", exception.getMessage());
                exception.printStackTrace();
            }
        });
    }

    private void getMessagesData() {
        RequestService getMessages = new RequestService(DataModel.uriMessages, null, "GET");
        getMessages.start();
        getMessages.setOnSucceeded(e -> {
            Gson gson = new Gson();
            MessageResponse response = gson.fromJson(getMessages.getValue(), MessageResponse.class);
            if (response.getOk()) {
                messageTable.getItems().clear();
                messageTable.getItems().addAll(response.getMessages());
                messageList = response.getMessages();
            } else {
                AlertUtils.showError("Error", response.getError());
            }
        });
        getMessages.setOnFailed(e -> {
            Throwable exception = getMessages.getException();
            if(exception != null){
                AlertUtils.showError("Error", exception.getMessage());
                exception.printStackTrace();
            }
        });
    }

    private void newMessage(String toUserId, String message, File image){
        String imageData = ImageUtils.toString(image);
        String json = String.format("{\"message\": \"%s\", \"image\": \"%s\"}", message, imageData);
        RequestService deleteMessage = new RequestService(DataModel.uriMessages + "/" + toUserId, json, "POST");
        deleteMessage.start();
        deleteMessage.setOnSucceeded(e -> {
            Gson gson = new Gson();
            MessageResponse response = gson.fromJson(deleteMessage.getValue(), MessageResponse.class);
            if (response.getOk()) {
                AlertUtils.showMessage("Info", "Message send successfully.");
                clearFields();
            } else {
                AlertUtils.showError("Error", response.getError());
            }
        });
        deleteMessage.setOnFailed(e -> {
            Throwable exception = deleteMessage.getException();
            if(exception != null){
                AlertUtils.showError("Error", exception.getMessage());
                exception.printStackTrace();
            }
        });
    }

    private void newMessage(String toUserId, String message){
        String json = String.format("{\"message\": \"%s\"}", message);
        RequestService deleteMessage = new RequestService(DataModel.uriMessages + "/" + toUserId, json, "POST");
        deleteMessage.start();
        deleteMessage.setOnSucceeded(e -> {
            Gson gson = new Gson();
            MessageResponse response = gson.fromJson(deleteMessage.getValue(), MessageResponse.class);
            if (response.getOk()) {
                AlertUtils.showMessage("Info", "Message send successfully.");
                clearFields();
            } else {
                AlertUtils.showError("Error", response.getError());
            }
        });
        deleteMessage.setOnFailed(e -> {
            Throwable exception = deleteMessage.getException();
            if(exception != null){
                AlertUtils.showError("Error", exception.getMessage());
                exception.printStackTrace();
            }
        });
    }

    private void deleteMessage(String id) {
        RequestService deleteMessage = new RequestService(DataModel.uriMessages + "/" + id, null, "DELETE");
        deleteMessage.start();
        deleteMessage.setOnSucceeded(e -> {
            Gson gson = new Gson();
            MessageResponse response = gson.fromJson(deleteMessage.getValue(), MessageResponse.class);
            if (response.getOk()) {
                AlertUtils.showMessage("Info", "Message successfully deleted.");
                clearFields();
            } else {
                AlertUtils.showError("Error", response.getError());
            }
        });
        deleteMessage.setOnFailed(e -> {
            Throwable exception = deleteMessage.getException();
            if(exception != null){
                AlertUtils.showError("Error", exception.getMessage());
                exception.printStackTrace();
            }
        });
    }

    private void clearFields(){
        messageTextField.clear();
        image = null;
    }
}
