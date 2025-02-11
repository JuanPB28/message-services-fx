package com.juanpascual.messagesfx;

import com.juanpascual.messagesfx.controllers.LoginController;
import com.juanpascual.messagesfx.controllers.MainController;
import com.juanpascual.messagesfx.controllers.RegisterController;
import com.juanpascual.messagesfx.models.DataModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MessagesApplication extends Application {
    private static final DataModel model = new DataModel();

    @Override
    public void start(Stage stage) throws IOException {
        login(stage);
    }

    public static void main(String[] args) {
        launch();
    }

    private static void login(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MessagesApplication.class.getResource("login-view.fxml"));
        Parent root = fxmlLoader.load();
        //root.getStylesheets().add("file:src/main/resources/com/juanpascual/messagesfx/style/login-style.css");
        Scene scene = new Scene(root, 320, 240);
        LoginController loginController = fxmlLoader.getController();
        loginController.setStage(stage);
        loginController.setData(model);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    // Método para abrir la ventana de Login
    public static void openLoginWindow() throws IOException {
        Stage stage = new Stage();
        login(stage);
    }

    // Método para abrir la ventana de Registro
    public static void openRegisterWindow() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MessagesApplication.class.getResource("register-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        RegisterController registerController = fxmlLoader.getController();
        registerController.setStage(stage);
        stage.setTitle("Register");
        stage.setScene(scene);
        stage.show();
    }

    // Método para abrir la ventana Principal
    public static void openMainWindow() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MessagesApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        MainController mainController = fxmlLoader.getController();
        mainController.setData(model);
        stage.setTitle("Messages");
        stage.setScene(scene);
        stage.show();
    }
}