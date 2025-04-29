package com.example.skinwise.User;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.skinwise.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class UDiseaseDetails extends Fragment {
    private TextView lesionTypeTextView,Melanoma,MV,BCC,AK,BK,DF,VL,SCC,gendertxt,locationtxt,agetxt;
    private LinearLayout confidenceLayout;
    private ImageView imageView;

    private Button saveResultButton, viewDetailsButton;
    private FirebaseAuth firebaseAuth;

    private RelativeLayout Rlayout2;
    public UDiseaseDetails() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_u_disease_details, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        lesionTypeTextView = view.findViewById(R.id.lesion_type);
        confidenceLayout = view.findViewById(R.id.confidence_layout);
        imageView = view.findViewById(R.id.imageView);
        saveResultButton = view.findViewById(R.id.saveresult);
        viewDetailsButton = view.findViewById(R.id.viewdbtn);

        agetxt = view.findViewById(R.id.agetxt);
        gendertxt = view.findViewById(R.id.gendertxt);
        locationtxt = view.findViewById(R.id.locationtxt);

        Rlayout2 = view.findViewById(R.id.Rlayout2);//back

        Rlayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new UHome();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container1,fragment).commit();
            }
        });
        Melanoma = view.findViewById(R.id.Melanoma);
        MV = view.findViewById(R.id.MV);
        BCC = view.findViewById(R.id.BCC);
        AK = view.findViewById(R.id.AK);
        BK = view.findViewById(R.id.BK);
        DF = view.findViewById(R.id.DF);
        VL = view.findViewById(R.id.VL);
        SCC = view.findViewById(R.id.SCC);


        Bundle bundle = getArguments();
        if (bundle != null) {
            String lesionType = bundle.getString("lesionType");
            float[] confidences = bundle.getFloatArray("confidences");
            Bitmap image = bundle.getParcelable("uploadedImage");
            String AnatomicalSite = bundle.getString("AnatomicalSite");
            String Gender = bundle.getString("Gender");
            String Age = bundle.getString("Age");

            // Set lesion type
            lesionTypeTextView.setText(lesionType);

            locationtxt.setText(AnatomicalSite);
            gendertxt.setText(Gender);
            agetxt.setText(Age);

            // Display confidence levels
            displayConfidenceLevels(confidences);

            // Set uploaded image
            imageView.setImageBitmap(image);
        }
        saveResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get lesion type from TextView
                String lesionType = lesionTypeTextView.getText().toString().trim();
                String AnatomicalSite = locationtxt.getText().toString().trim();
                String Gender = gendertxt.getText().toString().trim();
                String Age = agetxt.getText().toString().trim();

                float[] confidences = bundle.getFloatArray("confidences");

                Bitmap image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                // Pass data to saveResultToFirebase function
                saveResultToFirebase(lesionType,AnatomicalSite,Gender,Age,confidences, image);
            }
        });

        viewDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lesionType = lesionTypeTextView.getText().toString().trim();

                USkinLesion uSkinLesion = new USkinLesion();
                Bundle args = new Bundle();
                args.putString("lesionType", lesionType);
                uSkinLesion.setArguments(args);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container1, uSkinLesion).commit();
            }
        });




        return view;
    }

    private void displayConfidenceLevels(float[] confidences) {
        confidenceLayout.removeAllViews();

        // Add confidence levels dynamically
        String[] classNames = {
                "Melanoma", "Melanocytic nevus", "Basal cell carcinoma", "Actinic keratosis",
                "Benign keratosis", "Dermatofibroma", "Vascular lesion", "Squamous cell carcinoma"
        };

        for (int i = 0; i < confidences.length; i++) {
            LinearLayout layout = new LinearLayout(getContext());
            layout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            layout.setOrientation(LinearLayout.HORIZONTAL);

            TextView textView1 = new TextView(getContext());
            textView1.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            textView1.setText(classNames[i] + ": ");
            textView1.setTextColor(ContextCompat.getColor(getContext(), R.color.darker));
            textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            textView1.setTypeface(null, Typeface.BOLD);

            TextView textView2 = new TextView(getContext());
            textView2.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            textView2.setText(String.format("%.2f%%", confidences[i] * 100));
            textView2.setTextColor(ContextCompat.getColor(getContext(), R.color.darker));
            textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

            layout.addView(textView1);
            layout.addView(textView2);

            confidenceLayout.addView(layout);
        }
    }

    private void saveResultToFirebase(String lesionType,String AnatomicalSite,String Gender,String Age, float[] confidences, Bitmap image) {
        if (image == null) {
            Toast.makeText(getContext(), "Image not selected", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get current date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String date = dateFormat.format(new Date());
        String time = timeFormat.format(new Date());

        // Upload image to Firebase Storage
        String filePathAndName = "SkinLesionImages/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + System.currentTimeMillis();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        storageReference.putBytes(data)
                .addOnSuccessListener(taskSnapshot -> {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    uriTask.addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();

                        // Save result data to Firebase Realtime Database
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("History").push();

                        String historyUid = databaseReference.getKey();
                        // Create a HashMap to store the result data
                        HashMap<String, Object> resultData = new HashMap<>();
                        resultData.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid()); // Add user's UID
                        resultData.put("lesionType", lesionType);
                        resultData.put("imageURL", imageUrl);
                        resultData.put("date", date); // Add date
                        resultData.put("time", time); // Add time
                        resultData.put("hisuid", historyUid);
                        resultData.put("AnatomicalSite", AnatomicalSite);
                        resultData.put("Gender", Gender);
                        resultData.put("Age", Age);


                        // Populate SL1 to SL8 with confidence values
                        for (int i = 0; i < Math.min(confidences.length, 8); i++) {
                            resultData.put("SL" + (i + 1), String.format("%.10f", confidences[i])); // Format confidence value as string

                        }

                        // Save the result data to Firebase
                        databaseReference.setValue(resultData)
                                .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Result saved successfully", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to save result: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    });
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }


}