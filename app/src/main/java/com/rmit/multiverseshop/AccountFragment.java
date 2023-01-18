package com.rmit.multiverseshop;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import androidx.fragment.app.Fragment;

public class AccountFragment extends Fragment {
    RelativeLayout signOut;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_fragment, container, false);

        auth = FirebaseAuth.getInstance();

        signOut = view.findViewById(R.id.option_signout);
        signOut.setOnClickListener(v -> {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Confirm sign out")
                    .setMessage("Do you want to sign out?")
                    .setPositiveButton("Yes", (dialog, i) -> {
                        auth.signOut();
                        Toast.makeText(getActivity(), "Signed out successfully",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                    })
                    .setNegativeButton("Cancel", (dialog, i) -> dialog.cancel())
                    .create()
                    .show();
        });
        return view;
    }
}
