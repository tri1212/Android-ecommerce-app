package com.rmit.multiverseshop.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rmit.multiverseshop.R;
import com.rmit.multiverseshop.model.CartItem;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartViewAdapter extends RecyclerView.Adapter<CartViewAdapter.CartViewHolder> {

    private List<CartItem> cartItems;

    Context context;
    DataChangedEventHandler handler;

    public CartViewAdapter(List<CartItem> cartItems, Context context, DataChangedEventHandler handler) {
        this.cartItems = cartItems;
        this.context = context;
        this.handler = handler;
    }

    public interface DataChangedEventHandler {
        void updateUI();
        void saveCart();
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_view, parent, false);
        return new CartViewAdapter.CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(holder.getAdapterPosition());

        Picasso.get().load(cartItem.getProduct().getImageUrl()).placeholder(R.drawable.placeholder).into(holder.itemImage);

        String category = cartItem.getProduct().getCategory().substring(0, 1).toUpperCase() +
                cartItem.getProduct().getCategory().substring(1).toLowerCase();

        String total = String.format(Locale.US, "$%.2f",
                cartItem.getProduct().getPrice() * cartItem.getQuantity());

        holder.itemName.setText(cartItem.getProduct().getName());
        holder.itemCategory.setText(category);
        holder.itemTotal.setText(total);
        holder.itemQuantity.setText(String.valueOf(cartItem.getQuantity()));
        holder.removeButton.setOnClickListener(view -> {
            new AlertDialog.Builder(context)
                    .setTitle("Confirm remove")
                    .setMessage("Do you want to remove this item?")
                    .setPositiveButton("Yes", (dialog, i) -> {
                        cartItems.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        handler.saveCart();
                        handler.updateUI();
                        Toast.makeText(context, "Removed successfully", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", (dialog, i) -> dialog.cancel())
                    .create()
                    .show();
        });
        holder.decreaseButton.setOnClickListener(view -> {
            if (cartItem.getQuantity() > 1) {
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                notifyDataSetChanged();
                handler.saveCart();
                handler.updateUI();
            }
        });
        holder.increaseButton.setOnClickListener(view -> {
            if (cartItem.getQuantity() < 98) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                notifyDataSetChanged();
                handler.saveCart();
                handler.updateUI();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }
}
