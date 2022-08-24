package com.barosanu.controller.services;

import com.barosanu.EmailManager;
import com.barosanu.controller.EmailLoginResult;
import com.barosanu.model.EmailAccount;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import javax.mail.*;
//import javax.mail.Authenticator;

public class LoginService extends Service<EmailLoginResult> {
    EmailAccount emailAccount;
    EmailManager emailManager;

    public LoginService(EmailAccount emailAccount, EmailManager emailManager) {
        this.emailAccount = emailAccount;
        this.emailManager = emailManager;
    }

    private EmailLoginResult login(){
        Authenticator authenticator = new Authenticator(){
            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(emailAccount.getAddress(), emailAccount.getPassword());
            }
        };

        try {
//            Thread.sleep(6000);
            Session session = Session.getInstance(emailAccount.getProperties(), authenticator);
            emailAccount.setSession(session);
            Store store = session.getStore("imaps");
            store.connect(emailAccount.getProperties().getProperty("incomingHost"), emailAccount.getAddress(), emailAccount.getPassword().toString());
            emailAccount.setStore(store);
            emailManager.addEmailAccount(emailAccount);
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            return  EmailLoginResult.FAILED_BY_NETWORK;
        } catch (AuthenticationFailedException e) {
            e.printStackTrace();
            return  EmailLoginResult.FAILED_BY_CREDENTIALS;
        } catch (MessagingException e) {
            e.printStackTrace();
            return  EmailLoginResult.FAILED_BY_UNEXPECTED_ERROR;
        } catch (Exception e) {
            e.printStackTrace();
            return EmailLoginResult.FAILED_BY_UNEXPECTED_ERROR;
        }
        return EmailLoginResult.SUCCESS;

    }

    @Override
    protected Task<EmailLoginResult> createTask() {
        return new Task<EmailLoginResult>() {
            @Override
            protected EmailLoginResult call() throws Exception {
                return login();
            }
        };

    }
}
