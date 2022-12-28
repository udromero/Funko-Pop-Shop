package com.dannydevs.funkopopshop.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dannydevs.funkopopshop.User;
import com.dannydevs.funkopopshop.Item;

import java.util.List;

@Dao
public interface FunkoPopShopDAO {
    @Query("DELETE FROM ITEM_TABLE")
    void deleteAll();

    // Users
    @Insert
    void insert(User...users);

    @Update
    void update(User...users);

    @Delete
    void delete(User...users);

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE)
    List<User> getAllUsers();

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE + " WHERE mUsername = :username")
    User getUserByUsername(String username);

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE + " WHERE mUserId =:userId")
    User getUserByUserId(int userId);

    @Query("DELETE FROM " + AppDataBase.USER_TABLE + " WHERE mUserId=:userId")
    void deleteUserById(int userId);

    // Items
    @Insert
    void insert(Item...items);

    @Update
    void update(Item...items);

    @Delete
    void delete(Item...items);

    @Query("SELECT * FROM " + AppDataBase.ITEM_TABLE)
    List<Item> getAllItems();

    @Query("SELECT * FROM " + AppDataBase.ITEM_TABLE + " WHERE mItemId =:itemId")
    Item getItemById(int itemId);

    @Query("SELECT * FROM " + AppDataBase.ITEM_TABLE + " WHERE mPicture =:url")
    Item getItemByPictureUrl(String url);

    @Query("UPDATE " + AppDataBase.ITEM_TABLE + " SET mPicture=:picture, mName=:name, mPrice=:price, mDescription=:description WHERE mItemId=:itemId")
    void updateItemInfo(String picture, String name, String price, String description, int itemId);

    @Query("DELETE FROM " + AppDataBase.ITEM_TABLE + " WHERE mItemId=:itemId")
    void deleteItemById(int itemId);
}
