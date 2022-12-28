package com.dannydevs.funkopopshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dannydevs.funkopopshop.databinding.ActivityAdminDashboardBinding;

public class AdminDashboard extends AppCompatActivity {
    static final String ITEM_OPERATION = "com.dannydevs.funkopopshop.ITEM_OPERATION";

    TextView mAdminDashDisplay;

    Button mViewUsersButton;
    Button mAddItemButton;
    Button mModifyItemButton;
    Button mDeleteItemButton;
    Button mBackButton;

    ActivityAdminDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        binding = ActivityAdminDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Wire up display bindings
        mAdminDashDisplay = binding.adminDashDisplay;

        mViewUsersButton = binding.adminDashViewUsers;
        mAddItemButton = binding.adminDashAddItem;
        mModifyItemButton = binding.adminDashModifyItem;
        mDeleteItemButton = binding.adminDashDeleteItem;
        mBackButton = binding.adminDashBackButton;

        // View users button is clicked
        mViewUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AllUsersOperations.class);
                startActivity(intent);
            }
        });

        // Add item button is clicked
        mAddItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDashboard.this, ItemOperations.class);
                intent.putExtra(ITEM_OPERATION, "Add");
                startActivity(intent);
            }
        });

        // Modify item button is clicked
        mModifyItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ItemOperations.class);
                intent.putExtra(ITEM_OPERATION, "Modify");
                startActivity(intent);
            }
        });

        // Delete item button is clicked
        mDeleteItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ItemOperations.class);
                intent.putExtra(ITEM_OPERATION, "Delete");
                startActivity(intent);
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LandingPage.class);
                startActivity(intent);
            }
        });
    }
}
