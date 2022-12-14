package com.barosanu.controller.services;


import com.barosanu.controller.EmailSendingResult;
import com.barosanu.model.EmailAccount;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EmailSenderService extends Service<EmailSendingResult> { // w  rezultacie zwraca wyniki z EmailSendingResult

    private EmailAccount emailAccount;
    private String subject;
    private String recipient;
    private String content;
    private List<File> attachments;


    public EmailSenderService(EmailAccount emailAccount, String subject, String recipient, String content, List<File> attachments) {
        this.emailAccount = emailAccount;
        this.subject = subject;
        this.recipient = recipient;
        this.content = content;
        this.attachments = attachments;
    }



    @Override
    protected Task<EmailSendingResult> createTask() {
        return new Task<EmailSendingResult>() {
            @Override
            protected EmailSendingResult call() throws Exception {
                try {
                    //create message
                    MimeMessage mimeMessage = new MimeMessage(emailAccount.getSession());
                    mimeMessage.setFrom(emailAccount.getAddress());
                    mimeMessage.addRecipients(Message.RecipientType.TO, recipient);
                    mimeMessage.setSubject(subject);

                    Multipart multipart = new MimeMultipart();
                    BodyPart messageBodyPart = new MimeBodyPart();
                    messageBodyPart.setContent(content, "text/html");
                    multipart.addBodyPart(messageBodyPart);
                    mimeMessage.setContent(multipart);
                    // adding attachments:
                    if(attachments.size()>0){
                        for (File file:attachments){
                            MimeBodyPart mimeBodyPart = new MimeBodyPart();
                            DataSource source = new FileDataSource(file.getAbsolutePath());
                            mimeBodyPart.setDataHandler(new DataHandler(source));
                            mimeBodyPart.setFileName(file.getName());
                            multipart.addBodyPart(mimeBodyPart);
                        }
                    }



                    //Sending the message:
                    Transport transport = emailAccount.getSession().getTransport();
                    transport.connect(
                        emailAccount.getProperties().getProperty("outgoingHost"),
                        emailAccount.getAddress(),
                        emailAccount.getPassword()
                        );
                    transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
                    transport.close();
                    copyMessageIntoSentFolder(mimeMessage);
                    return EmailSendingResult.SUCCESS;
                } catch (MessagingException e){
                    e.printStackTrace();
                    return EmailSendingResult.FAILED_BY_PROVIDER;
                } catch (Exception e){
                    e.printStackTrace();
                    return EmailSendingResult.FAILED_BY_UNEXPECTED_ERROR;
                }
            }
        };
    }

    private void copyMessageIntoSentFolder(MimeMessage mimeMessage) throws MessagingException{

        Store store = emailAccount.getSession().getStore();
        store.connect(
            emailAccount.getProperties().getProperty("outgoingHost"),
            emailAccount.getAddress(),
            emailAccount.getPassword());

        Folder folder = (Folder) store.getFolder("Wys??ane");
        if (!folder.exists()) {
            folder.create(Folder.HOLDS_MESSAGES);
        }
        folder.open(Folder.READ_WRITE);

        folder.appendMessages(new Message[]{mimeMessage});
        folder.close();
        store.close();
    }
}
