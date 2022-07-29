package com.barosanu.controller;
import com.barosanu.EmailManager;
import com.barosanu.controller.services.LoginService;
import com.barosanu.model.EmailAccount;
import com.barosanu.view.ViewFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginWindowController extends BaseController {


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
            EmailLoginResult emailLoginResult= loginService.login();

            switch (emailLoginResult) {
                case SUCCESS:
                    System.out.println("login succesfull!" + emailAccount);
                    viewFactory.showMainWindow();
                    Stage stage = (Stage) errorLabel.getScene().getWindow();//w taki sposób dostajemy stage- za pomocą get wykonanym na jakimś polu z tego okna.
                    viewFactory.closeStage(stage);// zamyka okno logowania
                    return;
            }
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


}
