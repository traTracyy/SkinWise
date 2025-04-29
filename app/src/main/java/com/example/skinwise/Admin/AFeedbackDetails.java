package com.example.skinwise.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.skinwise.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AFeedbackDetails extends Fragment {

    private RelativeLayout Rlayout2;
    private ImageView ivLargeImage;
    private TextView UserId, reportId,dateTime,contactInfo,description,image;
    private LinearLayout imageContainer;
    public AFeedbackDetails() {    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a_feedback_details, container, false);

        ivLargeImage = view.findViewById(R.id.ivLargeImage);
        UserId = view.findViewById(R.id.UserId);
        reportId = view.findViewById(R.id.reportId);
        dateTime = view.findViewById(R.id.dateTime);
        contactInfo = view.findViewById(R.id.contactInfo);
        description = view.findViewById(R.id.description);
        imageContainer = view.findViewById(R.id.llImagesContainer);
        image = view.findViewById(R.id.image);

        Rlayout2 = view.findViewById(R.id.Rlayout2);
        //back to feedback
        Rlayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new AFeedback();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container3,fragment).commit();
            }
        });


        Bundle args = getArguments();
        if (args != null) {
            String contactInfo1 = args.getString("contactInfo", "No Contact Information");
            List<String> images = args.getStringArrayList("images");

            if (images == null || images.isEmpty()) {
                image.setText("No images uploaded by user");
            } else {
                for (String imageUrl : images) {
                    addImageToContainer(imageUrl, imageContainer, ivLargeImage);
                }
            }

            String userId1 = args.getString("userId", "No User ID"); // Using default value if not found
            String description1 = args.getString("description", "No Description");
            String reportId1 = args.getString("reportId", "No ReportID");
            String dateTime1 = args.getString("dateTime", "No Date/Time");


            UserId.setText(userId1);
            description.setText(description1);
            reportId.setText(reportId1);
            dateTime.setText(dateTime1);
            contactInfo.setText(contactInfo1);
        }

        return view;
    }



    private void addImageToContainer(String imageUrl, LinearLayout container, ImageView largeImageView) {
        ImageView imageView = new ImageView(getContext());
        imageView.setLayoutParams(new LinearLayout.LayoutParams(100, 100)); // Adjust size as needed
        Picasso.get().load(imageUrl).into(imageView);

        imageView.setOnClickListener(v -> {
            Picasso.get().load(imageUrl).into(largeImageView);
            largeImageView.setVisibility(View.VISIBLE);
        });

        container.addView(imageView);
    }


}

