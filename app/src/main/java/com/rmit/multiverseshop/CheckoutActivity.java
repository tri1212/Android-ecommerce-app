package com.rmit.multiverseshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rmit.multiverseshop.model.CartItem;
import com.rmit.multiverseshop.model.Order;
import com.rmit.multiverseshop.model.OrderList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseFirestore db;

    ImageButton backButton;

    TextInputLayout nameText;
    TextInputLayout addressText;
    TextInputLayout phoneText;

    MaterialButton orderButton;

    ArrayList<CartItem> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> finish());

        nameText = findViewById(R.id.name);
        addressText = findViewById(R.id.address);
        phoneText = findViewById(R.id.phone);

        orderButton = findViewById(R.id.order_button);
        orderButton.setOnClickListener(view -> checkout());

        cartItems = (ArrayList<CartItem>) getIntent().getSerializableExtra("items");
        if (cartItems == null || cartItems.isEmpty()) finish();
    }

    private void checkout() {
        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "Error, user is not logged in",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate input details
        String name = String.valueOf(nameText.getEditText().getText());
        String address = String.valueOf(addressText.getEditText().getText());
        String phone = String.valueOf(phoneText.getEditText().getText());

        if (name.isEmpty()) {
            nameText.setError("Enter your name");
            return;
        }
        nameText.setError(null);

        if (address.isEmpty()) {
            addressText.setError("Enter delivery address");
            return;
        }
        addressText.setError(null);

        if (phone.isEmpty()) {
            phoneText.setError("Enter your phone number");
            return;
        }
        phoneText.setError(null);

        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        Order order = new Order(name, address, phone, new Date(), total, cartItems);

        DocumentReference docRef = db.collection("userOrders")
                .document(auth.getCurrentUser().getUid());

        // Add new order
        docRef.get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
            if (document != null && document.exists()) {
                try {
                    List<Order> orders = document.toObject(OrderList.class).getOrders();
                    if (orders == null) return;
                    orders.add(order);
                    document.getReference().update("orders", orders)
                            .addOnCompleteListener(task1 -> {
                                Toast.makeText(this, task.isSuccessful() 
                                                ? "Order placed" 
                                                : "Failed to place order", Toast.LENGTH_SHORT).show();
                            });
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            } else {
                docRef.set(Collections.singletonMap("orders", Collections.singletonList(order)))
                        .addOnCompleteListener(task1 -> {
                            Toast.makeText(this, task.isSuccessful()
                                    ? "Order placed"
                                    : "Failed to place order", Toast.LENGTH_SHORT).show();
                        });
            }

            // Increase product sold amount
            for (CartItem item : cartItems) {
                db.collection("products")
                        .document(item.getProduct().getId())
                        .update("productsSold", FieldValue.increment(item.getQuantity()))
                        .addOnCompleteListener(task1 -> {
                            if (task.isCanceled())
                                Toast.makeText(this, "Failed to update products",
                                        Toast.LENGTH_SHORT).show();
                        });
            }

            // Clear cart
            db.collection("userCarts")
                    .document(auth.getCurrentUser().getUid())
                    .set(Collections.singletonMap("items", Collections.emptyList()))
                    .addOnCompleteListener(task1 -> {
                        if (task.isCanceled())
                            Toast.makeText(this, "Failed to clear cart",
                                    Toast.LENGTH_SHORT).show();
                    });;
        });

        finishAffinity();
        startActivity(new Intent(this, MainActivity.class));
    }
}
