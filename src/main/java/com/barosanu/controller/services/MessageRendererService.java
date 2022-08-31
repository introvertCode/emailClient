package com.barosanu.controller.services;

import com.barosanu.model.EmailMessage;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.web.WebEngine;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import java.io.IOException;

public class MessageRendererService extends Service {

    private EmailMessage emailMessage;
    private WebEngine webEngine;
    private StringBuffer stringBuffer;

//    private boolean thisAttachmentExists = false;

    public MessageRendererService(WebEngine webEngine) {
        this.webEngine = webEngine;
        this.stringBuffer = new StringBuffer();
//        System.out.println(this.getState() + " przed");
        this.setOnSucceeded(event -> { //onSucceeded jest wywoływany gdy Task osiągnie taki stan, Task jest niżej w kodzie (jak klikniemy wiadomosc wywołuje się metoda restart, która zaczyna taska)
//            System.out.println(this.getState() + " wykonalo sie");
            displayMessage();
        });
//        System.out.println(this.getState() + " po");

    }

    public void setEmailMessage(EmailMessage emailMessage) {
        this.emailMessage = emailMessage;
    }

    private void displayMessage() {
        webEngine.loadContent(stringBuffer.toString());

    }

    @Override
    protected Task createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                try {
//                    System.out.println("Wywolany task");
                    loadMessage();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    private void loadMessage() throws MessagingException, IOException {
        stringBuffer.setLength(0); //clean stringBuffer
        emailMessage.clearAttachmentList();
        Message message = emailMessage.getMessage();
        String contentType = message.getContentType();
        if (isSimpleType(contentType)) {
            stringBuffer.append(message.getContent().toString());
        } else if (isMultipartType(contentType)) {
            Multipart multipart = (Multipart) message.getContent();
            loadMultipart(multipart, stringBuffer); //rekurencja
        }
    }

    private void loadMultipart(Multipart multipart, StringBuffer stringBuffer) throws MessagingException, IOException {
//        thisAttachmentExists = false;
        for (int i = multipart.getCount() - 1; i >= 0; i--) { //i>=0 w oryginale, ale wtedy wyswietla podwojnie
            BodyPart bodyPart = multipart.getBodyPart(i);
            String contentType = bodyPart.getContentType();
//            System.out.println(contentType);
            if (isSimpleType(contentType)) {
                if(!isTextPlain(contentType)){
                    stringBuffer.append(bodyPart.getContent().toString());
                }

            } else if(isMultipartType(contentType)){
                Multipart multipart2 = (Multipart) bodyPart.getContent();
                loadMultipart(multipart2, stringBuffer);
            } else if(!isTextPlain(contentType)){ //tutaj obsługujemy załączniki
                MimeBodyPart mbp = (MimeBodyPart) bodyPart; //załącznik jest typu MimeBodyPart
                emailMessage.addAttachment(mbp);

//                for(MimeBodyPart mimeBodyPart: emailMessage.getAttachmentList()) {
//                    if(mbp.getFileName() == mimeBodyPart.getFileName()){
////                        System.out.println(mbp.getFileName());
//                        thisAttachmentExists = true;
//                    }
//                }
//                if (!thisAttachmentExists){
//                    emailMessage.addAttachment(mbp);
//                    thisAttachmentExists = false;
//                }

            }
        }
    }

    private boolean isTextPlain(String contentType){
        return contentType.contains("text/plain");
    }

    private boolean isSimpleType(String contentType) {
        if (contentType.contains("text/html") ||
//            contentType.contains("mixed") ||
            contentType.contains("text")) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isMultipartType(String contentType) {
        if (contentType.contains("multipart")) {
            return true;
        } else {
            return false;
        }
    }
}
