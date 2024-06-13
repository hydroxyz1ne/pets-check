package com.example.petscheck;

public class Users {
    public String email,login;

    public Users() {
    }

    public Users(String email, String login) {
        this.email = email;
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public String getLogin() {
        return login;
    }
}

