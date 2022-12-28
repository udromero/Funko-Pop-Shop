package com.dannydevs.funkopopshop;

import android.app.admin.SystemUpdateInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.dannydevs.funkopopshop.DB.AppDataBase;
import com.dannydevs.funkopopshop.DB.FunkoPopShopDAO;
import com.dannydevs.funkopopshop.databinding.ActivityItemOperationsBinding;
import com.dannydevs.funkopopshop.databinding.ActivityMainBinding;

public class ItemOperations extends AppCompatActivity {
    static final String ITEM_OPERATION = "com.dannydevs.funkopopshop.ITEM_OPERATION";

    FunkoPopShopDAO mFunkoPopShopDAO;

    TextView mItemOperationsDisplay;

    EditText mItemIdField;
    EditText mItemPictureLinkField;
    EditText mItemNameField;
    EditText mItemPriceField;
    EditText mItemDescriptionField;

    Button mItemOpSaveButton;
    Button mItemOpContinueButton;
    Button mItemOpBackButton;

    private int mItemId;
    private String mItemPictureLink;
    private String mItemName;
    private String mItemPrice;
    private String mItemDescription;

    private Item mItem = null;

    ActivityItemOperationsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_operations);

        binding = ActivityItemOperationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getDataBase();

        // Wire Up Display
        mItemOperationsDisplay = binding.itemOpDisplay;

        mItemIdField = binding.itemOpItemId;
        mItemPictureLinkField = binding.itemOpPictureLink;
        mItemNameField = binding.itemOpName;
        mItemPriceField = binding.itemOpPrice;
        mItemDescriptionField = binding.itemOpDescriptionBox;

        mItemOpSaveButton = binding.itemOpSaveButton;
        mItemOpContinueButton = binding.itemOpContinueButton;
        mItemOpBackButton = binding.itemOpBackButton;

        String itemOperation = getIntent().getStringExtra(ITEM_OPERATION);

        if(itemOperation.equals("Modify")){
            mItemOperationsDisplay.setText(getString(R.string.itemOpItemId));
            mItemPictureLinkField.setVisibility(View.INVISIBLE);
            mItemNameField.setVisibility(View.INVISIBLE);
            mItemPriceField.setVisibility(View.INVISIBLE);
            mItemDescriptionField.setVisibility(View.INVISIBLE);
            mItemOpSaveButton.setVisibility(View.INVISIBLE);
        } else if (itemOperation.equals("Delete")){
            mItemOperationsDisplay.setText(getString(R.string.itemOpItemId));
            mItemPictureLinkField.setVisibility(View.INVISIBLE);
            mItemNameField.setVisibility(View.INVISIBLE);
            mItemPriceField.setVisibility(View.INVISIBLE);
            mItemDescriptionField.setVisibility(View.INVISIBLE);
            mItemOpContinueButton.setText(R.string.adminDashDeleteItem);
            mItemOpSaveButton.setVisibility(View.INVISIBLE);
        } else {
            mItemOperationsDisplay.setText(getString(R.string.itemOpDisplay, itemOperation));
            mItemIdField.setVisibility(View.INVISIBLE);
            mItemOpContinueButton.setVisibility(View.INVISIBLE);
            mItemOpSaveButton.setVisibility(View.VISIBLE);
        }

        mItemOpContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemOperation.equals("Delete")){
                    String itemIdString = mItemIdField.getText().toString().trim();
                    mItemId = Integer.parseInt(itemIdString);

                    if(itemExists(true, mItemId)){
                        deleteItemFromDB(mItemId); // using id
                        Toast.makeText(ItemOperations.this, "Item Deleted Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), LandingPage.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ItemOperations.this, "Item with that ID wasn't found", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(getApplicationContext(), AdminDashboard.class);
//                        startActivity(intent);
                    }
                } else if(itemOperation.equals("Modify")){
                    mItemIdField.setVisibility(View.INVISIBLE);
                    mItemOpContinueButton.setVisibility(View.INVISIBLE);

                    mItemPictureLinkField.setVisibility(View.VISIBLE);
                    mItemNameField.setVisibility(View.VISIBLE);
                    mItemPriceField.setVisibility(View.VISIBLE);
                    mItemDescriptionField.setVisibility(View.VISIBLE);
                    mItemOpSaveButton.setVisibility(View.VISIBLE);

                    mItemOperationsDisplay.setText(getString(R.string.itemOpDisplay, itemOperation));

                    String itemIdString = mItemIdField.getText().toString().trim();
                    mItemId = Integer.parseInt(itemIdString);

                    if(itemExists(true, mItemId)){
                        mItemPictureLinkField.setText(mItem.getPicture());
                        mItemNameField.setText(mItem.getName());
                        mItemPriceField.setText(mItem.getPrice());
                        mItemDescriptionField.setText(mItem.getDescription());
                    } else {
                        Toast.makeText(ItemOperations.this, "Item with that ID wasn't found", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), AdminDashboard.class);
                        startActivity(intent);
                    }
                }
            }
        });

        mItemOpSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (itemOperation) {
                    case "Add":
                        getItemInformationFromDisplay(mItem);
                        if(!areEmptyFields()) {
                            if (!itemExists(false, -1)) {
                                createItem();
                                Toast.makeText(ItemOperations.this, "Item Added successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), LandingPage.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(ItemOperations.this, "Item already exists", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ItemOperations.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "Modify":
                        if(!getItemInformationFromDisplay(mItem)){
                            setNewItemInformation();
                            Toast.makeText(ItemOperations.this, "Item Modified Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), LandingPage.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(ItemOperations.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    default:
                        // Send admin user back to admin dashboard
                        Toast.makeText(ItemOperations.this, "Operation Choice Error: Kicking back to admin dashboard", Toast.LENGTH_SHORT).show();
                        Intent defaultIntent = new Intent(getApplicationContext(), AdminDashboard.class);
                        startActivity(defaultIntent);
                }
            }
        });

        mItemOpBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemOperations.this, AdminDashboard.class);
                startActivity(intent);
            }
        });
    }
    // Supporting Functions
    private boolean getItemInformationFromDisplay(Item item){
        mItemPictureLink = mItemPictureLinkField.getText().toString().trim();
        mItemName = mItemNameField.getText().toString().trim();
        mItemPrice = mItemPriceField.getText().toString().trim();
        mItemDescription = mItemDescriptionField.getText().toString().trim();

        return mItemPictureLink.equals("") || mItemName.equals("") || mItemPrice.equals("") || mItemDescription.equals("");
    }

    private boolean areEmptyFields(){
        return mItemPictureLink.equals("") || mItemName.equals("") || mItemPrice.equals("") || mItemDescription.equals("");
    }

    private void setNewItemInformation(){
        mItem.setPicture(mItemPictureLink);
        mItem.setName(mItemName);
        mItem.setPrice(mItemPrice);
        mItem.setDescription(mItemDescription);
        updateItemInDataBase();
    }

    private void updateItemInDataBase(){
        mFunkoPopShopDAO.updateItemInfo(
                mItem.getPicture(), mItem.getName(), mItem.getPrice(), mItem.getDescription(), mItem.getItemId());
    }

    private boolean itemExists(boolean useId, int itemId){
        if(useId && itemId != -1){
            mItem = mFunkoPopShopDAO.getItemById(itemId);
        } else {
            mItem = mFunkoPopShopDAO.getItemByPictureUrl(mItemPictureLink);
        }

        return mItem != null;
    }

    // Delete item from DB based on item's id value
    private void deleteItemFromDB(int itemId){
        mFunkoPopShopDAO.deleteItemById(itemId);
    }

    // Add item to DB functions
    private void createItem(){
        mItem = new Item(mItemName, mItemPrice, mItemPictureLink, mItemDescription);
        addItemToDB();
    }

    private void addItemToDB(){
        mFunkoPopShopDAO.insert(mItem);
    }

    // Get DB
    private void getDataBase(){
        mFunkoPopShopDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .FunkoPopShopDAO();
    }
}
