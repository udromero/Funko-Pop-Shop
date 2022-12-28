package com.dannydevs.funkopopshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dannydevs.funkopopshop.DB.AppDataBase;
import com.dannydevs.funkopopshop.DB.FunkoPopShopDAO;
import com.dannydevs.funkopopshop.databinding.ActivityLoginBinding;
import com.dannydevs.funkopopshop.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private static final String USER_ID_KEY = "com.dannydevs.funkopopshop.userIdKey";
    private static final String USERNAME_KEY = "com.dannydevs.funkopopshop.usernameKey";
    private static final String PASSWORD_KEY = "com.dannydevs.funkopopshop.passwordKey";
    private static final String IS_ADMIN_KEY = "com.dannydevs.funkopopshop.isAdminKey";
    private static final String PREFERENCES_KEY = "com.dannydevs.funkopopshop.PREFERENCES_KEY";

    private static String choice;

    private TextView mLoginDisplay;
    private TextView mAdminDisplay;

    private EditText mUsernameField;
    private EditText mPasswordField;
    private EditText mAdminKeyField;

    private Button mButton;

    private FunkoPopShopDAO mFunkoPopShopDAO;

    private String mUsername;
    private String mPassword;
    private String mAdminKey;
    private boolean mIsAdmin;
    private User mUser;

    private SharedPreferences mPreferences;

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Retrieving Database
        getDataBase();

        // Get user shared preferences
        getPrefs();

        // Activity binding
        mLoginDisplay = binding.loginLoginDisplay;
        mAdminDisplay = binding.loginAdminDisplay;

        mUsernameField = binding.loginUsernameEditText;
        mPasswordField = binding.loginPasswordEditText;
        mAdminKeyField = binding.loginAdminKeyText;

        // Get user's button choice via an extra
        choice = getIntent().getStringExtra("choice");

        // based on choice take user through create or login process
        if (choice.equals("create")) {
            create();
        } else if (choice.equals("login")){
            login();
        } else {
            // TODO: Reset all user preferences
            Toast.makeText(LoginActivity.this, "Fatal Error Occurred: Resetting Application", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    // Login process
    private void login(){
        mLoginDisplay.setText(R.string.login);

        mAdminDisplay.setText("");
        mAdminKeyField.setVisibility(View.INVISIBLE);

        mButton = binding.loginLoginButton;
        mButton.setVisibility(View.VISIBLE);

        // Click login button
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserInfo(choice); // also updates shared preferences

                if(checkForUserInDatabase()){
                    if(!validatePassword()){
                        Toast.makeText(LoginActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(LoginActivity.this, LandingPage.class);
                        intent.putExtra(USER_ID_KEY, mUser.getUserId());
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "No user with that username found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Create account process
    private void create(){
        mLoginDisplay.setText(R.string.createAccount);

        mButton = findViewById(R.id.loginCreateAccountButton);
        mButton.setVisibility(View.VISIBLE);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserInfo(choice);

                // make sure no required fields are empty
                if(checkForEmptyFields()){
                    return;
                }

                mUser = null;

                // make sure username isn't already taken by checking if a user exists under said username already
                if(checkForUserInDatabase()){
                    // TODO: [POSSIBLE USE CASE] If dupe username is found, they may have a account already. Ask user if they would like to login instead?
                    // TODO: create possible alert builder to ask if they'd like to login instead
                    Toast.makeText(LoginActivity.this, "Account with this username already exists", Toast.LENGTH_SHORT).show();
                } else {
                    createNewUser(); // also adds user to database

                    //Send user to login page
                    Toast.makeText(LoginActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                    choice = "login";
                    intent.putExtra("choice", choice);
                    startActivity(intent);
                }
            }
        });
    }

    // Checks if user is an admin based on adminKey value
    private boolean checkAdmin(String adminKey){
        return adminKey.equals("123");
    }

    // Check user left no empty fields
    private boolean checkForEmptyFields(){
        if(mUsername.equals("")){
            Toast.makeText(LoginActivity.this, "Please provide a username", Toast.LENGTH_SHORT).show();
            return true;
        }

        if(mPassword.equals("")){
            Toast.makeText(LoginActivity.this, "Please provide a password", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }

    // Checks if user is found in database
    private boolean checkForUserInDatabase(){
        mUser = mFunkoPopShopDAO.getUserByUsername(mUsername);
        return mUser != null;
    }

    // Create a new user
    private void createNewUser(){
        mUser = new User(mUsername, mPassword, mIsAdmin, "");
        updateUserPreferences(mUser);
        addUserToDB();
    }

    // Add a user to database
    private void addUserToDB(){
        mFunkoPopShopDAO.insert(mUser);
    }

    // Validate password matches the password in the database
    private boolean validatePassword(){
        return mUser.getPassword().equals(mPassword);
    }

    // Pull all values from login/create page to update user's information
    private void getUserInfo(String choice){
        if(choice.equals("create")){
            mUsername = mUsernameField.getText().toString().trim();
            mPassword = mPasswordField.getText().toString().trim();
            mAdminKey = mAdminKeyField.getText().toString().trim();
            mIsAdmin = checkAdmin(mAdminKey);
        } else if(choice.equals("login")){
            mUsername = mUsernameField.getText().toString().trim();
            mPassword = mPasswordField.getText().toString().trim();

            mUser = mFunkoPopShopDAO.getUserByUsername(mUsername);
            mIsAdmin = mUser.isAdmin();
            updateUserPreferences(mUser);
        }
    }

    // Once logged in successfully, updated user shared preferences
    // NOTE: Might not need to pass a user object since mUser should be populated
    private void updateUserPreferences(User currUser){
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(USER_ID_KEY, currUser.getUserId());
        editor.putString(USERNAME_KEY, currUser.getUsername());
        editor.putString(PASSWORD_KEY, currUser.getPassword());
        editor.putBoolean(IS_ADMIN_KEY, currUser.isAdmin());
        editor.putBoolean("loggedIn", true);
        editor.apply();
    }

    private void getPrefs() {
        mPreferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    // Retrieve database
    private void getDataBase(){
        mFunkoPopShopDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .FunkoPopShopDAO();
    }
}

