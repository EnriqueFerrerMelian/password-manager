package com.example.keymasterdegozer.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_passwords")
public class UserPassword {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String account, password, platform, notas;
    private int iconInt;

    public UserPassword(String account, String password, String platform, int iconInt) {
        this.account = account;
        this.password = password;
        this.platform = platform;
        this.iconInt = iconInt;
    }

    public int getIconInt() { return iconInt;}

    public void setIconInt(int iconInt) {
        this.iconInt = iconInt;
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

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    @Override
    public String toString() {
        return "UserPassword{" +
                "id=" + id +
                ", iconInt=" + iconInt +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", platform='" + platform + '\'' +
                '}';
    }
}
