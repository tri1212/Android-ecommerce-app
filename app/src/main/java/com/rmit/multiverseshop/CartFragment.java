package com.rmit.multiverseshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rmit.multiverseshop.adapters.CartViewAdapter;
import com.rmit.multiverseshop.model.CartItem;
import com.rmit.multiverseshop.model.CartItemList;
import com.rmit.multiverseshop.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CartFragment extends Fragment implements CartViewAdapter.DataChangedEventHandler {

    View view;

    FirebaseAuth auth;
    FirebaseFirestore db;

    CartViewAdapter adapter;

    RecyclerView recyclerView;

    MaterialButton checkoutButton;

    TextView noItemsText;
    TextView itemsTotalText;
    TextView priceTotalText;

    LinearLayout cartDetails;

    List<CartItem> cartItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = view.findViewById(R.id.order_recycler_view);
        checkoutButton = view.findViewById(R.id.checkout_button);

        noItemsText = view.findViewById(R.id.no_items_text);
        itemsTotalText = view.findViewById(R.id.items_total_text);
        priceTotalText = view.findViewById(R.id.price_total_text);

        cartDetails = view.findViewById(R.id.cart_details);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        cartItems = new ArrayList<>();

        adapter = new CartViewAdapter(cartItems, getContext(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        checkoutButton.setOnClickListener(view1 -> {
            if (cartItems.size() == 0) return;
            Intent checkoutIntent = new Intent(getActivity(), CheckoutActivity.class);
            checkoutIntent.putExtra("items", (ArrayList<CartItem>) cartItems);
            startActivity(checkoutIntent);
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getCart();
    }

    // Get current user's cart
    private void getCart() {
        if (auth.getCurrentUser() == null) {
            Toast.makeText(view.getContext(), "Error, user is not signed in",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        DocumentReference docRef = db.collection("userCarts")
                .document(auth.getCurrentUser().getUid());

        docRef.get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
            if (document != null && document.exists()) {
                List<HashMap<String, Object>> items;
                try {
                    items = document.toObject(CartItemList.class).getItems();
                    cartItems.clear();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return;
                }
                for (HashMap<String, Object> item : items) {
                    db.collection("products")
                            .document(item.get("id").toString())
                            .get()
                            .addOnCompleteListener(task1 -> {
                                DocumentSnapshot document1 = task1.getResult();
                                if (document1 != null && document1.exists()) {
                                    try {
                                        Product product = document1.toObject(Product.class);
                                        cartItems.add(new CartItem(product,
                                                Integer.parseInt(item.get("quantity").toString())));
                                        adapter.notifyDataSetChanged();
                                        updateUI();
                                    } catch (RuntimeException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                }
            }
            updateUI();
        });
    }

    @Override
    public void updateUI() {
        if (cartItems.size() == 0) {
            noItemsText.setVisibility(View.VISIBLE);
            cartDetails.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            return;
        } else {
            noItemsText.setVisibility(View.GONE);
            cartDetails.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        double totalPrice = 0;
        int amount = 0;

        for (CartItem item : cartItems) {
            totalPrice += item.getProduct().getPrice() * item.getQuantity();
            amount += item.getQuantity();
        }

        String priceTotal = String.format(Locale.US, "$%.2f", totalPrice);

        priceTotalText.setText(priceTotal);
        itemsTotalText.setText(String.valueOf(amount));
    }

    @Override
    public void saveCart() {
        if (auth.getCurrentUser() == null) {
            Toast.makeText(getContext(), "Error, user is not logged in",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        List<HashMap<String, Object>> items = new ArrayList<>();
        for (CartItem item : cartItems) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", item.getProduct().getId());
            map.put("quantity", item.getQuantity());
            items.add(map);
        }
        db.collection("userCarts")
                .document(auth.getCurrentUser().getUid())
                .update("items", items)
                .addOnCompleteListener(task -> {
                    if (task.isCanceled()) {
                        Toast.makeText(getContext(), "Failed to update cart",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
