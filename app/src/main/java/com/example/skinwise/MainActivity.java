package com.example.skinwise;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.skinwise.Admin.MAdminActivity;
import com.example.skinwise.Dermatologist.MDermaActivity;
import com.example.skinwise.User.MUserActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        firebaseAuth = FirebaseAuth.getInstance();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null){
                    //havt login, display login page
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }else {
                    UserRole();
                }
            }
        },1000);

    }

    private void UserRole(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String role = ""+snapshot.child("role").getValue();
                        switch (role) {
                            case "user":
                                startActivity(new Intent(MainActivity.this, MUserActivity.class));
                                finish();
                                break;
                            case "dermatologist":
                                startActivity(new Intent(MainActivity.this, MDermaActivity.class));
                                finish();
                                break;
                            case "admin":
                                startActivity(new Intent(MainActivity.this, MAdminActivity.class));
                                finish();
                                break;
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }



}