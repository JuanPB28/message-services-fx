package com.juanpascual.messagesfx.controllers;

import com.google.gson.Gson;
import com.juanpascual.messagesfx.MessagesApplication;
import com.juanpascual.messagesfx.models.DataModel;
import com.juanpascual.messagesfx.responses.UserResponse;
import com.juanpascual.messagesfx.utils.AlertUtils;
import com.juanpascual.messagesfx.utils.ImageUtils;
import com.juanpascual.messagesfx.utils.RequestService;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class RegisterController {
    public TextField userTextField;
    public PasswordField passPassField;
    public PasswordField passRepPassField;
    public ImageView imageView;

    private Stage stage;
    private String user;
    private String pass;
    private File image;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void onCancelClick() throws IOException {
        MessagesApplication.openLoginWindow();
        stage.close();
    }

    public void onRegisterClick() {
        try{
            user = userTextField.getText();
            pass = passPassField.getText();
            if(user.isBlank() || pass.length() < 4 || !Objects.equals(passPassField.getText(), passRepPassField.getText()) || image == null){
                AlertUtils.showError("Error", "Fields incorrectly completed.");
            } else {
                newRegister(user, pass, image);
                clearFields();
            }
        } catch (Exception e){
            AlertUtils.showError("Error", e.getMessage());
            e.printStackTrace();
        }
    }

    public void onSelectImageClick() {
        try {
            File selectedImage = ImageUtils.jpgChooser();
            if (selectedImage != null) {
                image = selectedImage;
                imageView.setImage(new Image("file:" + selectedImage.getAbsolutePath()));
            }
        } catch (Exception e) {
            AlertUtils.showError("Error", "Error selecting file.");
        }
    }

    private void newRegister(String user, String pass, File image){
        String imageData = ImageUtils.toString(image);
        String json = String.format("{\"name\": \"%s\", \"password\": \"%s\", \"image\": \"%s\"}", user, pass, imageData);
        RequestService register = new RequestService(DataModel.uriRegister, json, "POST");
        register.start();
        register.setOnSucceeded(e -> {
            Gson gson = new Gson();
            UserResponse response = gson.fromJson(register.getValue(), UserResponse.class);
            if (response.getOk()) {
                AlertUtils.showConfirmToLogin("You have successfully registered", "You will be redirected to the login page.", stage);
            } else {
                AlertUtils.showError("Error", response.getError());
            }
        });
        register.setOnFailed(e -> {
            Throwable exception = register.getException();
            if(exception != null){
                AlertUtils.showError("Error", exception.getMessage());
            }
        });
    }

    private void clearFields() {
        userTextField.clear();
        passPassField.clear();
        passRepPassField.clear();
        imageView.setImage(new Image("file:src/main/resources/com/juanpascual/messagesfx/img/placeholder.jpg"));
        image = null;
    }
}
