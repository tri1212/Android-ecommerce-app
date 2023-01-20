package com.rmit.multiverseshop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rmit.multiverseshop.model.Product;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class AddToCartFragment extends BottomSheetDialogFragment {

    FirebaseAuth auth;
    FirebaseFirestore db;

    TextView quantityText;

    ImageButton decreaseButton;
    ImageButton increaseButton;

    MaterialButton addToCartButton;

    View view;

    Product product;

    int quantity = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_to_cart, container, false);

        quantityText = view.findViewById(R.id.quantity);

        decreaseButton = view.findViewById(R.id.decrease_button);
        increaseButton = view.findViewById(R.id.increase_button);
        addToCartButton = view.findViewById(R.id.add_to_cart_button1);

        if (this.getArguments() != null) {
            product = (Product) getArguments().getSerializable("product");
            addToCartButton.setOnClickListener(view1 -> {
                addToCart(product, quantity);
                this.dismiss();
            });
            decreaseButton.setOnClickListener(view1 -> {
                if (quantity > 1) {
                    quantity--;
                    quantityText.setText(String.valueOf(quantity));
                }
            });
            increaseButton.setOnClickListener(view1 -> {
                if (quantity < 98) {
                    quantity++;
                    quantityText.setText(String.valueOf(quantity));
                }
            });
        }
        quantityText.setText("1");

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        return view;
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }

    private void addToCart(Product product, int quantity) {
        if (auth.getCurrentUser() == null) {
            Toast.makeText(view.getContext(), "Error, user is not logged in",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, Object> item = new HashMap<>();
        item.put("id", product.getId());
        item.put("quantity", quantity);

        DocumentReference docRef = db.collection("userCarts")
                .document(auth.getCurrentUser().getUid());

        docRef.get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
            if (document != null && document.exists()) {
                List<HashMap<String, Object>> items =
                        (List<HashMap<String, Object>>) document.get("items");
                if (items == null) return;
                boolean isInCart = false;
                // Check if the item is already in the cart
                // If the item is already in the cart, update the quantity, otherwise add a new item
                for (HashMap<String, Object> item1 : items) {
                    if (product.getId().equals(item1.get("id"))) {
                        int q = Integer.parseInt(item1.get("quantity").toString()) + quantity;
                        item1.put("quantity", q);
                        isInCart = true;
                        break;
                    }
                }
                if (!isInCart) items.add(item);
                document.getReference().update("items", items)
                        .addOnCompleteListener(task1 -> {
                            Toast.makeText(view.getContext(), task1.isSuccessful()
                                    ? "Added to cart"
                                    : "Failed to add to cart", Toast.LENGTH_SHORT).show();
                        });
            } else {
                // If user's cart does not exist, create a new cart and add the item
                docRef.set(Collections.singletonMap("items", Collections.singletonList(item)))
                        .addOnCompleteListener(task1 -> {
                            Toast.makeText(view.getContext(), task1.isSuccessful()
                                    ? "Added to cart"
                                    : "Failed to add to cart", Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }
}
