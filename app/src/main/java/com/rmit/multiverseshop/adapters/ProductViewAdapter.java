package com.rmit.multiverseshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.rmit.multiverseshop.AddToCartFragment;
import com.rmit.multiverseshop.R;
import com.rmit.multiverseshop.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class ProductViewAdapter extends RecyclerView.Adapter<ProductViewAdapter.ProductViewHolder> {
    private List<Product> productList;
    Context context;

    public ProductViewAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView productSoldCount;
        TextView productPrice;
        ImageButton addToCartButton;
        MaterialCardView productCard;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_title);
            productSoldCount = itemView.findViewById(R.id.product_sold_count);
            productPrice = itemView.findViewById(R.id.product_price);
            addToCartButton = itemView.findViewById(R.id.add_to_cart_button);
            productCard = itemView.findViewById(R.id.product_card);
        }
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card_view, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        Picasso.get().load(product.getImageUrl()).placeholder(R.drawable.placeholder).into(holder.productImage);

        holder.productName.setText(product.getName());
        holder.productSoldCount.setText(String.format(Locale.US, "%d sold", product.getProductsSold()));
        holder.productPrice.setText(String.format(Locale.US, "$%.2f", product.getPrice()));
        holder.addToCartButton.setOnClickListener(view -> {
            Toast.makeText(view.getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
        });

        holder.productCard.setOnClickListener(view -> {
            AddToCartFragment addToCartFragment = new AddToCartFragment();
            addToCartFragment.show(((AppCompatActivity) context).getSupportFragmentManager(),
                    addToCartFragment.getTag());
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
