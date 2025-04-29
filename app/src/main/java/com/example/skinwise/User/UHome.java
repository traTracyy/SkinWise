package com.example.skinwise.User;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.skinwise.LoginActivity;
import com.example.skinwise.R;
import com.example.skinwise.ml.SkinLesionModelv8;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;


public class UHome extends Fragment {
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int REQUEST_IMAGE_PICK = 102;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private TextView welcometxt,typeTxt,viewtypes,uploadLabel;
    private float[] confidences;
    private ImageView imageView,iconImageView;
    private EditText age;
    private Bitmap image;
    private Button resultbtn,detailsbtn,viewDetailsBtn ;
    private RadioButton radio_anterior_torso,radio_upper_extremity,radio_posterior_torso,radio_lower_extremity,radio_lateral_torso,radio_head_neck, radio_palms_soles, radio_oral_genital;
    private RadioButton male,female;
    private RadioGroup radioGroupAnatomicalSite, radioGroupGender;
    private TensorBuffer inputFeature1;
    private String anatomicalSite,gender,ageText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_u_home, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);

        checkuser();
        welcometxt = view.findViewById(R.id.welcometxt);
        viewtypes = view.findViewById(R.id.viewtypes);

        iconImageView = view.findViewById(R.id.iconImageView);
        uploadLabel = view.findViewById(R.id.uploadLabel);

        imageView = view.findViewById(R.id.imageView);
        resultbtn = view.findViewById(R.id.resultbtn);
        detailsbtn = view.findViewById(R.id.detailsbtn);


        age = view.findViewById(R.id.age);
        male = view.findViewById(R.id.male);
        female = view.findViewById(R.id.female);
        radioGroupAnatomicalSite = view.findViewById(R.id.radio_group_anatomical_site);
        radioGroupGender = view.findViewById(R.id.radio_group_gender);


        inputFeature1 = TensorBuffer.createFixedSize(new int[]{1, 11}, DataType.FLOAT32);

