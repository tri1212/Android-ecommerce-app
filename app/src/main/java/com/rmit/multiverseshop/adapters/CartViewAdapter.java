package com.rmit.multiverseshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.rmit.multiverseshop.R;
import com.rmit.multiverseshop.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartViewAdapter extends RecyclerView.Adapter<CartViewAdapter.CartViewHolder> {
    private List<Product> productList;
    Context context;

    public CartViewAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemName;
        TextView itemCategory;
        TextView itemTotal;
        TextView itemQuantity;
        ImageButton removeButton;
        ImageButton increaseButton;
        ImageButton decreaseButton;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById((R.id.item_image));
            itemName = itemView.findViewById(R.id.item_name);
            itemCategory = itemView.findViewById(R.id.item_category);
            itemTotal = itemView.findViewById(R.id.item_total);
            itemQuantity = itemView.findViewById(R.id.item_quantity);
            removeButton = itemView.findViewById(R.id.remove_item_button);
            increaseButton = itemView.findViewById(R.id.item_increase_button);
            decreaseButton = itemView.findViewById(R.id.item_decrease_button);
        }
    }

    @NonNull
    @Override
    public CartViewAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card_view, parent, false);
        return new CartViewAdapter.CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = productList.get(position);

        Picasso.get().load(product.getImageUrl()).placeholder(R.drawable.placeholder).into(holder.itemImage);

        holder.itemName.setText(product.getName());
        holder.itemCategory.setText(product.getCategory());
        holder.itemTotal.setText("0.0");
        holder.itemQuantity.setText("0");
        holder.removeButton.setOnClickListener(view -> {});
        holder.decreaseButton.setOnClickListener(view -> {});
        holder.increaseButton.setOnClickListener(view -> {});
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
