package com.example.skinwise.User;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skinwise.Adapter.AdapterType;
import com.example.skinwise.Model.ModelType;
import com.example.skinwise.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class UViewTypes extends Fragment {
    private AdapterType adapter;
    private ArrayList<ModelType> lesionData;
    private RelativeLayout Rlayout2;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    public UViewTypes() {
        lesionData = new ArrayList<>();
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_u_view_types, container, false);


        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);

        Rlayout2 = view.findViewById(R.id.Rlayout2);//back
        Rlayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new UHome();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container1,fragment).commit();
            }
        });
        RecyclerView recyclerView = view.findViewById(R.id.lesionRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AdapterType(getActivity(), lesionData);
        recyclerView.setAdapter(adapter);
        loadLesionDataFromFirebase();
        return view;
    }
    private void loadLesionDataFromFirebase() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("SkinLesion");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ModelType lesion = snapshot.getValue(ModelType.class);
                        lesion.setName(snapshot.getKey());
                        lesionData.add(lesion);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }


}