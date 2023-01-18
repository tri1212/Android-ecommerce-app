package com.rmit.multiverseshop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

public class AddToCartFragment extends BottomSheetDialogFragment {
    MaterialButton addToCartButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_to_cart, container, false);
        addToCartButton = view.findViewById(R.id.add_to_cart_button1);
        addToCartButton.setOnClickListener(view1 -> {
            this.dismiss();
            Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }
}
