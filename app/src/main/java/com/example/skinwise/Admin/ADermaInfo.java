package com.example.skinwise.Admin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.skinwise.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class ADermaInfo extends Fragment {
    private TextView uidTextView, emailTextView, nameTextView, phnumTextView, stateTextView,addressTextView;
    private ImageView shopImageView;

    private String userId;
    private Button deleteUserButton;
    private RelativeLayout Rlayout2;


    public ADermaInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a_derma_info, container, false);


        Rlayout2 = view.findViewById(R.id.Rlayout2);//back
        uidTextView = view.findViewById(R.id.userInfo_Uid);
        emailTextView = view.findViewById(R.id.userInfo_Email);
        nameTextView = view.findViewById(R.id.userInfo_Name);
        phnumTextView = view.findViewById(R.id.userInfo_Phnum);
        stateTextView = view.findViewById(R.id.userInfo_State);
        shopImageView = view.findViewById(R.id.userInfo_ShopImg);
        deleteUserButton = view.findViewById(R.id.deleteUserButton);
        addressTextView = view.findViewById(R.id.userInfo_Address);

        Rlayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new AManageDerma();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container3,fragment).commit();
            }
        });
        if (getArguments() != null) {

            String uid = getArguments().getString("user_uid", "");
            String email = getArguments().getString("user_email", "");
            String name = getArguments().getString("user_name", "");
            String phnum = getArguments().getString("user_phnum", "");
            String state = getArguments().getString("user_state", "");
            String shopImg = getArguments().getString("user_shopImg", "");
            String address = getArguments().getString("user_address", "");


            // Set the text in TextViews
            uidTextView.setText(uid);
            emailTextView.setText(email);
            nameTextView.setText(name);
            phnumTextView.setText(phnum);
            stateTextView.setText(state);
            addressTextView.setText(address);
            userId = getArguments().getString("user_uid");


            if (shopImg != null && !shopImg.isEmpty()) {
                Picasso.get().load(shopImg).placeholder(R.drawable.ic_profile).error(R.drawable.ic_profile).into(shopImageView);
            } else {
                if (isAdded()) { // Check if the fragment is currently added to its activity
                    shopImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_profile));
                }
            }
        }
        deleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser();
            }
        });

        return view;
    }
    private void deleteUser() {
        if (userId != null && !userId.isEmpty()) {
            // Firebase delete logic
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(userId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "User deleted successfully", Toast.LENGTH_SHORT).show();
                        // Navigate back or update UI accordingly
                    } else {
                        Toast.makeText(getContext(), "Failed to delete user", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(getContext(), "Error: User ID is missing", Toast.LENGTH_SHORT).show();
        }
    }

}











