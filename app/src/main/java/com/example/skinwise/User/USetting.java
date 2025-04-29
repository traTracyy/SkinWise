package com.example.skinwise.User;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.skinwise.Constants;
import com.example.skinwise.LoginActivity;
import com.example.skinwise.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;


public class USetting extends Fragment {

    private ImageView profile;
    private EditText namehint,addresshint,phonenumberhint;
    private Button logoutbtn,updatebtn,chnagepassowrdbtn;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private static final int REQUEST_CODE = 1;
    private RelativeLayout report;

    private Uri mImageUri;


    public USetting() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_u_setting, container, false);

        //define
        logoutbtn = view.findViewById(R.id.logoutbtn);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);

        checkuser();


        //define
        profile = view.findViewById(R.id.profile);
        namehint = view.findViewById(R.id.namehint);
        addresshint = view.findViewById(R.id.addresshint);
        phonenumberhint = view.findViewById(R.id.phonenumberhint);
        updatebtn = view.findViewById(R.id.updatebtn);
        chnagepassowrdbtn = view.findViewById(R.id.chnagepassowrdbtn);
        report = view.findViewById(R.id.report);


        //update function
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputData();

            }
        });
        chnagepassowrdbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                UChangePassword UChangePasswordFragment = new UChangePassword();
                transaction.replace(R.id.container1, UChangePasswordFragment).commit();
            }
        });
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                UReportIssue uReportIssue = new UReportIssue();
                transaction.replace(R.id.container1, uReportIssue).commit();
            }
        });


        //change profile picture
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE);
            }
        });

        //logout function
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toOffline();
            }
        });
        addresshint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stateDialog();
            }
        });

        return view;
    }

    private void stateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose Your State").setItems(Constants.state, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String selectedState = Constants.state[which];
                addresshint.setText(selectedState);
            }
        }).show();
    }

    //account to choose image
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


    //change to offline status
    private void toOffline(){
        progressDialog.setMessage("Logging Out...");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("online","false");

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        firebaseAuth.signOut();
                        checkuser();
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
    //identify user
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


    private String name,phnum,address;
    private void inputData(){
        name = namehint.getText().toString().trim();
        phnum = phonenumberhint.getText().toString().trim();
        address = addresshint.getText().toString().trim();

        if (name.isEmpty() || phnum.isEmpty() || address.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else {
            updateProfile();
        }

    }

    private void updateProfile(){
        progressDialog.setMessage("Updating...");
        progressDialog.show();

        if (mImageUri == null){
            HashMap<String, Object> hashMap = new HashMap<>();

            hashMap.put("name",""+name);
            hashMap.put("phnum",""+phnum);
            hashMap.put("state",""+address);

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.child(firebaseAuth.getUid()).updateChildren(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(),"Profile Updated",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    });
        }else { //save image with info
            String filePathAndName = "Profile_images/"+""+firebaseAuth.getUid();
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
                                hashMap.put("name",""+name);
                                hashMap.put("phnum",""+phnum);
                                hashMap.put("state",""+address);
                                hashMap.put("profileImage",""+downloadImageUri);

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                                ref.child(firebaseAuth.getUid()).updateChildren(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                progressDialog.dismiss();
                                                Toast.makeText(getActivity(),"Profile Updated",Toast.LENGTH_SHORT).show();
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


    //load info
    private void loadInfo(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            String uid = ""+ds.child("uid").getValue();
                            String role = ""+ds.child("role").getValue();
                            String email = ""+ds.child("email").getValue();
                            String timestamp = ""+ds.child("timestamp").getValue();
                            String address = ""+ds.child("state").getValue();
                            String name = ""+ds.child("name").getValue();
                            String phnum = ""+ds.child("phnum").getValue();
                            String profileImage = ""+ds.child("profileImage").getValue();

                            namehint.setText(name);
                            addresshint.setText(address);
                            phonenumberhint.setText(phnum);

                            if (profileImage != null && !profileImage.isEmpty()) {
                                Picasso.get().load(profileImage).placeholder(R.drawable.ic_profile).error(R.drawable.ic_profile).into(profile);
                            } else {
                                if (isAdded()) { // Check if the fragment is currently added to its activity
                                    profile.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_profile));
                                }
                            }



                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}