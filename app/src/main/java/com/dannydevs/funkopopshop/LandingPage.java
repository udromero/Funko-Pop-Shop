package com.dannydevs.funkopopshop;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dannydevs.funkopopshop.DB.AppDataBase;
import com.dannydevs.funkopopshop.DB.FunkoPopShopDAO;
import com.dannydevs.funkopopshop.databinding.ActivityItemOperationsBinding;
import com.dannydevs.funkopopshop.databinding.ActivityLandingPageBinding;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class LandingPage extends AppCompatActivity {
    private static final String USER_ID_KEY = "com.dannydevs.funkopopshop.userIdKey";
    private static final String USERNAME_KEY = "com.dannydevs.funkopopshop.usernameKey";
    private static final String PASSWORD_KEY = "com.dannydevs.funkopopshop.passwordKey";
    private static final String IS_ADMIN_KEY = "com.dannydevs.funkopopshop.isAdminKey";
    private static final String CART_KEY = "com.dannydevs.funkopopshop.cartKey";

    private static final String PREFERENCES_KEY = "com.dannydevs.funkopopshop.PREFERENCES_KEY";

    private static final String ITEM_ID_KEY = "com.dannydevs.funkopopshop.ITEM_ID_KEY";
    private static final String INDEX_KEY = "com.dannydevs.funkopopshop.INDEX_KEY";

    TextView mLandingDisplay;

    Button mAdminButton;
    Button mLogoutButton;
    Button mNextButton;
    Button mPreviousButton;

    ImageView mImageOne;
    ImageView mImageTwo;
    ImageView mImageThree;
    ImageView mImageFour;

    List<Item> mAllItems;
    Item mCurrItem1;
    Item mCurrItem2;
    Item mCurrItem3;
    Item mCurrItem4;

    int index;
    boolean hasVisited = false;

    FunkoPopShopDAO mFunkoPopShopDAO;

    SharedPreferences userSharedPreferences = null;

    ActivityLandingPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        binding = ActivityLandingPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Retrieving database
        getDataBase();

        // Getting user shared preferences
        getPrefs();

        // Binding our display
        mLandingDisplay = binding.landingLandingDisplay;

        mAdminButton = binding.landingAdminButton;
        mLogoutButton = binding.landingLogoutButton;
        mNextButton = binding.landingNextButton;
        mPreviousButton = binding.landingPreviousButton;

        // Retrieving all user information via user shared preferences
        int mUserId = userSharedPreferences.getInt(USER_ID_KEY, -1);

        // Check for a valid user based on user id not being default value (-1)
        checkValidUser(mUserId);

        // Fetch current user's 'user' object via user id
        User mUser = mFunkoPopShopDAO.getUserByUserId(mUserId);

        // If user is an admin, display access to admin button
        boolean mIsAdmin = mUser.isAdmin();
        checkIsAdmin(mIsAdmin);

        // Display welcome message with user's username
        String mUsername = mUser.getUsername();
        mLandingDisplay.setText(getString(R.string.welcome, mUsername));

        // Shop Stuff
        mImageOne = binding.landingItemImage1;
        mImageTwo = binding.landingItemImage2;
        mImageThree = binding.landingItemImage3;
        mImageFour = binding.landingItemImage4;

        getShopItems();

        hasVisited = getIntent().getBooleanExtra("visited", false);

        if(hasVisited){
            index = getIntent().getIntExtra(INDEX_KEY, 0);
        } else {
            index = 0;
        }

        if(index < 0){
            index = 0;
        }

//        Toast.makeText(this, String.valueOf(mAllItems), Toast.LENGTH_SHORT).show();

        for(int i = 0; i < mAllItems.size() + 1 - index; i++){
            Item currItem = mAllItems.get(index);
            if(currItem == null){
                break;
            } else {
                String url = currItem.getPicture();

                if(i == 0){
                    Picasso.get().load(url).into(mImageOne);
                    mCurrItem1 = currItem;
                } else if (i == 1){
                    Picasso.get().load(url).into(mImageTwo);
                    mCurrItem2 = currItem;
                } else if (i == 2){
                    Picasso.get().load(url).into(mImageThree);
                    mCurrItem3 = currItem;
                } else if (i == 3){
                    Picasso.get().load(url).into(mImageFour);
                    mCurrItem4 = currItem;
                }

                if(index + 1 < mAllItems.size()){
                    index++;
                }
            }

        }

//        Toast.makeText(this, String.valueOf(index), Toast.LENGTH_SHORT).show();

        checkItemsToDisplay();

        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkIfLessItemsToDisplay(index)){
                    Intent intent = new Intent(getApplicationContext(), LandingPage.class);
                    index-=3;
                    intent.putExtra(INDEX_KEY, index);
                    intent.putExtra("visited", true);
                    startActivity(intent);
                }
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkIfMoreItemsToDisplay(index)){
                    Intent intent = new Intent(getApplicationContext(), LandingPage.class);
                    index -= 1;
                    intent.putExtra(INDEX_KEY, index);
                    intent.putExtra("visited", true);
                    startActivity(intent);
                }
            }
        });

        // Go to admin dashboard
        mAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LandingPage.this, AdminDashboard.class);
                startActivity(intent);
            }
        });

        // Logout button clicked
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutButtonAction();
            }
        });

        mImageOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LandingPage.this, ItemPage.class);
                intent.putExtra(ITEM_ID_KEY, mCurrItem1.getItemId());
                startActivity(intent);
            }
        });

        mImageTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LandingPage.this, ItemPage.class);
                intent.putExtra(ITEM_ID_KEY, mCurrItem2.getItemId());
                startActivity(intent);
            }
        });

        mImageThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LandingPage.this, ItemPage.class);
                intent.putExtra(ITEM_ID_KEY, mCurrItem3.getItemId());
                startActivity(intent);
            }
        });

        mImageFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LandingPage.this, ItemPage.class);
                intent.putExtra(ITEM_ID_KEY, mCurrItem4.getItemId());
                startActivity(intent);
            }
        });
    }

    // SHOP FUNCTIONS
    private void getShopItems(){
        mAllItems = mFunkoPopShopDAO.getAllItems();

        if(mAllItems.size() == 0){
            Toast.makeText(LandingPage.this, "No items found", Toast.LENGTH_SHORT).show();
        }
    }

    private void getPrefs() {
        userSharedPreferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);

        if(userSharedPreferences == null){
            forceLogoutUser();
        }
    }

    private void checkIsAdmin(boolean isAdmin){
        if(isAdmin){
            mAdminButton.setVisibility(View.VISIBLE);
        }
    }

    private void logoutButtonAction(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage(R.string.logout);

        alertBuilder.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearUserFromPref();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });
        alertBuilder.setNegativeButton(getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        // don't need anything here
                    }
                });

        alertBuilder.create().show();
    }

    private void clearUserFromPref(){
        SharedPreferences.Editor editor = userSharedPreferences.edit();
        editor.putInt(USER_ID_KEY, -1);
        editor.putBoolean("loggedIn", false);
        editor.putString(USERNAME_KEY, "");
        editor.putString(PASSWORD_KEY, "");
        editor.putBoolean(IS_ADMIN_KEY, false);
        editor.putString(CART_KEY, "");
        editor.apply();
    }

    private void getDataBase(){
        mFunkoPopShopDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .FunkoPopShopDAO();
    }

    private void forceLogoutUser(){
        clearUserFromPref();
        Toast.makeText(LandingPage.this, "User Verification Error: Forcefully Logging Out", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }


    private void checkValidUser(int userId){
        if(userId == -1){
             Toast.makeText(LandingPage.this, "User Verification Error: Forcefully Logging Out", Toast.LENGTH_SHORT).show();
            forceLogoutUser();
        }
    }

    private void checkItemsToDisplay(){
        if(mCurrItem1 == null){
            mImageOne.setVisibility(View.INVISIBLE);
        }

        if(mCurrItem2 == null){
            mImageTwo.setVisibility(View.INVISIBLE);
        }

        if(mCurrItem3 == null){
            mImageThree.setVisibility(View.INVISIBLE);
        }

        if(mCurrItem4 == null){
            mImageFour.setVisibility(View.INVISIBLE);
        }
    }

    private boolean checkIfMoreItemsToDisplay(int index){
        if(index <= mAllItems.size()) {
            return true;
        }

        return false;
    }

    private boolean checkIfLessItemsToDisplay(int index){
        if(index - 4 > 4) {
            return true;
        }

        return false;
    }

}