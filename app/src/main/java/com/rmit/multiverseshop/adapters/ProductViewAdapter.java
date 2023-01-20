package com.rmit.multiverseshop.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_title);
            productSoldCount = itemView.findViewById(R.id.product_sold_count);
            productPrice = itemView.findViewById(R.id.product_price);
            addToCartButton = itemView.findViewById(R.id.add_to_cart_button);
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
        Product product = productList.get(holder.getAdapterPosition());

        Picasso.get().load(product.getImageUrl()).placeholder(R.drawable.placeholder).into(holder.productImage);

        String productsSold = String.format(Locale.US, "%d sold", product.getProductsSold());
        String price = String.format(Locale.US, "$%.2f", product.getPrice());

        holder.productName.setText(product.getName());
        holder.productSoldCount.setText(productsSold);
        holder.productPrice.setText(price);
        holder.addToCartButton.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("product", product);

            AddToCartFragment addToCartFragment = new AddToCartFragment();
            addToCartFragment.setArguments(bundle);
            addToCartFragment.show(((AppCompatActivity) context).getSupportFragmentManager(),
                    addToCartFragment.getTag());
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
