package com.barosanu.controller;

import com.barosanu.EmailManager;
import com.barosanu.controller.services.EmailSenderService;
import com.barosanu.model.EmailAccount;
import com.barosanu.view.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ComposeMessageControler extends BaseController implements Initializable {

    private List<File> attachments = new ArrayList<File>();

    @FXML
    private ChoiceBox<EmailAccount> emailAccountChoice;

    @FXML
    private Label errorLabel;

    @FXML
    private HTMLEditor htmlEditor;

    @FXML
    private TextField recipientTextField;

    @FXML
    private TextField subjectTextField;

    @FXML
    private Button sendButton;

    @FXML
    void attachBtnAction() {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if(selectedFile != null){
            attachments.add(selectedFile);
        }
    }


    @FXML
    void sendButtonAction() {
        EmailSenderService emailSenderService = new EmailSenderService(
            emailAccountChoice.getValue(),
            subjectTextField.getText(),
            recipientTextField.getText(),
            htmlEditor.getHtmlText(),
            attachments
        );
        emailSenderService.start();
        sendButton.setDisable(true);
        emailSenderService.setOnSucceeded(e->{
            EmailSendingResult emailSendingResult = emailSenderService.getValue(); //sprawdza wartość wątku
            sendButton.setDisable(false);
            switch (emailSendingResult){
                case SUCCESS:
                    Stage stage = (Stage) recipientTextField.getScene().getWindow();
                    viewFactory.closeStage(stage);
                    break;
                case FAILED_BY_UNEXPECTED_ERROR:
                    errorLabel.setText("Unexpected error!");
                    break;
                case FAILED_BY_PROVIDER:
                    errorLabel.setText("Provider error!");
                    break;
            }
        });
    }

    public ComposeMessageControler(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        emailAccountChoice.setItems(emailManager.getEmailAccounts());
        emailAccountChoice.setValue(emailManager.getEmailAccounts().get(0));
    }
}
