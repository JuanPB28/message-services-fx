package com.juanpascual.messagesfx.controllers;

import com.google.gson.Gson;
import com.juanpascual.messagesfx.MessagesApplication;
import com.juanpascual.messagesfx.models.DataModel;
import com.juanpascual.messagesfx.responses.UserResponse;
import com.juanpascual.messagesfx.utils.AlertUtils;
import com.juanpascual.messagesfx.utils.RequestService;
import com.juanpascual.messagesfx.utils.RequestUtils;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    public TextField userTextField;
    public PasswordField passPassField;
    public Text errorText;

    private Stage stage;
    private DataModel model;
    protected String user;
    protected String pass;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setData(DataModel model){
        this.model = model;
    }

    @FXML
    protected void onLoginClick(){
        try {
            user = userTextField.getText();
            pass = passPassField.getText();
            if (user.isBlank() || pass.isBlank()){
                errorText.setVisible(true);
            } else {
                newLogin(user, pass);
            }
        } catch (Exception e) {
            errorText.setVisible(true);
            e.printStackTrace();
        }
    }

    @FXML
    public void onRegisterClick() throws IOException {
        MessagesApplication.openRegisterWindow();
        stage.close();
    }

    private void newLogin(String user, String pass){
        String json = String.format("{\"name\": \"%s\", \"password\": \"%s\"}", user, pass);
        RequestService login = new RequestService(DataModel.uriLogin, json, "POST");
        login.start();
        login.setOnSucceeded(e -> {
            Gson gson = new Gson();
            UserResponse response = gson.fromJson(login.getValue(), UserResponse.class);
            if (response.getOk()) {
                model.setUsername(response.getName());
                model.setUserImage(new Image(DataModel.uriImages + response.getImage()));
                RequestUtils.setToken(response.getToken());
                try {
                    MessagesApplication.openMainWindow();
                    stage.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                errorText.setVisible(true);
            }
        });
        login.setOnFailed(e -> {
            Throwable exception = login.getException();
            if(exception != null){
                AlertUtils.showError("Error", exception.getMessage());
            }
        });
    }
}