package com.example.skinwise.User;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.skinwise.Model.ModelHistory;
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

public class UHisDetails extends Fragment {

    private TextView lesionTypeTextView,Melanoma,MV,BCC,AK,BK,DF,VL,SCC,timetv,datetv;
    private TextView gendertxt,locationtxt,agetxt;
    private ImageView imageView;
    private FirebaseAuth firebaseAuth;
    private LinearLayout confidenceLayout;
    private RelativeLayout Rlayout2,deletehistory;

    private String historyUid;
    public UHisDetails() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_u_his_details, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        lesionTypeTextView = view.findViewById(R.id.lesion_type);
        confidenceLayout = view.findViewById(R.id.confidence_layout);
        imageView = view.findViewById(R.id.imageView);

        agetxt = view.findViewById(R.id.agetxt);
        gendertxt = view.findViewById(R.id.gendertxt);
        locationtxt = view.findViewById(R.id.locationtxt);

        Bundle bundle = getArguments();
        if (bundle != null) {
            historyUid = bundle.getString("historyUid");
        }
        deletehistory = view.findViewById(R.id.deletehistory);
        deletehistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removehistory(historyUid);
            }
        });

        Rlayout2 = view.findViewById(R.id.Rlayout2);//back
        Rlayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new UHistory();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container1,fragment).commit();
            }
        });


        timetv = view.findViewById(R.id.timetv);
        datetv = view.findViewById(R.id.datetv);
        Melanoma = view.findViewById(R.id.Melanoma);
        MV = view.findViewById(R.id.MV);
        BCC = view.findViewById(R.id.BCC);
        AK = view.findViewById(R.id.AK);
        BK = view.findViewById(R.id.BK);
        DF = view.findViewById(R.id.DF);
        VL = view.findViewById(R.id.VL);
        SCC = view.findViewById(R.id.SCC);

        String hisuid;
        if (bundle != null) {
            hisuid = bundle.getString("historyUid");
            String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            if (currentUserUid != null && hisuid != null) {
                DatabaseReference historyRef = FirebaseDatabase.getInstance().getReference("Users")
                        .child(currentUserUid)
                        .child("History")
                        .child(hisuid);

                historyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            ModelHistory history = snapshot.getValue(ModelHistory.class);
                            displayHistoryDetails(history);
                        } else {
                            // Handle case when history data does not exist
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle onCancelled event
                    }
                });
            } else {
                // Handle case when currentUserUid or hisuid is null
            }
        } else {
            // Handle case when bundle is null
        }

        return view;
    }

    private void removehistory(String historyUid) {
        String currentUserId = firebaseAuth.getUid();

        // Assuming you have a method to get the current user's UID
        DatabaseReference userHisRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(currentUserId)
                .child("History")
                .child(historyUid);

        userHisRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Bookmark exists, so remove it
                    userHisRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "History removed", Toast.LENGTH_SHORT).show();
                            Fragment fragment = new UHistory();
                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.container1,fragment).commit();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Failed to remove history", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Bookmark doesn't exist, so inform the user or take other appropriate action
                    Toast.makeText(getActivity(), "History does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Failed to check history", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void displayHistoryDetails(ModelHistory history) {

        lesionTypeTextView.setText(history.getLesionType());

        datetv.setText(history.getDate());
        timetv.setText(history.getTime());

        // Set confidence levels
        Melanoma.setText(String.format("%.1f%%", Float.parseFloat(history.getSL1()) * 100));
        MV.setText(String.format("%.1f%%", Float.parseFloat(history.getSL2()) * 100));
        BCC.setText(String.format("%.1f%%", Float.parseFloat(history.getSL3()) * 100));
        AK.setText(String.format("%.1f%%", Float.parseFloat(history.getSL4()) * 100));
        BK.setText(String.format("%.1f%%", Float.parseFloat(history.getSL5()) * 100));
        DF.setText(String.format("%.1f%%", Float.parseFloat(history.getSL6()) * 100));
        VL.setText(String.format("%.1f%%", Float.parseFloat(history.getSL7()) * 100));
        SCC.setText(String.format("%.1f%%", Float.parseFloat(history.getSL8()) * 100));


        locationtxt.setText(history.getAnatomicalSite());
        gendertxt.setText(history.getGender());
        agetxt.setText(history.getAge());

        // Set image
        Picasso.get().load(history.getImageURL()).into(imageView);
    }

}