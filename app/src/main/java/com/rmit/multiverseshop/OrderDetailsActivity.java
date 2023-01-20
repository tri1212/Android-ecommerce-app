package com.rmit.multiverseshop;

import android.os.Bundle;
import android.widget.ImageButton;

import com.rmit.multiverseshop.adapters.OrderDetailsViewAdapter;
import com.rmit.multiverseshop.model.CartItem;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrderDetailsActivity extends AppCompatActivity {

    OrderDetailsViewAdapter adapter;
    RecyclerView recyclerView;

    ImageButton backButton;

    List<CartItem> orderItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> finish());

        orderItems = (ArrayList<CartItem>) getIntent().getSerializableExtra("order_items");
        if (orderItems == null || orderItems.isEmpty()) finish();

        adapter = new OrderDetailsViewAdapter(orderItems, this);
        recyclerView = findViewById(R.id.order_details_recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
