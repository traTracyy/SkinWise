package com.example.skinwise.Dermatologist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.skinwise.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class DBookDetails extends Fragment {

    private TextView tvBookingDate, tvBookingTime,tvName,tvPhoneNumber,tvEmail,tvBookingID;

    private Button donebtn;
    private ImageView profile;
    private RelativeLayout Rlayout2;

    private String bookingId;

    public DBookDetails() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_d_book_details, container, false);

        tvBookingDate = view.findViewById(R.id.tvBookingDate);
        tvBookingTime = view.findViewById(R.id.tvBookingTime);
        tvName = view.findViewById(R.id.tvName);
        tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber);
        tvEmail = view.findViewById(R.id.tvEmail);
        profile = view.findViewById(R.id.ivProfileImage);
        donebtn = view.findViewById(R.id.donebtn);
        tvBookingID = view.findViewById(R.id.tvBookingID);

        Rlayout2 = view.findViewById(R.id.Rlayout2);//back
        Bundle bundle = getArguments();
        if (bundle != null) {
            String date = bundle.getString("booking_date");
            String time = bundle.getString("booking_time");
            String userUid = bundle.getString("user_uid");

            String userPhone = bundle.getString("userPhone");
            bookingId = bundle.getString("BookingID");
            String userName = bundle.getString("userName");
            String userEmail = bundle.getString("userEmail");
            String userProfileImg = bundle.getString("userProfileImg");
//            String dermName = bundle.getString("dermName");
//            String dermEmail = bundle.getString("dermEmail");
//            String dermAddress = bundle.getString("dermAddress");
//            String dermState = bundle.getString("dermState");
//            String dermShopImg = bundle.getString("dermShopImg");

            tvBookingDate.setText(date);
            tvBookingTime.setText(time);
            tvName.setText(userName);
            tvPhoneNumber.setText(userPhone);
            tvEmail.setText(userEmail);
            tvBookingID.setText(bookingId);

            if (userProfileImg != null && !userProfileImg.isEmpty()) {
                Picasso.get().load(userProfileImg).placeholder(R.drawable.ic_profile).error(R.drawable.ic_profile).into(profile);
            } else {
                profile.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_profile));
            }


        }
        donebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBookingStatus();
            }
        });
        Rlayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new DBook();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container2,fragment).commit();
            }
        });

        return view;

    }
    private void updateBookingStatus() {

        if (bookingId  == null) {
            Toast.makeText(getActivity(), "Booking ID is missing", Toast.LENGTH_SHORT).show();
            return;
        }
        DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference("Bookings").child(bookingId);

        bookingRef.child("donestatus").setValue("true")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Booking status updated successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Failed to update booking status", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}