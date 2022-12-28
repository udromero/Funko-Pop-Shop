package com.dannydevs.funkopopshop;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.dannydevs.funkopopshop.DB.AppDataBase;

@Entity(tableName = AppDataBase.ITEM_TABLE)
public class Item {

    @PrimaryKey(autoGenerate = true)
    private int mItemId;

    private String mName;
    private String mPrice;
    private String mPicture;
    private String mDescription;

    public Item(String name, String price, String picture, String description) {
        mName = name;
        mPrice = price;
        mPicture = picture;
        mDescription = description;
    }

    public int getItemId() {
        return mItemId;
    }

    public void setItemId(int itemId) {
        mItemId = itemId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public String getPicture() {
        return mPicture;
    }

    public void setPicture(String picture) {
        mPicture = picture;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    @NonNull
    @Override
    public String toString() {
        return "Item{" +
                "mItemId=" + mItemId +
                ", mName='" + mName + '\'' +
                ", mItemPrice='" + mPrice + '\'' +
                ", mPicture='" + mPicture + '\'' +
                ", mDescription='" + mDescription + '\'' +
                '}';
    }
}
