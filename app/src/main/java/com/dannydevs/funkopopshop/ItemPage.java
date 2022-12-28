package com.dannydevs.funkopopshop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.dannydevs.funkopopshop.DB.AppDataBase;
import com.dannydevs.funkopopshop.DB.FunkoPopShopDAO;
import com.dannydevs.funkopopshop.databinding.ActivityItemPageBinding;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemPage extends AppCompatActivity {
    private static final String PREFERENCES_KEY = "com.dannydevs.funkopopshop.PREFERENCES_KEY";

    private static final String CART_KEY = "com.dannydevs.funkopopshop.cartKey";
    private static final String USER_ID_KEY = "com.dannydevs.funkopopshop.userIdKey";

    private static final String ITEM_ID_KEY = "com.dannydevs.funkopopshop.ITEM_ID_KEY";

    ActivityItemPageBinding binding;

    TextView mItemNameTextDisplay;
    TextView mItemIdTextDisplay;
    TextView mItemPriceTextDisplay;
    TextView mItemDescTextDisplay;

    Button mItemPageBackButton;
    Button mItemPageCartButton;

    ImageView mItemPictureDisplay;

    FunkoPopShopDAO mFunkoPopShopDAO;

    SharedPreferences userSharedPreferences = null;

    Item mItem;
    int mItemId;

    User mUser;
    int mUserId;

    List<Item> mUserCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        binding = ActivityItemPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Wire-up Display
        wireUpDisplay();

        // Get data base
        getDataBase();

        // Get user preferences
        getPrefs();

        //Grab user
        getUserFromSharedPreferences();

        mUserCart = new ArrayList<>();

        // Check for valid item ID else return to landing page
        mItemId = getIntent().getIntExtra(ITEM_ID_KEY, -1);
        checkValidItemID(mItemId);

        // Grab Item using itemId, check item was grabbed properly
        getItem();
        checkValidItem();

        // Display item info
        setItemDisplayValues();

        mItemPageBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemPage.this, LandingPage.class);
                // TODO: ensure cart values transfer via shared prefrences
                startActivity(intent);
            }
        });

        mItemPageCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mItemPageCartButton.getText().equals("Go to Cart")){
                    Intent intent = new Intent(ItemPage.this, UserCartPage.class);
                    SharedPreferences.Editor editor = userSharedPreferences.edit();
                    editor.putString(CART_KEY, mUser.getCartStringList());
                    editor.apply();
                    startActivity(intent);
                } else {
                    addItemToUserCart();
                }

                mItemPageCartButton.setText(getString(R.string.itemPageCartButtonText, "Go"));
            }
        });
    }

    // Support Functions
    private void wireUpDisplay(){
        mItemNameTextDisplay = binding.itemPageItemName;
        mItemPriceTextDisplay = binding.itemPageItemPrice;
        mItemIdTextDisplay = binding.itemPageItemId;
        mItemDescTextDisplay = binding.itemPageDescriptionBox;
        mItemDescTextDisplay.setMovementMethod(new ScrollingMovementMethod());

        mItemPageBackButton = binding.itemPageBackButton;
        mItemPageCartButton = binding.itemPageAddToCartButton;

        mItemPictureDisplay = binding.itemPageItemPicture;
    }

    private void getDataBase(){
        mFunkoPopShopDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .FunkoPopShopDAO();
    }

    private void getPrefs() {
        userSharedPreferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    private void checkValidItemID(int itemId){
        if(itemId == -1){
            Toast.makeText(ItemPage.this, "Invalid Item ID Error: Returning to landing page", Toast.LENGTH_SHORT).show();
            kickBackToLandingPage();
        }
    }

    private void kickBackToLandingPage(){
        Intent intent = new Intent(ItemPage.this, LandingPage.class);
        startActivity(intent);
    }

    private void getItem(){
        mItem = mFunkoPopShopDAO.getItemById(mItemId);
        checkValidItem();
    }

    private void checkValidItem(){
        if(mItem == null){
            kickBackToLandingPage();
        }
    }

    private void setItemDisplayValues(){
        mItemNameTextDisplay.setText(getString(R.string.itemPageItemNameString, mItem.getName()));

        //Check if user is admin to display item id or not
        String stringId = String.valueOf(mItem.getItemId());
        if(mUser.isAdmin()){
            mItemIdTextDisplay.setText(getString(R.string.itemPageItemIdTextDisplay, stringId));
        } else {
            mItemIdTextDisplay.setVisibility(View.INVISIBLE);
        }

        mItemPriceTextDisplay.setText(getString(R.string.itemPageItemPrice, mItem.getPrice()));
        mItemDescTextDisplay.setText(getString(R.string.itemPageDescription, mItem.getDescription()));

        mItemPageCartButton.setText(getString(R.string.itemPageCartButtonText, "Add"));

        setItemPictureDisplay();
    }

    private void setItemPictureDisplay(){
        String url = mItem.getPicture();
        Picasso.get().load(url).into(mItemPictureDisplay);
    }

    private void getUserFromSharedPreferences(){
        mUserId = userSharedPreferences.getInt(USER_ID_KEY, -1);
        mUser = mFunkoPopShopDAO.getUserByUserId(mUserId);
    }

    private void addItemToUserCart(){
        String currCart = mUser.getCartStringList();
        String itemId = String.valueOf(mItemId);
        String newCart = currCart + "," + itemId;
        mUser.setCartStringList(newCart);
    }
}
