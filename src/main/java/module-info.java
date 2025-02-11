module com.juanpascual.messagesfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens com.juanpascual.messagesfx to javafx.fxml;
    exports com.juanpascual.messagesfx;
    exports com.juanpascual.messagesfx.models;
    opens com.juanpascual.messagesfx.models to com.google.gson;
    exports com.juanpascual.messagesfx.controllers;
    opens com.juanpascual.messagesfx.controllers to javafx.fxml, com.google.gson;
    exports com.juanpascual.messagesfx.responses;
    opens com.juanpascual.messagesfx.responses to com.google.gson;
    exports com.juanpascual.messagesfx.utils;
    opens com.juanpascual.messagesfx.utils to com.google.gson;
}