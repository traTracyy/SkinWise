package com.example.skinwise.Admin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skinwise.Adapter.AdapterManageDerma;
import com.example.skinwise.Model.ModelDerm;
import com.example.skinwise.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AManageDerma extends Fragment {



    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private ArrayList<ModelDerm> modelDerms;
    private AdapterManageDerma adapterManageDerma;
    private RecyclerView recyclerViewUsers;
    private RelativeLayout Rlayout2;


    public AManageDerma() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a_manage_derma, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
        Rlayout2 = view.findViewById(R.id.Rlayout2);//back


//        addadminbtn = view.findViewById(R.id.addadminbtn);
        recyclerViewUsers = view.findViewById(R.id.recyclerViewUsers);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadAllUser();

        Rlayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new AManageU();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container3,fragment).commit();
            }
        });

        return view;
    }

    private void loadAllUser() {
        progressDialog.show();
        modelDerms = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelDerms.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelDerm derm = ds.getValue(ModelDerm.class);
                    if (derm != null && "Derma".equals(derm.getRole())) {
                        modelDerms.add(derm);
                    }
                }
                if (adapterManageDerma == null) {
                    adapterManageDerma = new AdapterManageDerma(getActivity(), modelDerms);
                    recyclerViewUsers.setAdapter(adapterManageDerma);
                } else {
                    adapterManageDerma.notifyDataSetChanged();
                }
                //--------------------
                adapterManageDerma = new AdapterManageDerma(getActivity(), modelDerms);
                adapterManageDerma.setUserClickListener(new AdapterManageDerma.UserClickListener() {
                    @Override
                    public void onUserClicked(ModelDerm user) {
                        showUserInfo(user);
                    }
                });

                recyclerViewUsers.setAdapter(adapterManageDerma);
                //---------------------
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Log.e("loadAllUser", "Error loading data: " + error.getMessage());
            }
        });
    }
    private void showUserInfo(ModelDerm user) {
        Bundle bundle = new Bundle();
        bundle.putString("user_uid", user.getUid());
        bundle.putString("user_email", user.getEmail());
        bundle.putString("user_name", user.getName());
        bundle.putString("user_phnum", user.getPhnum());
        bundle.putString("user_state", user.getState());
        bundle.putString("user_shopImg", user.getProfileImage());
        bundle.putString("user_address", user.getAddress());

        ADermaInfo userInfoFragment = new ADermaInfo();
        userInfoFragment.setArguments(bundle);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container3, userInfoFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
