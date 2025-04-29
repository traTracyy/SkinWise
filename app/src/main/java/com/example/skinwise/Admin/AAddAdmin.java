package com.example.skinwise.Admin;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.skinwise.Constants;
import com.example.skinwise.LoginActivity;
import com.example.skinwise.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;

public class AAddAdmin extends Fragment {
    private RelativeLayout Rlayout2;
    private ImageView profile;
    private EditText namehint,phonenumberhint, emailhint,passwordhint,passwordhint2,addresshint;
    private TextView registersellertxt;
    private Button registerbtn1;
    private Uri mImageUri;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    private static final int REQUEST_CODE = 1;
    public AAddAdmin() {    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a_add_admin, container, false);


        Rlayout2 = view.findViewById(R.id.Rlayout2);//back
        namehint= view.findViewById(R.id.namehint);
        phonenumberhint = view.findViewById(R.id.phonenumberhint);
        registerbtn1 = view.findViewById(R.id.registerbtn1);
        profile = view.findViewById(R.id.profile);
        emailhint = view.findViewById(R.id.emailhint);
        passwordhint = view.findViewById(R.id.passwordhint);
        passwordhint2 = view.findViewById(R.id.passwordhint2);
        addresshint = view.findViewById(R.id.addresshint);


        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Connecting...");
        progressDialog.setCanceledOnTouchOutside(false);




        Rlayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new AManageU();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container3,fragment).commit();
            }
        });
        addresshint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stateDialog();
            }
        });

        //confirm register as admin
        //using function:saveFirebaseData,reqisterselleracc,inputData
        registerbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputData();
            }
        });

        //choose and upload profile
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE);
            }
        });

        return view;
    }

    //choose image
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mImageUri);
                profile.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //state
    private void stateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Your State").setItems(Constants.state, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String selectedState = Constants.state[which];
                addresshint.setText(selectedState);
            }
        }).show();
    }


    private String name,email,phnum,password,cpassword, state;

    //validate
    private void inputData(){
        name=namehint.getText().toString().trim();
        email=emailhint.getText().toString().trim();
        phnum=phonenumberhint.getText().toString().trim();
        password=passwordhint.getText().toString().trim();
        cpassword=passwordhint2.getText().toString().trim();
        state =addresshint.getText().toString().trim();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(getActivity(),"Please Enter Your User Name",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(state)){
            Toast.makeText(getActivity(),"Please Choose Your State",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(getActivity(),"Invalid Email Address",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(getActivity(),"Please Enter Your Email Address",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(phnum)){
            Toast.makeText(getActivity(),"Please Enter Your Contact Number",Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length()<8){
            Toast.makeText(getActivity(),"Password Must Be At Least 8 Characters",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password) || TextUtils.isEmpty(cpassword)){
            Toast.makeText(getActivity(),"Password Must Be At Least 8 Characters",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(cpassword)){
            Toast.makeText(getActivity(),"Please Enter Confirm Password",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!password.equals(cpassword)){
            Toast.makeText(getActivity(),"Both Passwords Doesn't Match, Please Enter Both Correctly",Toast.LENGTH_SHORT).show();
            return;
        }
        reqisteradminacc();

    }

    //register admin account
    private void reqisteradminacc(){
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        saveFirebaseData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();

                        if(e instanceof FirebaseAuthUserCollisionException){
                            Toast.makeText(getActivity(),"Email already in use, please use another email",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    //save data to firebase
    private void saveFirebaseData(){
        progressDialog.setMessage("Successful!");
        String timestamp = ""+System.currentTimeMillis();

        if(mImageUri==null){
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("uid",""+firebaseAuth.getUid());
            hashMap.put("email",""+email);
            hashMap.put("state",""+ state);
            hashMap.put("name",""+name);
            hashMap.put("phnum",""+phnum);
            hashMap.put("password",""+password);
            hashMap.put("role","admin");
            hashMap.put("profileImage","");

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.child(firebaseAuth.getUid()).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(),"Successfully Added",Toast.LENGTH_SHORT).show();
                            firebaseAuth.signOut();
                            Toast.makeText(getActivity(), "User logged out", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss(); Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    });
        }
        else {
            //save image with info
            String filePathAndName = "Profile_images/"+""+firebaseAuth.getUid();
            //upload image
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
            storageReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());
                            Uri downloadImageUri = uriTask.getResult();

                            if (uriTask.isSuccessful()){
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("uid",""+firebaseAuth.getUid());
                                hashMap.put("email",""+email);
                                hashMap.put("state",""+ state);
                                hashMap.put("name",""+name);
                                hashMap.put("phnum",""+phnum);
                                hashMap.put("password",""+password);
                                hashMap.put("timestamp",""+timestamp);
                                hashMap.put("role","admin");
                                hashMap.put("profileImage",""+downloadImageUri);//url of uploaded image

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                                ref.child(firebaseAuth.getUid()).setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                progressDialog.dismiss();
                                                Toast.makeText(getActivity(),"Successfully Added",Toast.LENGTH_SHORT).show();
                                                firebaseAuth.signOut();
                                                Toast.makeText(getActivity(), "User logged out", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                                startActivity(intent);
                                                getActivity().finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

        }
    }
}