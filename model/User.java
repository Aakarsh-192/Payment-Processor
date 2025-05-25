package model;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private String phone;
    private double balance;

    public User(String username, String password, String phone, double balance) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.balance = balance;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getRecipientId() {
        String usernamePart = username.length() >= 3 ? username.substring(0, 3).toLowerCase() : username.toLowerCase();
        String phonePart = phone.length() >= 3 ? phone.substring(phone.length() - 3) : phone;
        return usernamePart + phonePart + "@guviCM";
    }
}