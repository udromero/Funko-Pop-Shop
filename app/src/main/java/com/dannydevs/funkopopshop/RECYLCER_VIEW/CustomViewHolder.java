package com.dannydevs.funkopopshop.RECYLCER_VIEW;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dannydevs.funkopopshop.R;

public class CustomViewHolder extends RecyclerView.ViewHolder {

    public TextView textUserID, textUsername, textUserIsAdmin;

    @SuppressLint("CutPasteId")
    public CustomViewHolder(@NonNull View itemView) {
        super(itemView);
        textUserID = itemView.findViewById(R.id.textUserID);
        textUsername = itemView.findViewById(R.id.textUsername);
        textUserIsAdmin = itemView.findViewById(R.id.textUserIsAdmin);
    }
}
