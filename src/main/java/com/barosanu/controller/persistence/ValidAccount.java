package com.barosanu.controller.persistence;

import java.io.Serializable;

//Serialization is the process of converting an object into a stream of bytes to store the object or transmit it to memory, a database, or a file
public class ValidAccount implements Serializable { //serializable umożliwia serializowanie obiektów klasy
    private String address;
    private String password;

    public ValidAccount(String address, String password) {
        this.address = address;
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
