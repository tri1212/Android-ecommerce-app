package com.rmit.multiverseshop;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.rmit.multiverseshop.adapters.ProductViewAdapter;
import com.rmit.multiverseshop.model.Product;

import java.util.ArrayList;
import java.util.Collections;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchActivity extends AppCompatActivity {
    FirebaseFirestore db;

    ProductViewAdapter adapter;

    ListenerRegistration registration;

    androidx.appcompat.widget.SearchView searchView;

    TextView searchResultsText;

    ImageButton backButton;

    Spinner sortSpinner;
    Spinner filterSpinner;

    String query;
    String filterBy;
    String sortBy;

    ArrayList<Product> products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        db = FirebaseFirestore.getInstance();

        searchResultsText = findViewById(R.id.search_results_text);

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> finish());

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(
                new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));

        adapter = new ProductViewAdapter(products, this);
        recyclerView.setAdapter(adapter);

        searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) { return false; }

            @Override
            public boolean onQueryTextChange(String s) {
                query = s;
                loadProducts();
                return false;
            }
        });

        sortSpinner = findViewById(R.id.sort_spinner);
        filterSpinner = findViewById(R.id.filter_spinner);

        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(
                SearchActivity.this, R.array.sort_array, android.R.layout.simple_spinner_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> filterAdapter = ArrayAdapter.createFromResource(
                SearchActivity.this, R.array.filter_array, android.R.layout.simple_spinner_item);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sortSpinner.setAdapter(sortAdapter);
        filterSpinner.setAdapter(filterAdapter);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sortBy = adapterView.getItemAtPosition(i).toString().toLowerCase();
                sortProducts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filterBy = adapterView.getItemAtPosition(i).toString().toLowerCase();
                loadProducts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle extras = getIntent().getExtras();
        query = extras.getString("search_query");
        filterBy = extras.getString("category");

        if(query != null)
            searchView.setQuery(query, false);

        loadProducts();
    }

    @Override
    public void onStop() {
        super.onStop();
        registration.remove();
    }

    private void loadProducts() {
        registration = db.collection("products")
                .addSnapshotListener((querySnapshot, error) -> {
                    if (querySnapshot == null || error != null) {
                        Toast.makeText(SearchActivity.this,
                                "Failed to fetch products", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    products.clear();

                    for(DocumentSnapshot document: querySnapshot.getDocuments()) {
                        Product product = document.toObject(Product.class);
                        if (product == null) return;
                        if ((query != null
                                && product.getName().toLowerCase().contains(query.toLowerCase())
                                && (filterBy == null || "none".equals(filterBy) || product.getCategory().equals(filterBy)))
                                || (query == null && product.getCategory().equals(filterBy))) {
                            products.add(product);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    searchResultsText.setText("Products found: " + products.size());
                });
    }

    private void sortProducts() {
        switch (sortBy) {
            case "price":
                Collections.sort(products, (p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()));
                break;
            case "popularity":
                Collections.sort(products, (p1, p2) -> Integer.compare(p2.getProductsSold(), p1.getProductsSold()));
                break;
            case "name":
                Collections.sort(products, (p1, p2) -> p2.getName().compareTo(p1.getName()));
                break;
        }
        adapter.notifyDataSetChanged();
    }
}
