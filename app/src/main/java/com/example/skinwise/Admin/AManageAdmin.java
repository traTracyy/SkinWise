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

import com.example.skinwise.Adapter.AdapterManageAdmin;
import com.example.skinwise.Model.ModelAdmin;
import com.example.skinwise.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AManageAdmin extends Fragment {


    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private ArrayList<ModelAdmin> modelAdmins;
    private AdapterManageAdmin adapterManageAdmin;
    private RecyclerView recyclerViewUsers;

    private RelativeLayout Rlayout2;
    public AManageAdmin() {    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a_manage_admin, container, false);

        Rlayout2 = view.findViewById(R.id.Rlayout2);//back
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
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
        modelAdmins = new ArrayList<>();
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get the UID of the current use

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelAdmins.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelAdmin modelAdmin = ds.getValue(ModelAdmin.class);
                    if (modelAdmin != null && "admin".equals(modelAdmin.getRole()) && !modelAdmin.getUid().equals(currentUserId)) {
                        modelAdmins.add(modelAdmin);
                    }
                }
                if (adapterManageAdmin == null) {
                    adapterManageAdmin = new AdapterManageAdmin(getActivity(), modelAdmins);
                    recyclerViewUsers.setAdapter(adapterManageAdmin);
                } else {
                    adapterManageAdmin.notifyDataSetChanged();
                }
                //--------------------
                adapterManageAdmin = new AdapterManageAdmin(getActivity(), modelAdmins);
                adapterManageAdmin.setUserClickListener(new AdapterManageAdmin.UserClickListener() {
                    @Override
                    public void onUserClicked(ModelAdmin user) {
                        showUserInfo(user);
                    }
                });
                recyclerViewUsers.setAdapter(adapterManageAdmin);
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
    private void showUserInfo(ModelAdmin user) {
        Bundle bundle = new Bundle();
        bundle.putString("user_uid", user.getUid());
        bundle.putString("user_email", user.getEmail());
        bundle.putString("user_name", user.getName());
        bundle.putString("user_phnum", user.getPhnum());
        bundle.putString("user_state", user.getState());
        bundle.putString("user_shopImg", user.getProfileImage());

        AAdminInfo userInfoFragment = new AAdminInfo();
        userInfoFragment.setArguments(bundle);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container3, userInfoFragment); // Replace with your container ID
        transaction.addToBackStack(null); // Add to back stack for navigation
        transaction.commit();
    }

}
