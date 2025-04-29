package com.example.skinwise.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skinwise.Adapter.AdapterHistory;
import com.example.skinwise.LoginActivity;
import com.example.skinwise.Model.ModelHistory;
import com.example.skinwise.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UHistory extends Fragment {

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private ArrayList<ModelHistory> hisList;
    private AdapterHistory adapterHistory;
    private RecyclerView HisRv;
    private String uid;
    private Button fav;
    private TextView text;

    public UHistory() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_u_history, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading History...");
        progressDialog.setCanceledOnTouchOutside(false);

        HisRv = view.findViewById(R.id.HisRv);
        text = view.findViewById(R.id.text);


        HisRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        checkuser();
        loadHistory();




        return view;
    }



    private void loadHistory() {
        progressDialog.show();
        hisList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("History");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hisList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelHistory modelHistory = ds.getValue(ModelHistory.class);
                    hisList.add(modelHistory);
                }
                if (hisList.isEmpty()) {
                    adapterHistory = new AdapterHistory(getActivity(), hisList);
                    HisRv.setAdapter(adapterHistory);
                    progressDialog.dismiss();
                    text.setText("No History Saved");
                }else {
                    adapterHistory = new AdapterHistory(getActivity(), hisList);
                    HisRv.setAdapter(adapterHistory);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });
    }


    private void checkuser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }
    }



}