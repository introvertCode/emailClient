//package com.barosanu;
//
///**
// * Hello world!
// *
// */
//public class App
//{
//    public static void main( String[] args )
//    {
//        System.out.println( "Hello World!" );
//    }
//}

package com.barosanu;

import com.barosanu.controller.persistence.PersistenceAccess;
import com.barosanu.controller.persistence.ValidAccount;
import com.barosanu.controller.services.LoginService;
import com.barosanu.model.EmailAccount;
import com.barosanu.view.ViewFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    private PersistenceAccess persistenceAccess = new PersistenceAccess();
    private EmailManager emailManager = new EmailManager();


    @Override
    public void start(Stage stage) throws Exception {

        ViewFactory viewFactory = new ViewFactory(emailManager);
        List<ValidAccount> validAccountList = persistenceAccess.loadFromPersistence();
        if(validAccountList.size() > 0) {
            viewFactory.showMainWindow();
            for (ValidAccount validAccount : validAccountList){
                EmailAccount emailAccount = new EmailAccount(validAccount.getAddress(), validAccount.getPassword());
                LoginService loginService = new LoginService(emailAccount, emailManager);
                loginService.start();
            }

        } else {
            viewFactory.showLoginWindow();
        }

//        viewFactory.showLoginWindow();
//        viewFactory.updateStyles();
    }



    @Override
    public void stop() throws Exception {
        List<ValidAccount> validAccountList = new ArrayList<ValidAccount>();
        for (EmailAccount emailAccount:emailManager.getEmailAccounts()){
            validAccountList.add(new ValidAccount(emailAccount.getAddress(), emailAccount.getPassword()));
        }
        persistenceAccess.saveToPersistence(validAccountList);
    }
}
