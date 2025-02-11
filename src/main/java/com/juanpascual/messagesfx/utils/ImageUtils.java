package com.juanpascual.messagesfx.utils;

import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class ImageUtils {
    public static String toString(File image){
        Path path;
        String data = "";
        byte[] bytes;
        try {
            path = Paths.get(image.getAbsolutePath());
            bytes = Files.readAllBytes(path);
            data = Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e){
            AlertUtils.showError("Error", "Image processing error.");
            e.printStackTrace();
        }

        return data;
    }

    public static File jpgChooser(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a image...");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg"));
        return fileChooser.showOpenDialog(null);
    }
}
