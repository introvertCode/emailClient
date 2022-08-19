package com.barosanu.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class EmailTreeItem<String> extends TreeItem<String> {
    private String name;
    private ObservableList<EmailMessage> emailMessages;
    private int unreadMessageCount;


    public EmailTreeItem(String name) {
        super(name); // super przywołuje konstruktor klasy nadrzędnej TreeItem TreeItem(T value),  i jako wartość przypisuje nazwę folderu name.
        this.name = name;
        this.emailMessages = FXCollections.observableArrayList();
    }

    public ObservableList<EmailMessage> getEmailMessages(){
        return emailMessages;
    }

    public void addEmail(Message message) throws MessagingException {
        //sprawdzenie jaka flaga jest przypisana do maila
        boolean messageIsRead = message.getFlags().contains(Flags.Flag.SEEN);
        EmailMessage emailMessage = new EmailMessage(
                message.getSubject(),
                message.getFrom()[0].toString(),
                message.getRecipients(MimeMessage.RecipientType.TO)[0].toString(),
                message.getSize(),
                message.getSentDate(),
                messageIsRead,
                message
        );
        emailMessages.add(emailMessage);
        if(!messageIsRead){
            incrementMessagesCount();
        }
        System.out.println("added to " + name + " " + message.getSubject());
    }

    public void incrementMessagesCount(){
        unreadMessageCount++;
        updateName();
    }

    private void updateName() {
        if(unreadMessageCount>0) {
            this.setValue((String) (name +"(" + unreadMessageCount + ")")); //funkcja setValue() przypisuje wartość do TreeItem, w konstruktorze w funkcji super(name) podane jest, że wartością jest nazwa
        } else {
            this.setValue(name);
        }
    }
}
