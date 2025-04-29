package com.example.skinwise.Dermatologist;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skinwise.Adapter.AdapterUser;
import com.example.skinwise.LoginActivity;
import com.example.skinwise.Model.ModelUser;
import com.example.skinwise.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DHome extends Fragment {
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private TextView welcometxt,text;
    private ArrayList<ModelUser> userList;
    private AdapterUser adapterUser;
    private RecyclerView userRv;

    public DHome() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_d_home, container, false);

        userRv = view.findViewById(R.id.userRv);
        welcometxt = view.findViewById(R.id.welcometxt);
        text = view.findViewById(R.id.text);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Patient...");
        progressDialog.setCanceledOnTouchOutside(false);

        userRv.setLayoutManager(new LinearLayoutManager(getActivity()));

        checkuser();

        loadConsultedUsers();

    }




    private void loadConsultedUsers() {
        progressDialog.show();
        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("chats");
        String dermUid = firebaseAuth.getUid();
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Set<String> userIds = new HashSet<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String chatRoom = ds.getKey();
                    if (chatRoom != null && chatRoom.startsWith(dermUid)) {
                        String userId = chatRoom.replace(dermUid, ""); // Extract user UID
                        userIds.add(userId);
                    } else if (chatRoom != null && chatRoom.endsWith(dermUid)) {
                        String userId = chatRoom.replace(dermUid, ""); // Extract user UID
                        userIds.add(userId);
                    }
                }
                loadUsersDetails(userIds);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });
    }


    private void loadUsersDetails(Set<String> userIds) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelUser user = ds.getValue(ModelUser.class);
                    if (user != null && userIds.contains(user.getUid())) {
                        userList.add(user);
                    }
                }
                if (userList.isEmpty()) {
                    text.setText("No Consultation");
                } else {
                    adapterUser = new AdapterUser(getActivity(), userList);
                    userRv.setAdapter(adapterUser);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }




    private void checkuser(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null){
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
        else {
            loadInfo();
        }
    }
    //loading user's information
    private void loadInfo(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            String name = ""+ds.child("name").getValue();
                            //String role = ""+ds.child("role").getValue();

                            welcometxt.setText("Welcome, "+ name);
                            //loadShopInfo();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}