//        radioGroupAnatomicalSite.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                // Convert checkedId to anatomical site index
//                int anatomicalSiteIndex = getAnatomicalSiteIndex(checkedId);
//                // Prepare buffer for inputFeature2
//                ByteBuffer bbAnatomicalSite = ByteBuffer.allocateDirect(4); // float size is 4 bytes
//                bbAnatomicalSite.order(ByteOrder.nativeOrder());
//                bbAnatomicalSite.putFloat(anatomicalSiteIndex);
//                bbAnatomicalSite.rewind();
//            }
//        });
//
//        radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                // Convert checkedId to gender index
//                int genderIndex = checkedId == R.id.male ? 1 : 0; // Assuming male is 1 and female is 0
//                // Prepare buffer for inputFeature3
//                ByteBuffer bbGender = ByteBuffer.allocateDirect(4); // float size is 4 bytes
//                bbGender.order(ByteOrder.nativeOrder());
//                bbGender.putFloat(genderIndex);
//                bbGender.rewind();
//            }
//        });


        // Set onClickListener for the uploadBtn
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        detailsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                UDetails UDetails = new UDetails();
                transaction.replace(R.id.container1, UDetails).commit();
            }
        });
        viewtypes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                UViewTypes uViewTypes = new UViewTypes();
                transaction.replace(R.id.container1, uViewTypes).commit();
            }
        });

        resultbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ageText = age.getText().toString();
                gender = getSelectedGender();
                anatomicalSite = getSelectedAnatomicalSite();
                performClassification();
            }
        });

        return view;
    }
    private void performClassification() {
        if (validateInput()) {
            float ageValue = Float.parseFloat(age.getText().toString());
            int anatomicalSiteIndex = getAnatomicalSiteIndex(radioGroupAnatomicalSite.getCheckedRadioButtonId());
            float genderValue = male.isChecked() ? 1.0f : 0.0f;

            prepareCombinedFeatures(ageValue, anatomicalSiteIndex, genderValue);
            classifyImage();
        } else {
            Toast.makeText(getContext(), "Please fill in all the fields.", Toast.LENGTH_LONG).show();
        }
    }

    private void prepareCombinedFeatures(float age, int anatomicalSiteIndex, float genderIndex) {
        int numFeatures = 11;
        ByteBuffer bb = ByteBuffer.allocateDirect(numFeatures * 4);
        bb.order(ByteOrder.nativeOrder());

        // Load known features
        bb.putFloat(age);
        bb.putFloat(anatomicalSiteIndex);
        bb.putFloat(genderIndex);

        // Fill the rest with zeros if additional features are expected
        for (int i = 3; i < numFeatures; i++) {
            bb.putFloat(0.0f);
        }

        bb.rewind();
        inputFeature1.loadBuffer(bb);
    }
    private int getAnatomicalSiteIndex(int checkedId) {
        switch (checkedId) {
            case R.id.radio_anterior_torso:
                return 0;
            case R.id.radio_upper_extremity:
                return 1;
            case R.id.radio_posterior_torso:
                return 2;
            case R.id.radio_lower_extremity:
                return 3;
            case R.id.radio_lateral_torso:
                return 4;
            case R.id.radio_head_neck:
                return 5;
            case R.id.radio_palms_soles:
                return 6;
            case R.id.radio_oral_genital:
                return 7;
            default:
                return -1; // Error state or default selection
        }
    }
    private boolean validateInput() {
        if (age.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Age is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (radioGroupAnatomicalSite.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getContext(), "Lesion's location not selected", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (radioGroupGender.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getContext(), "Gender not selected", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (this.image == null) {
            Toast.makeText(getContext(), "Image not selected", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    // Method to select an image (from camera or gallery)
    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose an option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                        }
                    }
                    else{
                        requestPermissions(new String[]{android.Manifest.permission.CAMERA},100);
                    }
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    int imageSize = 224;

    // Method to handle the result of the image capture or pick
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && data != null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(imageBitmap);
                this.image = Bitmap.createScaledBitmap(imageBitmap, imageSize, imageSize, false);

                iconImageView.setVisibility(View.GONE);
                uploadLabel.setVisibility(View.GONE);

            }else{
                Uri dat = data.getData();
                Bitmap image = null;
                try {
                    image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),dat);
                }catch (IOException e){
                    e.printStackTrace();
                }
                imageView.setImageBitmap(image);
                this.image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);


                iconImageView.setVisibility(View.GONE);
                uploadLabel.setVisibility(View.GONE);
//                classifyImage(image);
            }
        } else {
            Toast.makeText(getActivity(), "Failed to capture image", Toast.LENGTH_SHORT).show();
            super.onActivityResult(requestCode,resultCode,data);
        }
    }
    private String getSelectedGender() {
        int selectedId = radioGroupGender.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = getView().findViewById(selectedId);
        return (selectedRadioButton != null) ? selectedRadioButton.getText().toString() : "";
    }

    private String getSelectedAnatomicalSite() {
        int selectedId = radioGroupAnatomicalSite.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = getView().findViewById(selectedId);
        return (selectedRadioButton != null) ? selectedRadioButton.getText().toString() : "";
    }

    private void classifyImage() {
        // Ensure image is not null
        if (this.image == null) {
            Toast.makeText(getContext(), "Image not selected", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            SkinLesionModelv8 model = SkinLesionModelv8.newInstance(getContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);

            // Convert image to ByteBuffer
            ByteBuffer imageBuffer = convertBitmapToByteBuffer(this.image);

            inputFeature0.loadBuffer(imageBuffer);


            // Run the model
            SkinLesionModelv8.Outputs outputs = model.process(inputFeature0, inputFeature1);
            this.confidences = outputs.getOutputFeature0AsTensorBuffer().getFloatArray();



            Log.d("ModelOutput", "Confidences: " + Arrays.toString(this.confidences));

            processConfidences();


            model.close();
        } catch (IOException e) {
            Toast.makeText(getContext(), "Error loading model", Toast.LENGTH_SHORT).show();
        }
    }

    private void processConfidences() {
        int maxPos = 0;
        float maxConfidence = 0;
        for (int i = 0; i < this.confidences.length; i++) {
            if (this.confidences[i] > maxConfidence) {
                maxConfidence = this.confidences[i];
                maxPos = i;
            }
        }

        String[] classNames = {
                "Melanoma", "Melanocytic nevus", "Basal cell carcinoma", "Actinic keratosis",
                "Benign keratosis", "Dermatofibroma", "Vascular lesion", "Squamous cell carcinoma"
        };

        String predictedClassName = classNames[maxPos];
        showDialog(predictedClassName);
    }
    private void showDialog(String result) {
        Dialog resultDialog = new Dialog(getContext());
        resultDialog.setContentView(R.layout.dialog_result);

        TextView typeTextView = resultDialog.findViewById(R.id.typetxt);
        typeTextView.setText(result);

        viewDetailsBtn = resultDialog.findViewById(R.id.viewdetialsbtn);
        viewDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass skin lesion type and uploaded image to UDiseaseDetails Fragment
                Bundle bundle = new Bundle();
                bundle.putString("lesionType", result);
                bundle.putFloatArray("confidences", confidences);
                bundle.putParcelable("uploadedImage", image);
                bundle.putString("Age", ageText);
                bundle.putString("Gender", gender);
                bundle.putString("AnatomicalSite", anatomicalSite);


                // Replace current fragment with UDiseaseDetails Fragment
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                UDiseaseDetails uDiseaseDetails = new UDiseaseDetails();
                uDiseaseDetails.setArguments(bundle);
                transaction.replace(R.id.container1, uDiseaseDetails).commit();

                // Dismiss the dialog
                resultDialog.dismiss();
            }
        });

        ImageButton closeButton = resultDialog.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultDialog.dismiss();
            }
        });
        resultDialog.show();
    }


    private ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
        byteBuffer.order(ByteOrder.nativeOrder());
        int[] intValues = new int[imageSize * imageSize];
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        int pixel = 0;
        for (int i = 0; i < imageSize; i++) {
            for (int j = 0; j < imageSize; j++) {
                int val = intValues[pixel++];
                byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255));
                byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255));
                byteBuffer.putFloat((val & 0xFF) * (1.f / 255));
            }
        }
        return byteBuffer;
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