package com.dannydevs.funkopopshop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.dannydevs.funkopopshop.DB.AppDataBase;
import com.dannydevs.funkopopshop.DB.FunkoPopShopDAO;
import com.dannydevs.funkopopshop.RECYLCER_VIEW.CustomAdapter;
import com.dannydevs.funkopopshop.RECYLCER_VIEW.MyModel;
import com.dannydevs.funkopopshop.databinding.ActivityAllUsersOperationsBinding;

import java.util.ArrayList;
import java.util.List;

public class AllUsersOperations extends AppCompatActivity {
    private static final String PREFERENCES_KEY = "com.dannydevs.funkopopshop.PREFERENCES_KEY";
    private static final String USER_ID_KEY = "com.dannydevs.funkopopshop.userIdKey";

    ActivityAllUsersOperationsBinding binding;

    EditText mUserIdToDeleteField;

    Button mDeleteUserButton;
    Button mBackButton;

    FunkoPopShopDAO mFunkoPopShopDAO;

    List<User> mAllUsers;
    User mUser = null;
    User mLoggedInUser = null;
    int mUserId;

    RecyclerView mRecyclerView;
    List<MyModel> mMyModelList;
    CustomAdapter mCustomerAdapter;

    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users_operations);

        binding = ActivityAllUsersOperationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        wireUpDisplay();

        getDataBase();

        getPrefs();

        getCurrentUser();

        displayAllUsers();

        mDeleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserIdFromDisplay();
                if(checkValidUserById()){
                    if(deleteUserFromDataBase()) {
                        Toast.makeText(AllUsersOperations.this, "User Deleted Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AllUsersOperations.this, "You aren't allowed to delete logged in user", Toast.LENGTH_SHORT).show();
                    }

                    displayAllUsers();
                } else {
                    Toast.makeText(AllUsersOperations.this, "No user found with entered ID", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdminDashboard.class);
                startActivity(intent);
            }
        });
    }

    private void wireUpDisplay(){
        mUserIdToDeleteField = binding.allUsersOpsUserIdField;
        mDeleteUserButton = binding.allUsersOpsDeleteButton;
        mBackButton = binding.allUsersOpsBackButton;
        mRecyclerView = binding.allUsersOpsRecyclerView;
    }

    private void getDataBase(){
        mFunkoPopShopDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .FunkoPopShopDAO();
    }

    private void getPrefs() {
        mSharedPreferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    private void getCurrentUser(){
        int mLoggedInUserId = mSharedPreferences.getInt(USER_ID_KEY, -1);
        mLoggedInUser = mFunkoPopShopDAO.getUserByUserId(mLoggedInUserId);
    }


    private void displayAllUsers(){
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        mMyModelList = new ArrayList<>();

        // grab users
        mAllUsers = mFunkoPopShopDAO.getAllUsers();

        // display users
        for(int i = 0; i < mAllUsers.size(); i++){
            User currUser = mAllUsers.get(i);

            String isAdmin = "";
            if (currUser.isAdmin()) {
                isAdmin = "Yes";
            }

            mMyModelList.add(new MyModel(currUser.getUserId(), currUser.getUsername(), isAdmin));
        }

        mCustomerAdapter = new CustomAdapter(getApplicationContext(), mMyModelList);
        mRecyclerView.setAdapter(mCustomerAdapter);
    }

    private void getUserIdFromDisplay(){
        String stringUserId = mUserIdToDeleteField.getText().toString();
        if(stringUserId.equals("")){
            mUserId = -1;
        } else {
            mUserId = Integer.parseInt(stringUserId);
        }
    }

    private boolean checkValidUserById(){
        mUser = mFunkoPopShopDAO.getUserByUserId(mUserId);
        return mUser != null;
    }

    private boolean deleteUserFromDataBase(){
        if(mLoggedInUser.getUserId() != mUserId){
            mFunkoPopShopDAO.deleteUserById(mUserId);
            return true;
        } else {
            return false;
        }
    }
}
