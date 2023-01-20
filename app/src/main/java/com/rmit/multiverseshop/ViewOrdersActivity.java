package com.rmit.multiverseshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rmit.multiverseshop.adapters.OrderViewAdapter;
import com.rmit.multiverseshop.model.CartItem;
import com.rmit.multiverseshop.model.Order;
import com.rmit.multiverseshop.model.OrderList;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ViewOrdersActivity extends AppCompatActivity
        implements OrderViewAdapter.DataChangedEventHandler {

    FirebaseFirestore db;
    FirebaseAuth auth;

    TextView noItemsText;

    OrderViewAdapter adapter;
    RecyclerView recyclerView;

    ImageButton backButton;

    List<Order> orders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        noItemsText = findViewById(R.id.no_items_text);

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> finish());

        adapter = new OrderViewAdapter(orders, this, this);
        recyclerView = findViewById(R.id.order_recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadOrders();
    }

    private void loadOrders() {
        DocumentReference docRef = db.collection("userOrders")
                .document(auth.getCurrentUser().getUid());

        docRef.get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
            if (document != null && document.exists()) {
                orders = document.toObject(OrderList.class).getOrders();
                adapter.setOrders(orders);
                updateUI();
            }
        });
    }

    @Override
    public void saveOrders() {
        db.collection("userOrders")
                .document(auth.getCurrentUser().getUid())
                .update("orders", orders).addOnCompleteListener(task -> {
            if (task.isCanceled()) {
                Toast.makeText(this, "Failed to update orders", Toast.LENGTH_SHORT).show();
            }
            updateUI();
        });
    }

    @Override
    public void viewDetails(int index) {
        Intent intent = new Intent(this, OrderDetailsActivity.class);
        intent.putExtra("order_items", (ArrayList<CartItem>) orders.get(index).getOrderItems());
        startActivity(intent);
    }

    public void updateUI() {
        if (orders.size() == 0) {
            noItemsText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            return;
        } else {
            noItemsText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
