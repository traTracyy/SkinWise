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

import com.example.skinwise.Model.ModelDerm;
import com.example.skinwise.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AManageDermaDetails extends Fragment {

    private RelativeLayout Rlayout2;
    private ImageView ivProfileImage;
    private TextView tvName, tvEmail,tvPhoneNumber,tvState;
    private Button btnApprove;
    private DatabaseReference usersRef;
    private Button deleteUserButton;
    private String dermatologistUid;
    public AManageDermaDetails() {    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a_manage_derma_details, container, false);

        Rlayout2 = view.findViewById(R.id.Rlayout2);
        ivProfileImage = view.findViewById(R.id.ivProfileImage);
        tvName = view.findViewById(R.id.tvName);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber);
        tvState = view.findViewById(R.id.tvState);
        btnApprove = view.findViewById(R.id.btnApprove);
        deleteUserButton = view.findViewById(R.id.deleteUserButton);

        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        Bundle args = getArguments();
        if (args != null) {
            dermatologistUid = args.getString("dermatologistUid");
            loadDermatologistDetails(dermatologistUid);
        }

        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                approveDermatologist(dermatologistUid);
            }
        });
        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approveDermatologist(dermatologistUid);
            }
        });
        //back to Amanage
        Rlayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new AManage();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container3,fragment).commit();
            }
        });

        deleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser();
            }
        });

        return view;
    }
    private void deleteUser() {
        if (dermatologistUid != null && !dermatologistUid.isEmpty()) {
            // Firebase delete logic
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(dermatologistUid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Delete successfully", Toast.LENGTH_SHORT).show();
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

    private void loadDermatologistDetails(String uid) {
        usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Assuming ModelDerm class has these fields
                ModelDerm dermatologist = dataSnapshot.getValue(ModelDerm.class);
                if (dermatologist != null) {
                    tvEmail.setText(dermatologist.getEmail());
                    tvName.setText(dermatologist.getName());
                    tvPhoneNumber.setText(dermatologist.getPhnum());
                    tvState.setText(dermatologist.getState());

                    String profileImageUrl = dermatologist.getProfileImage();
                    if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                        Picasso.get().load(profileImageUrl).placeholder(R.drawable.ic_profile).into(ivProfileImage);
                    } else {
                        ivProfileImage.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.ic_profile));
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void approveDermatologist(String uid) {
        usersRef.child(uid).child("approve").setValue("true")
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(getContext(), "Dermatologist approved", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Approval failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
//
//    private void approveDermatologist() {
//        // Fetch uid or identifier of the dermatologist
//        String dermatologistUid = ""; // Replace with actual uid
//
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
//        ref.child(dermatologistUid).child("approve").setValue("true")
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(getContext(), "Dermatologist approved", Toast.LENGTH_SHORT).show();
//                        // Handle additional logic such as navigating back
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getContext(), "Failed to approve: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
