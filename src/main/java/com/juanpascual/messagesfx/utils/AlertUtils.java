package com.juanpascual.messagesfx.utils;

import com.juanpascual.messagesfx.MessagesApplication;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;

public class AlertUtils {

    public static void showMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.setHeaderText(title);
        alert.show();
    }

    public static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.setHeaderText(title);
        alert.show();
    }

    public static void showConfirmToLogin(String title, String message, Stage stage){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message);
        alert.setHeaderText(title);
        ButtonType btOk = new ButtonType("Ok");
        ButtonType btCancel = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(btOk, btCancel);
        alert.showAndWait().ifPresent(buttonType -> {
            try {
                if(buttonType == btOk){
                    MessagesApplication.openLoginWindow();
                    alert.close();
                    stage.close();
                } else if (buttonType == btCancel) {
                    alert.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
