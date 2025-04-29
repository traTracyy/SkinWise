package com.example.skinwise.Admin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.skinwise.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AChangePassowrd extends Fragment {
    private EditText passwordOldHint, passwordNewHint, passwordConfirmHint;
    private Button changePasswordButton;
    private String userUid;
    private RelativeLayout Rlayout2;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    public AChangePassowrd() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_a_change_passowrd, container, false);
        firebaseAuth = FirebaseAuth.getInstance();


        Rlayout2 = view.findViewById(R.id.Rlayout2);//back
        passwordOldHint = view.findViewById(R.id.passwordhint);
        passwordNewHint = view.findViewById(R.id.passwordhint1);
        passwordConfirmHint = view.findViewById(R.id.passwordhint2);
        changePasswordButton = view.findViewById(R.id.chnagepassowrdbtn);

        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);


        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });
        //back to login page
        Rlayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ASetting();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container3, fragment).commit();
            }
        });

        return view;

    }

    private void changePassword() {
        String oldPassword = passwordOldHint.getText().toString();
        String newPassword = passwordNewHint.getText().toString();
        String confirmPassword = passwordConfirmHint.getText().toString();

        if (newPassword.length() < 8) {
            Toast.makeText(getActivity(), "Password at least 8 characters!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(oldPassword)){
            Toast.makeText(getActivity(),"Please enter the original password!",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(getActivity(), "Password at least 8 characters!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(getActivity(), "Please Enter Confirm Password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(getActivity(), "Password doesn't match, pleas enter both correctly!", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseUser user = firebaseAuth.getCurrentUser();

        // Reauthenticate the user
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.setMessage("Updating...");
                    progressDialog.show();
                    // Old password is correct, now update to new password
                    user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Password updated successfully.", Toast.LENGTH_SHORT).show();

                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                                Map<String, Object> hashMap = new HashMap<>();
                                hashMap.put("password", "" + newPassword);
                                databaseReference.child(firebaseAuth.getUid()).updateChildren(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                progressDialog.dismiss();
                                                Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@androidx.annotation.NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        });

                            } else {
                                Toast.makeText(getActivity(), "Error to update the password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    // Old password is incorrect
                    Toast.makeText(getActivity(), "Old password is incorrect", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}


