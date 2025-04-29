package com.example.skinwise.Dermatologist;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.skinwise.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DReportIssue extends Fragment {

    private RelativeLayout Rlayout2;
    private EditText editTextIssueDescription, editTextContactInfo;
    private Button buttonAttachScreenshot, submitissuebutton;
    private FirebaseAuth firebaseAuth;
    private static final int REQUEST_CODE = 1;
    private Uri imageUri;
    private List<Uri> imageUris = new ArrayList<>();

    public DReportIssue() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_d_report_issue, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        editTextIssueDescription = view.findViewById(R.id.editTextIssueDescription);
        buttonAttachScreenshot = view.findViewById(R.id.buttonAttachScreenshot);
        editTextContactInfo = view.findViewById(R.id.editTextContactInfo);
        submitissuebutton = view.findViewById(R.id.submitissuebutton);
        Rlayout2 = view.findViewById(R.id.Rlayout2);

        //back to setting page
        Rlayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new DSetting();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container2, fragment).commit();
            }
        });

        buttonAttachScreenshot.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Pictures"), REQUEST_CODE);
        });


        submitissuebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitIssue();
            }
        });

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            LinearLayout imageContainer = getView().findViewById(R.id.imageContainer);

            List<Uri> newImageUris = new ArrayList<>();

            if (data.getClipData() != null) {
                ClipData clipData = data.getClipData();
                int newCount = clipData.getItemCount();
                for (int i = 0; i < newCount; i++) {
                    Uri newImageUri = clipData.getItemAt(i).getUri();
                    if (!imageUris.contains(newImageUri) && imageUris.size() < 3) {
                        newImageUris.add(newImageUri);
                        ImageView imageView = createImageView(newImageUri);
                        imageContainer.addView(imageView);
                    }
                }
            } else if (data.getData() != null) {
                Uri newImageUri = data.getData();
                if (!imageUris.contains(newImageUri) && imageUris.size() < 3) {
                    newImageUris.add(newImageUri);
                    ImageView imageView = createImageView(newImageUri);
                    imageContainer.addView(imageView);
                }
            }

            // Update the main Uri list with new selections
            imageUris.addAll(newImageUris);

            Log.d("ReportIssue", "Total selected images: " + imageUris.size());
            imageContainer.requestLayout();
        }
    }

    private void addImageToContainer(Uri imageUri, LinearLayout imageContainer) {
        ImageView imageView = createImageView(imageUri);
        imageContainer.addView(imageView); // Add ImageView to LinearLayout
    }

    private ImageView createImageView(Uri imageUri) {
        Log.d("ReportIssue", "Creating view for image: " + imageUri.toString());
        ImageView imageView = new ImageView(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                300, // width in pixels
                ViewGroup.LayoutParams.MATCH_PARENT // height
        );
        layoutParams.setMargins(8, 8, 8, 8); // Add margins to prevent overlapping
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.get().load(imageUri).into(imageView);
        return imageView;
    }

    private void submitIssue() {
        final String issueDescription = editTextIssueDescription.getText().toString().trim();
        final String contactInfo = editTextContactInfo.getText().toString().trim();
        final String userId = firebaseAuth.getCurrentUser() != null ? firebaseAuth.getCurrentUser().getUid() : "Anonymous";

        if (issueDescription.isEmpty()) {
            editTextIssueDescription.setError("Please describe the issue");
            editTextIssueDescription.requestFocus();
            return;
        }

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Report");
        final String reportId = databaseReference.push().getKey();

        // Initialize report object with non-image data
        final Map<String, Object> report = new HashMap<>();
        report.put("userId", userId);
        report.put("description", issueDescription);
        report.put("contactInfo", contactInfo.isEmpty() ? "No contact information" : contactInfo);
        report.put("reportId", reportId);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());
        report.put("dateTime", currentDateAndTime);

        if (!imageUris.isEmpty()) {
            uploadImages(new OnImagesUploadedListener() {
                @Override
                public void onUploaded(List<String> imageUrls) {
                    report.put("images", imageUrls);
                    saveReportToFirebase(reportId, report);
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(getActivity(), "Failed to upload images", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            report.put("images", new ArrayList<String>());
            saveReportToFirebase(reportId, report);
        }
    }

    private void uploadImages(final OnImagesUploadedListener listener) {
        final List<String> imageUrls = new ArrayList<>();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("report_images");

        for (Uri imageUri : imageUris) {
            final StorageReference imageRef = storageRef.child("img_" + System.currentTimeMillis() + ".jpg");
            imageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageUrls.add(uri.toString());
                            if (imageUrls.size() == imageUris.size()) {
                                // All images have been uploaded
                                listener.onUploaded(imageUrls);
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    listener.onError(e);
                }
            });
        }
    }

    private void saveReportToFirebase(String reportId, Map<String, Object> report) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Report");
        databaseReference.child(reportId).setValue(report)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Report submitted successfully", Toast.LENGTH_SHORT).show();
                        // Reset fields or navigate away
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Failed to submit report", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Listener interface for image uploads
    private interface OnImagesUploadedListener {
        void onUploaded(List<String> imageUrls);

        void onError(Exception e);
    }

    // Call this method when images are selected
    private void addImageUriToList(Uri imageUri) {
        if (imageUris.size() < 3) {
            imageUris.add(imageUri);
        } else {
            Toast.makeText(getActivity(), "You can only upload up to 3 images", Toast.LENGTH_SHORT).show();
        }
    }
}
