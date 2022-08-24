package com.barosanu.model;

import javax.mail.Store;
import java.util.Properties;

public class EmailAccount {
    private String address;
    private String password;
    private Properties properties;
    private Store store;

    public String getAddress() {
        return address;
    }


    public String getPassword() {
        return password;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public EmailAccount(String address, String password) {
        this.address = address;
        this.password = password;
        properties = new Properties();
        properties.put("incomingHost", "poczta.interia.pl");
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.transport.protocol", "smtps");
        properties.put("mail.smtps.host", "poczta.interia.pl");
        properties.put("mail.smtps.auth", "true");
        properties.put("outgoingHost", "poczta.interia.pl");


    }

    @Override
    public String toString() {//by wyświetlało poprawnie adres w choiceBox przy wysyłaniu wiadomości
        return address;
    }

}
