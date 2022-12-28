package com.dannydevs.funkopopshop.DB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.dannydevs.funkopopshop.User;
import com.dannydevs.funkopopshop.Item;

@Database(entities = {User.class, Item.class}, version = 5)
public abstract class AppDataBase extends RoomDatabase {
    public static final String DATABASE_NAME = "FUNKOPOPSHOP_DATABASE";
    public static final String USER_TABLE = "USER_TABLE";
    public static final String ITEM_TABLE = "ITEM_TABLE";

    public abstract FunkoPopShopDAO FunkoPopShopDAO();
}
