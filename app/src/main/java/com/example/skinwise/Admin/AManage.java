package com.example.skinwise.Admin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skinwise.Adapter.AdapterManageDerm;
import com.example.skinwise.Model.ModelDerm;
import com.example.skinwise.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AManage extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<ModelDerm> dermatologistList;
    private AdapterManageDerm adapter;
    private TextView text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a_manage, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewDermatologists);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dermatologistList = new ArrayList<>();
        adapter = new AdapterManageDerm(getContext(), dermatologistList);

        text = view.findViewById(R.id.text);
        adapter.setOnDermatologistClickListener(new AdapterManageDerm.OnDermatologistClickListener() {
            @Override
            public void onDermatologistClick(ModelDerm dermatologist) {
                navigateToDermaDetails(dermatologist);
            }
        });

        recyclerView.setAdapter(adapter);

        fetchDermatologists(); // Call the method to fetch dermatologists

        return view;
    }

    private void fetchDermatologists() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("approve").equalTo("false")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dermatologistList.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            ModelDerm dermatologist = ds.getValue(ModelDerm.class);
                            if (dermatologist != null) { // Make sure the object is not null
                                dermatologistList.add(dermatologist);
                            }
                        }
                        if (dermatologistList.isEmpty()) {
                            text.setText("No Waiting List");
                            text.setVisibility(View.VISIBLE); // Show the TextView
                        } else {
                            text.setVisibility(View.GONE); // Hide the TextView
                        }
                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void navigateToDermaDetails(ModelDerm dermatologist) {
        AManageDermaDetails detailsFragment = new AManageDermaDetails();

        Bundle args = new Bundle();
        args.putString("dermatologistUid", dermatologist.getUid());
        // Add other details like name, email, etc.
        detailsFragment.setArguments(args);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container3, detailsFragment)
                .addToBackStack(null)
                .commit();
    }
}
