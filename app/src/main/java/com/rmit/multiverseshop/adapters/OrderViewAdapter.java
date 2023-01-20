package com.rmit.multiverseshop.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.rmit.multiverseshop.R;
import com.rmit.multiverseshop.model.Order;

import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static java.text.DateFormat.getDateInstance;

public class OrderViewAdapter extends RecyclerView.Adapter<OrderViewAdapter.OrderViewHolder> {

    private List<Order> orders;

    Context context;
    OrderViewAdapter.DataChangedEventHandler handler;

    public OrderViewAdapter(List<Order> orders, Context context,
                            OrderViewAdapter.DataChangedEventHandler handler) {
        this.orders = orders;
        this.context = context;
        this.handler = handler;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    public interface DataChangedEventHandler {
        void saveOrders();
        void viewDetails(int index);
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderDate;
        TextView orderName;
        TextView orderAddress;
        TextView orderPhone;
        TextView orderTotal;
        MaterialButton viewDetailsButton;
        ImageButton removeOrderButton;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderDate = itemView.findViewById(R.id.order_date);
            orderName = itemView.findViewById(R.id.order_name);
            orderAddress = itemView.findViewById(R.id.order_address);
            orderPhone = itemView.findViewById(R.id.order_phone);
            orderTotal = itemView.findViewById(R.id.order_total);
            viewDetailsButton = itemView.findViewById(R.id.view_details_button);
            removeOrderButton = itemView.findViewById(R.id.remove_order_button);
        }
    }

    @NonNull
    @Override
    public OrderViewAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_view, parent, false);
        return new OrderViewAdapter.OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(holder.getAdapterPosition());

        String date = getDateInstance().format(order.getDate());
        String total = String.format(Locale.US, "$%.2f", order.getTotal());

        holder.orderDate.setText(date);
        holder.orderName.setText(order.getFullname());
        holder.orderAddress.setText(order.getAddress());
        holder.orderPhone.setText(order.getPhone());
        holder.orderTotal.setText(total);
        holder.removeOrderButton.setOnClickListener(view -> {
            new AlertDialog.Builder(context)
                    .setTitle("Confirm remove")
                    .setMessage("Do you want to remove this order?")
                    .setPositiveButton("Yes", (dialog, i) -> {
                        orders.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        handler.saveOrders();
                        Toast.makeText(context, "Removed successfully", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", (dialog, i) -> dialog.cancel())
                    .create()
                    .show();
        });
        holder.viewDetailsButton.setOnClickListener(view ->
                handler.viewDetails(holder.getAdapterPosition())
        );
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }
}
