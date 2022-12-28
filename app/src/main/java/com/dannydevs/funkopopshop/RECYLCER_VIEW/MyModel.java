package com.dannydevs.funkopopshop.RECYLCER_VIEW;

public class MyModel {
    int userId = -1;
    String name = "";
    String isAdmin = "";

    public MyModel(int userId, String name, String isAdmin) {
        this.userId = userId;
        this.name = name;
        this.isAdmin = isAdmin;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }
}
