package com.dannydevs.funkopopshop;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.dannydevs.funkopopshop.DB.AppDataBase;

import java.util.ArrayList;

@Entity(tableName = AppDataBase.USER_TABLE)
public class User {

    @PrimaryKey(autoGenerate = true)
    private int mUserId;

    private String mUsername;
    private String mPassword;
    private boolean mIsAdmin; // IntelliJ Suggestion: made final, may not work?
    private String mCartStringList;

    public User(String username, String password, boolean isAdmin, String cartStringList) {
        mUsername = username;
        mPassword = password;
        mIsAdmin = isAdmin;
        mCartStringList = cartStringList;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public boolean isAdmin() {
        return mIsAdmin;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getCartStringList() {
        return mCartStringList;
    }

    public void setCartStringList(String cartStringList) {
        mCartStringList = cartStringList;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "mUserId=" + mUserId +
                ", mUsername='" + mUsername + '\'' +
                ", mPassword='" + mPassword + '\'' +
                ", mIsAdmin=" + mIsAdmin +
                ", mCartStringList='" + mCartStringList + '\'' +
                '}';
    }
}
