package com.rmit.multiverseshop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.rmit.multiverseshop.adapters.ProductViewAdapter;
import com.rmit.multiverseshop.model.Product;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class CartFragment extends Fragment {
    FirebaseFirestore db;

    ProductViewAdapter adapter;

    ListenerRegistration registration;

    RecyclerView recyclerView;

    MaterialButton addToCartButton;

    ArrayList<Product> cartItems = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = view.findViewById(R.id.cart_recycler_view);
        addToCartButton = view.findViewById(R.id.add_to_cart_button2);



        return view;
    }
}
