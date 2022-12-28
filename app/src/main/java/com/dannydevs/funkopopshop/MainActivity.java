package com.dannydevs.funkopopshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.PrimaryKey;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dannydevs.funkopopshop.DB.AppDataBase;
import com.dannydevs.funkopopshop.DB.FunkoPopShopDAO;
import com.dannydevs.funkopopshop.databinding.ActivityLandingPageBinding;
import com.dannydevs.funkopopshop.databinding.ActivityMainBinding;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String USER_ID_KEY = "com.dannydevs.funkopopshop.userIdKey";
    private static final String USERNAME_KEY = "com.dannydevs.funkopopshop.usernameKey";
    private static final String PASSWORD_KEY = "com.dannydevs.funkopopshop.passwordKey";
    private static final String IS_ADMIN_KEY = "com.dannydevs.funkopopshop.isAdminKey";
    private static final String PREFERENCES_KEY = "com.dannydevs.funkopopshop.PREFERENCES_KEY";

    private static String choice;

    TextView mMainDisplay;

    Button mLoginButton;
    Button mCreateAccountButton;

    FunkoPopShopDAO mFunkoPopShopDAO;

    SharedPreferences userSharedPreferences;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        destroyDataBase();
//        resetAllUserPreferences();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Activity Binding
        mMainDisplay = binding.mainMainDisplay;
        mMainDisplay.setMovementMethod(new ScrollingMovementMethod());

        mLoginButton = binding.mainLoginButton;
        mCreateAccountButton = binding.mainCreateAccountButton;

        if(checkValidPrefsExist()){
            getPrefs();

            // If logged in take to landing page
            boolean loggedInAlready = userSharedPreferences.getBoolean("loggedIn", false);
            int userId = userSharedPreferences.getInt(USER_ID_KEY, -1);

            if (loggedInAlready) {
                Toast.makeText(MainActivity.this,"Skipping to Landing Page", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, LandingPage.class);
                startActivity(intent);
            }
        } else {
            userSharedPreferences = null;
            addUserToPreferences();
        }

        // User clicked create account button
        mCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choice = "create";
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("choice", choice);
                startActivity(intent);
            }
        });

        // User clicked login button
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choice = "login";
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("choice", choice);
                startActivity(intent);
            }
        });
    }

    private void addUserToPreferences() {
        if(userSharedPreferences == null){
            userSharedPreferences = getSharedPreferences(PREFERENCES_KEY, MODE_PRIVATE);
        }

        SharedPreferences.Editor editor = userSharedPreferences.edit();

        editor.putInt(USER_ID_KEY, -1);
        editor.putBoolean("loggedIn", false);
        editor.apply();
    }

    private boolean checkValidPrefsExist(){
        userSharedPreferences = getSharedPreferences(PREFERENCES_KEY, MODE_PRIVATE);

        if(userSharedPreferences != null){
            int userId = userSharedPreferences.getInt(USER_ID_KEY, -1);

            return userId != -1;
        }

        return false;
    }

    private void getPrefs(){
        userSharedPreferences = getSharedPreferences(PREFERENCES_KEY, MODE_PRIVATE);
    }

    private void resetAllUserPreferences(){
        getPrefs();
        SharedPreferences.Editor editor = userSharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    // Support Function: Destroy All Data Base
    private void destroyDataBase(){
        getBaseContext().deleteDatabase(AppDataBase.DATABASE_NAME);
    }
}