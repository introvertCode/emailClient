package com.barosanu.controller;
import com.barosanu.EmailManager;
import com.barosanu.controller.services.LoginService;
import com.barosanu.model.EmailAccount;
import com.barosanu.view.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginWindowController extends BaseController implements Initializable {


    @FXML
    private TextField emailAddressField;

    @FXML
    private Label errorLabel;

    @FXML
    private PasswordField passwordField;

    public LoginWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName); //wywołuje konstruktor klasy z której dziediczymy
    }

    @FXML
    void loginButtonAction() {

        if(fieldsAreValid()) {
            EmailAccount emailAccount = new EmailAccount(emailAddressField.getText(), passwordField.getText());
            LoginService loginService = new LoginService(emailAccount, emailManager);
            loginService.start();
            loginService.setOnSucceeded(event-> {
                EmailLoginResult emailLoginResult= loginService.getValue();
                switch (emailLoginResult) {
                    case SUCCESS:
                        System.out.println("login succesfull!" + emailAccount);
                        if(!viewFactory.isMainViewInitialized()){
                            viewFactory.showMainWindow();
                        }

                        Stage stage = (Stage) errorLabel.getScene().getWindow();//w taki sposób dostajemy stage- za pomocą get wykonanym na jakimś polu z tego okna.
                        viewFactory.closeStage(stage);// zamyka okno logowania
                        return;
                    case FAILED_BY_CREDENTIALS:
                        errorLabel.setText("invalid credentials!");
                        return;
                    case FAILED_BY_UNEXPECTED_ERROR:
                        errorLabel.setText("unexpected error!");
                        return;
                    default:
                        return;
                }
            });



        }
            System.out.println("clicked login button");



    }

    private boolean fieldsAreValid() {
        if(emailAddressField.getText().isEmpty()) {
            errorLabel.setText("Please fill email");
            return false;
        }
        if (passwordField.getText().isEmpty()) {
            errorLabel.setText("Please fill password");
            return false;
        }
        return true;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {// by nie wpisywać za każdym razem initialize jest wywoływana od razu gdy kontroler jest przetworzony.
        emailAddressField.setText("javafxmail@interia.pl");
        passwordField.setText("Wy4T3HxjLciXzm");

    }
}
