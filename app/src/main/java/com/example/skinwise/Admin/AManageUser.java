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

import com.example.skinwise.Adapter.AdapterManageUser;
import com.example.skinwise.Model.ModelUser;
import com.example.skinwise.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AManageUser extends Fragment {


    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private ArrayList<ModelUser> modelUsers;
    private AdapterManageUser adapterUser;
    private RecyclerView recyclerViewUsers;
    private RelativeLayout Rlayout2;

    public AManageUser() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a_manage_user, container, false);

        Rlayout2 = view.findViewById(R.id.Rlayout2);//back
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);


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

//        addadminbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                AAddAdmin aAddAdmin = new AAddAdmin();
//                transaction.replace(R.id.container3, aAddAdmin).commit();
//            }
//        });

        return view;
    }

    private void loadAllUser() {
        progressDialog.show();
        modelUsers = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelUsers.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelUser modelUser = ds.getValue(ModelUser.class);
                    // Check if the user has the role "user"
                    if (modelUser != null && "user".equals(modelUser.getRole())) {
                        modelUsers.add(modelUser);
                    }
                }
                if (adapterUser == null) {
                    adapterUser = new AdapterManageUser(getActivity(), modelUsers);
                    recyclerViewUsers.setAdapter(adapterUser);
                } else {
                    adapterUser.notifyDataSetChanged();
                }
                //--------------------
                adapterUser = new AdapterManageUser(getActivity(), modelUsers);
                adapterUser.setUserClickListener(new AdapterManageUser.UserClickListener() {
                    @Override
                    public void onUserClicked(ModelUser user) {
                        showUserInfo(user);
                    }
                });
                recyclerViewUsers.setAdapter(adapterUser);
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
    private void showUserInfo(ModelUser user) {
        Bundle bundle = new Bundle();
        bundle.putString("user_uid", user.getUid());
        bundle.putString("user_email", user.getEmail());
        bundle.putString("user_name", user.getName());
        bundle.putString("user_phnum", user.getPhnum());
        bundle.putString("user_state", user.getState());
        bundle.putString("user_shopImg", user.getProfileImage());

        AUserInfo userInfoFragment = new AUserInfo();
        userInfoFragment.setArguments(bundle);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container3, userInfoFragment); // Replace with your container ID
        transaction.addToBackStack(null); // Add to back stack for navigation
        transaction.commit();
    }

}


