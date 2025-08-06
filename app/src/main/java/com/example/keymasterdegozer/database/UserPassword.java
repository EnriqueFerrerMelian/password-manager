package com.example.keymasterdegozer.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_passwords")
public class UserPassword {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;
    public String account, password, platform;

    public UserPassword(String account, String password, String platform) {
        this.account = account;
        this.platform = platform;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }


    @Override
    public String toString() {
        return "UserPassword{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", platform='" + platform + '\'' +
                '}';
    }
}
