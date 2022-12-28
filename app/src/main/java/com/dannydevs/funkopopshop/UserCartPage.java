package com.dannydevs.funkopopshop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.dannydevs.funkopopshop.DB.AppDataBase;
import com.dannydevs.funkopopshop.DB.FunkoPopShopDAO;
import com.dannydevs.funkopopshop.RECYLCER_VIEW.CustomAdapter;
import com.dannydevs.funkopopshop.RECYLCER_VIEW.MyModel;
import com.dannydevs.funkopopshop.ShoppingCartRecyclerView.ShoppingCartAdapter;
import com.dannydevs.funkopopshop.ShoppingCartRecyclerView.ShoppingCartModel;
import com.dannydevs.funkopopshop.databinding.ActivityCartBinding;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.DoubleToLongFunction;

public class UserCartPage extends AppCompatActivity {
    private static final String PREFERENCES_KEY = "com.dannydevs.funkopopshop.PREFERENCES_KEY";
    private static final String USER_ID_KEY = "com.dannydevs.funkopopshop.userIdKey";
    private static final String CART_KEY = "com.dannydevs.funkopopshop.cartKey";

    ActivityCartBinding binding;

    TextView mUserPriceTotalField;

    Button mBackButton;
    Button mGoToPaymentButton;

    RecyclerView mRecyclerView;
    List<ShoppingCartModel> mShoppingCartModelList;
    ShoppingCartAdapter mShoppingCartAdapter;

    FunkoPopShopDAO mFunkoPopShopDAO;

    SharedPreferences mSharedPreferences;

    User mUser;

    double mPriceTotal = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getDataBase();

        getPrefs();

        wireUpDisplay();

        getCurrentUser();

        displayCartItems();

        displayTotal(mPriceTotal);

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LandingPage.class);
                startActivity(intent);
            }
        });

        mGoToPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PaymentPage.class);
                startActivity(intent);
            }
        });

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

    private void wireUpDisplay(){
        mUserPriceTotalField = binding.userCartPagePriceTotal;
        mGoToPaymentButton = binding.userCartPageGoToPaymentButton;
        mBackButton = binding.userCartPageBackButton;
    }

    private void getCurrentUser(){
        int userId = mSharedPreferences.getInt(USER_ID_KEY, -1);
        mUser = mFunkoPopShopDAO.getUserByUserId(userId);
    }


    private void displayCartItems(){
        mRecyclerView = findViewById(R.id.userCartRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));

        mShoppingCartModelList = new ArrayList<>();

        // grab items

//        ArrayList<String> cartItemIds = new ArrayList<>(Arrays.asList(mUser.getCartStringList().split(",")));
//
//        Toast.makeText(this, cartItemIds.toString(), Toast.LENGTH_SHORT).show();
        ArrayList<String> cartItemIds = new ArrayList<>();
        cartItemIds.add("1");
        cartItemIds.add("2");

        for(int i = 0; i < cartItemIds.size(); i++){
            String currItemIdString = cartItemIds.get(i);
            int currItemId = Integer.parseInt(currItemIdString);

            Item currItem = mFunkoPopShopDAO.getItemById(currItemId);

            updateTotal(currItem.getPrice());

            mShoppingCartModelList.add(new ShoppingCartModel(currItem.getPicture(), currItem.getName(), currItem.getPrice()));
        }

        mShoppingCartAdapter = new ShoppingCartAdapter(getApplicationContext(), mShoppingCartModelList);
        mRecyclerView.setAdapter(mShoppingCartAdapter);
    }

    private void updateTotal(String currItemPriceString){
        double currItemPrice = Double.parseDouble(currItemPriceString);
        mPriceTotal += currItemPrice;
        displayTotal(mPriceTotal);
    }

    private void displayTotal(double total){
        String stringTotal = String.valueOf(total);
        stringTotal += "0";
        mUserPriceTotalField.setText(getString(R.string.userCartPageItemPriceTotalText, stringTotal));
    }
}
