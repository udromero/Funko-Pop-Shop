package com.dannydevs.funkopopshop.RECYLCER_VIEW;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dannydevs.funkopopshop.R;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomViewHolder> {

    private Context context;
    private List<MyModel> list;

    public CustomAdapter(Context context, List<MyModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.user_text_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.textUserID.setText(String.valueOf(list.get(position).getUserId()));
        holder.textUsername.setText(list.get(position).getName());
        holder.textUserIsAdmin.setText(list.get(position).getIsAdmin());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
