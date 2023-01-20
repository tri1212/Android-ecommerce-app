package com.rmit.multiverseshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rmit.multiverseshop.R;
import com.rmit.multiverseshop.model.CartItem;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderDetailsViewAdapter extends
        RecyclerView.Adapter<OrderDetailsViewAdapter.OrderDetailsViewHolder> {

    private List<CartItem> orderItems;

    Context context;

    public OrderDetailsViewAdapter(List<CartItem> orderItems, Context context) {
        this.orderItems = orderItems;
        this.context = context;
    }

    public class OrderDetailsViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView itemName;
        TextView itemCategory;
        TextView itemQuantity;
        TextView itemPrice;

        public OrderDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.item_image);
            itemName = itemView.findViewById(R.id.item_name);
            itemCategory = itemView.findViewById(R.id.item_category);
            itemQuantity = itemView.findViewById(R.id.item_quantity);
            itemPrice = itemView.findViewById(R.id.item_price);
        }
    }

    @NonNull
    @Override
    public OrderDetailsViewAdapter.OrderDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_details_view, parent, false);
        return new OrderDetailsViewAdapter.OrderDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailsViewHolder holder, int position) {
        CartItem orderItem = orderItems.get(holder.getAdapterPosition());

        Picasso.get().load(orderItem.getProduct().getImageUrl())
                .resize(0, 90)
                .placeholder(R.drawable.placeholder)
                .into(holder.image);

        String category = orderItem.getProduct().getCategory().substring(0, 1).toUpperCase() +
                orderItem.getProduct().getCategory().substring(1).toLowerCase();
        String quantity = String.format(Locale.US, "x%d", orderItem.getQuantity());
        String price = String.format(Locale.US, "$%.2f", orderItem.getProduct().getPrice());

        holder.itemName.setText(orderItem.getProduct().getName());
        holder.itemCategory.setText(category);
        holder.itemQuantity.setText(quantity);
        holder.itemPrice.setText(price);
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }
}
