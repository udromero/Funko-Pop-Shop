package com.dannydevs.funkopopshop.ShoppingCartRecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dannydevs.funkopopshop.R;
import com.dannydevs.funkopopshop.RECYLCER_VIEW.CustomViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartViewHolder> {

    private Context context;
    private List<ShoppingCartModel> list;

    public ShoppingCartAdapter(Context context, List<ShoppingCartModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ShoppingCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShoppingCartViewHolder(LayoutInflater.from(context).inflate(R.layout.shopping_cart_text_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingCartViewHolder holder, int position) {
        Picasso.get().load(list.get(position).getItemPictureUrl()).into(holder.itemPictureUrl);
        holder.textItemName.setText(list.get(position).getItemName());
        holder.textItemPrice.setText(list.get(position).getItemPrice());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
