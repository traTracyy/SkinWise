package com.example.skinwise.Dermatologist;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.skinwise.Constants;
import com.example.skinwise.LoginActivity;
import com.example.skinwise.Model.ModelDerm;
import com.example.skinwise.Model.SharedViewModel;
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
import java.util.ArrayList;
import java.util.HashMap;


public class DSetting extends Fragment {


    private ImageView profile;
    private EditText namehint,addresshint,phonenumberhint,addresshint1;
    private Button logoutbtn,updatebtn,chnagepassowrdbtn;
    private SwitchCompat shopswitch;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private static final int REQUEST_CODE = 1;
    private TextView shopStatusTextView;

    private Uri mImageUri;

    private Context context;
    private ArrayList<ModelDerm> dermaList;
    private RelativeLayout report;

    public DSetting() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_d_setting, container, false);



        //define
        logoutbtn = view.findViewById(R.id.logoutbtn);
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


        //define
        profile = view.findViewById(R.id.profile);
        namehint = view.findViewById(R.id.namehint);
        addresshint = view.findViewById(R.id.addresshint);
        addresshint1 = view.findViewById(R.id.addresshint1);
        phonenumberhint = view.findViewById(R.id.phonenumberhint);
        updatebtn = view.findViewById(R.id.updatebtn);
        shopswitch = view.findViewById(R.id.shopswitch);
        shopStatusTextView = view.findViewById(R.id.shopStatusTextView);
        chnagepassowrdbtn = view.findViewById(R.id.chnagepassowrdbtn);
        report = view.findViewById(R.id.report);


        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                DReportIssue dReportIssue = new DReportIssue();
                transaction.replace(R.id.container2, dReportIssue).commit();
            }
        });
        //update function
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputData();

            }
        });
        //change password function
        chnagepassowrdbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                DChangePassword DChangePasswordFragment = new DChangePassword();
                transaction.replace(R.id.container2, DChangePasswordFragment).commit();
            }
        });


        //change profile picture
//        profile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE);
//            }
//        });
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
        shopswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    shopStatusTextView.setText("Open");
                } else {
                    shopStatusTextView.setText("Closed");
                }
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
    //state
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


    private String name,phnum,address,address1;
    private Boolean shopOpen;
    private void inputData(){
        name = namehint.getText().toString().trim();
        phnum = phonenumberhint.getText().toString().trim();
        address = addresshint.getText().toString().trim();
        address1 = addresshint1.getText().toString().trim();
        shopOpen = shopswitch.isChecked();

        if (name.isEmpty() || phnum.isEmpty() || address.isEmpty() || address1.isEmpty()) {
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
            hashMap.put("address",""+address1);
            hashMap.put("shopOpen",""+shopOpen);

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
                                hashMap.put("address",""+address1);
                                hashMap.put("shopOpen",""+shopOpen);
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
                            String address1 = ""+ds.child("address").getValue();
                            String name = ""+ds.child("name").getValue();
                            String shopOpen = ""+ds.child("shopOpen").getValue();
                            String phnum = ""+ds.child("phnum").getValue();
                            String profileImage = ""+ds.child("profileImage").getValue();

                            namehint.setText(name);
                            addresshint.setText(address);
                            phonenumberhint.setText(phnum);
                            addresshint1.setText(address1);

                            if (shopOpen.equals("true")){
                                shopswitch.setChecked(true);
                                shopStatusTextView.setText("Open");
                            }else {
                                shopswitch.setChecked(false);
                                shopStatusTextView.setText("Closed");
                            }

                            if (profileImage != null && !profileImage.isEmpty()) {
                                Picasso.get().load(profileImage).placeholder(R.drawable.ic_profile).error(R.drawable.ic_profile).into(profile);
                            } else {
                                Context context = getContext();
                                if (context != null) { // Check if context is not null
                                    profile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_profile));
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