package com.rmit.multiverseshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.rmit.multiverseshop.adapters.ProductViewAdapter;
import com.rmit.multiverseshop.model.Product;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {

    FirebaseFirestore db;

    ProductViewAdapter adapter;

    ListenerRegistration registration;

    androidx.appcompat.widget.SearchView searchView;

    RecyclerView recyclerView;

    ArrayList<Product> popularProducts = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        db = FirebaseFirestore.getInstance();

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(
                new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));

        adapter = new ProductViewAdapter(popularProducts, getContext());
        recyclerView.setAdapter(adapter);

        searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
                searchIntent.putExtra("search_query", s);
                startActivity(searchIntent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        view.findViewById(R.id.food_category).setOnClickListener(view1 -> searchByCategory("food"));
        view.findViewById(R.id.drinks_category).setOnClickListener(view1 -> searchByCategory("drinks"));
        view.findViewById(R.id.tech_category).setOnClickListener(view1 -> searchByCategory("tech"));
        view.findViewById(R.id.fashion_category).setOnClickListener(view1 -> searchByCategory("fashion"));
        view.findViewById(R.id.sports_category).setOnClickListener(view1 -> searchByCategory("sports"));
        view.findViewById(R.id.pet_category).setOnClickListener(view1 -> searchByCategory("pet"));

        return view;
    }

    private void searchByCategory(String category) {
        Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
        searchIntent.putExtra("category", category);
        startActivity(searchIntent);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadPopularProducts();
    }

    @Override
    public void onStop() {
        super.onStop();
        registration.remove();
    }

    @Override
    public void onResume() {
        super.onResume();
        searchView.setQuery("", false);
        searchView.clearFocus();
    }

    // Load 20 best selling products
    private void loadPopularProducts() {
        registration = db.collection("products")
                .orderBy("productsSold", Query.Direction.DESCENDING).limit(20)
                .addSnapshotListener((querySnapshot, error) -> {
                    if (querySnapshot == null || error != null) {
                        Toast.makeText(getContext(), "Failed to fetch products",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    popularProducts.clear();
                    for(DocumentSnapshot document: querySnapshot.getDocuments()) {
                        try {
                            Product product = document.toObject(Product.class);
                            if (product == null) continue;
                            popularProducts.add(product);
                        } catch (RuntimeException e) {
                            e.printStackTrace();
                        }
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}
