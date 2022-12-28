package com.dannydevs.funkopopshop.ShoppingCartRecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dannydevs.funkopopshop.R;

public class ShoppingCartViewHolder extends RecyclerView.ViewHolder {
    public ImageView itemPictureUrl;
    public TextView textItemName, textItemPrice;

    public ShoppingCartViewHolder(@NonNull View itemView) {
        super(itemView);
        itemPictureUrl = itemView.findViewById(R.id.itemPicture);
        textItemName = itemView.findViewById(R.id.textItemName);
        textItemPrice = itemView.findViewById(R.id.textItemPrice);
    }
}
