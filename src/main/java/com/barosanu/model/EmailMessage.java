package com.barosanu.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import javax.mail.Message;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.util.Date;

public class EmailMessage {

    private SimpleStringProperty subject;
    private SimpleStringProperty sender;
    private SimpleStringProperty recipient;
    private SimpleObjectProperty<SizeInteger> size;
    private SimpleObjectProperty<Date> date;
    private boolean isRead;
    private Message message;

//    A JavaFX Property is a special kind member variable of JavaFX controls. JavaFX properties are typically used to
//    contain control properties such as X and Y position, width and height, text, children and other central properties
//    of JavaFX controls. You can attach change listeners to JavaFX properties so other components can get notified when
//    the value of the property changes, and you can bind properties to each other so when one property value changes,
//    so does the other. In this JavaFX property tutorial I will explain how JavaFX properties work, and how to use them.

    public EmailMessage(String subject, String sender, String recipient, int size, Date date, boolean isRead, Message message) throws UnsupportedEncodingException {
        this.subject = new SimpleStringProperty(subject);
        this.sender = new SimpleStringProperty(MimeUtility.decodeText(sender)); //DEKODOWANIE I DODANIE THROWS USUPPORTED ENCODING EXCEPTION
        this.recipient=new SimpleStringProperty(recipient);
        this.size= new SimpleObjectProperty<SizeInteger>(new SizeInteger(size));
        this.date= new SimpleObjectProperty<Date>(date);
        this.isRead = isRead;
        this.message = message;
    }

    public String getSubject(){
        return this.subject.get();
    }
    public String getSender(){
        return this.sender.get();
    }

    public String getRecipient(){
        return this.recipient.get();
    }

    public SizeInteger getSize(){
        return this.size.get();
    }

    public Date getDate(){
        return this.date.get();
    }

    public boolean isRead(){
        return this.isRead;
    }

    public void setRead(boolean read){
        isRead = read;
    }

    public Message getMessage(){
        return this.message;
    }


}
