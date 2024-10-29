package com.example.password_manager.util;

public class Password {
    String data;

    public Password(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Password{" +
                "data='" + data + '\'' +
                '}';
    }
}
