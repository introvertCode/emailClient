module emailClient2 {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.web;
    requires activation;
    requires javax.mail.api;



    opens com.barosanu;
    opens com.barosanu.view;
    opens com.barosanu.controller;
    opens com.barosanu.model;
}