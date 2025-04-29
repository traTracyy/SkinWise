package com.example.skinwise.User;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.skinwise.Dermatologist.DSetting;
import com.example.skinwise.LoginActivity;
import com.example.skinwise.Model.SharedViewModel;
import com.example.skinwise.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class UDetails extends Fragment {

    private SwitchCompat switch1,switch2,switch3,switch4,switch5,switch6;
    private RelativeLayout Rlayout2;
    private FirebaseAuth firebaseAuth;
    private EditText age,lesion;
    private TextView yn1,yn2,yn3,yn4,yn5,yn6;
    private ProgressDialog progressDialog;
    private Button savebtn;



    public UDetails() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_u_details, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);


        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.getUid().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String uid) {
                // Use the uid here
            }
        });
        checkuser();

        Rlayout2 = view.findViewById(R.id.Rlayout2);//back
        switch1 = view.findViewById(R.id.switch1);
        switch2 = view.findViewById(R.id.switch2);
        switch3 = view.findViewById(R.id.switch3);
        switch4 = view.findViewById(R.id.switch4);
        switch5 = view.findViewById(R.id.switch5);
        switch6 = view.findViewById(R.id.switch6);
        yn1 = view.findViewById(R.id.yn1);
        yn2 = view.findViewById(R.id.yn2);
        yn3 = view.findViewById(R.id.yn3);
        yn4 = view.findViewById(R.id.yn4);
        yn5 = view.findViewById(R.id.yn5);
        yn6 = view.findViewById(R.id.yn6);
        age = view.findViewById(R.id.age);
        lesion = view.findViewById(R.id.lesion);
        savebtn = view.findViewById(R.id.savebtn);

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    yn1.setText("Yes");
                } else {
                    yn1.setText("No");
                }
            }
        });
        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    yn2.setText("Yes");
                } else {
                    yn2.setText("No");
                }
            }
        });
        switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    yn3.setText("Yes");
                } else {
                    yn3.setText("No");
                }
            }
        });
        switch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    yn4.setText("Yes");
                } else {
                    yn4.setText("No");
                }
            }
        });
        switch5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    yn5.setText("Yes");
                } else {
                    yn5.setText("No");
                }
            }
        });
        switch6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    yn6.setText("Yes");
                } else {
                    yn6.setText("No");
                }
            }
        });

        Rlayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new UHome();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container1,fragment).commit();
            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputData();

            }
        });
        return view;
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
    private String q8,q7;
    private Boolean q1,q2,q3,q4,q5,q6;
    private void inputData(){
        q1 = switch1.isChecked();
        q2 = switch2.isChecked();
        q3 = switch3.isChecked();
        q4 = switch4.isChecked();
        q5 = switch5.isChecked();
        q6 = switch6.isChecked();
        q7 = age.getText().toString().trim();
        q8 = lesion.getText().toString().trim();

        updateProfile();
    }
    private void updateProfile() {
        progressDialog.setMessage("Updating...");
        progressDialog.show();
        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("q1", "" + q1);
        hashMap.put("q2", "" + q2);
        hashMap.put("q3", "" + q3);
        hashMap.put("q4", "" + q4);
        hashMap.put("q5", "" + q5);
        hashMap.put("q6", "" + q6);
        hashMap.put("q7", "" + q7);
        hashMap.put("q8", "" + q8);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Information Updated", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }
    //load info
    private void loadInfo(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            String uid = ""+ds.child("uid").getValue();
                            String q1 = ""+ds.child("q1").getValue();
                            String q2 = ""+ds.child("q2").getValue();
                            String q3 = ""+ds.child("q3").getValue();
                            String q4 = ""+ds.child("q4").getValue();
                            String q5 = ""+ds.child("q5").getValue();
                            String q6 = ""+ds.child("q6").getValue();
                            String q7 = ""+ds.child("q7").getValue();
                            String q8 = ""+ds.child("q8").getValue();

                            age.setText(q7);
                            lesion.setText(q8);

                            if (q1.equals("true")){
                                switch1.setChecked(true);
                                yn1.setText("Yes");
                            }else {
                                switch1.setChecked(false);
                                yn1.setText("No");
                            }
                            if (q2.equals("true")){
                                switch2.setChecked(true);
                                yn2.setText("Yes");
                            }else {
                                switch2.setChecked(false);
                                yn2.setText("No");
                            }
                            if (q3.equals("true")){
                                switch3.setChecked(true);
                                yn3.setText("Yes");
                            }else {
                                switch3.setChecked(false);
                                yn3.setText("No");
                            }
                            if (q4.equals("true")){
                                switch4.setChecked(true);
                                yn4.setText("Yes");
                            }else {
                                switch4.setChecked(false);
                                yn4.setText("No");
                            }
                            if (q5.equals("true")){
                                switch5.setChecked(true);
                                yn5.setText("Yes");
                            }else {
                                switch5.setChecked(false);
                                yn5.setText("No");
                            }
                            if (q6.equals("true")){
                                switch6.setChecked(true);
                                yn6.setText("Yes");
                            }else {
                                switch6.setChecked(false);
                                yn6.setText("No");
                            }



                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

}