package com.example.loginapp;

public class Users {
    String userID, name,lastname, correo;
    public Users( String userID, String name, String lastname, String correo){
        this.userID = userID;
        this.name = name;
        this.lastname = lastname;
        this.correo = correo;
    }

    public String getUserID() {
        return userID;
    }
    public String getName() {
        return name;
    }
    public String getLastname(){
        return lastname;
    }

    public String getCorreo() {
        return correo;
    }
}