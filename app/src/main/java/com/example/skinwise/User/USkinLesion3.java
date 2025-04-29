package com.example.skinwise.User;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.skinwise.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class USkinLesion3 extends Fragment {

    private TextView typical,symptoms,overview,skintype,copyright;
    private ImageView skinlesionImg;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    private RelativeLayout Rlayout2;
    private RelativeLayout bookmark;

    private String lesionType;

    public USkinLesion3() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_u_skin_lesion3, container, false);


        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);

        Rlayout2 = view.findViewById(R.id.Rlayout2);//back
        skintype = view.findViewById(R.id.skintype);
        typical = view.findViewById(R.id.typical);
        symptoms = view.findViewById(R.id.symptoms);
        overview = view.findViewById(R.id.overview);
        skinlesionImg = view.findViewById(R.id.skinlesionImg);
        bookmark = view.findViewById(R.id.bookmark);
        copyright = view.findViewById(R.id.copyright);


        Bundle bundle = getArguments();
        if (bundle != null) {
            lesionType = bundle.getString("lesionType");

            skintype.setText(lesionType);
            loadLesionData(lesionType);
        }
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeBookmark(lesionType);
            }
        });

        Rlayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new UFavourite();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container1,fragment).commit();
            }
        });

        return view;
    }
    private void removeBookmark(String lesionType) {
        String currentUserId = firebaseAuth.getUid();

        // Assuming you have a method to get the current user's UID
        DatabaseReference userFavRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(currentUserId)
                .child("Favorites")
                .child(lesionType);

        userFavRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Bookmark exists, so remove it
                    userFavRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Bookmark removed", Toast.LENGTH_SHORT).show();
                            Fragment fragment = new UFavourite();
                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.container1,fragment).commit();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Failed to remove bookmark", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Bookmark doesn't exist, so inform the user or take other appropriate action
                    Toast.makeText(getActivity(), "Bookmark does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Failed to check bookmark", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadLesionData(String lesionType) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("SkinLesion").child(lesionType);

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String imageUrl = dataSnapshot.child("imageURL").getValue(String.class);
                    String overviewText = dataSnapshot.child("overview").getValue(String.class);
                    String symptomsText = dataSnapshot.child("symptoms").getValue(String.class);
                    String typicalText = dataSnapshot.child("Cause").getValue(String.class);
                    String copyrightText = dataSnapshot.child("copyright").getValue(String.class);

                    overview.setText(overviewText);
                    symptoms.setText(symptomsText);
                    typical.setText(typicalText);
                    copyright.setText(copyrightText);
                    Picasso.get().load(imageUrl).into(skinlesionImg); // using Picasso to load the image
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }
}