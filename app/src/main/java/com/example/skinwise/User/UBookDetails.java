package com.example.skinwise.User;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.skinwise.R;
import com.squareup.picasso.Picasso;

public class UBookDetails extends Fragment {


    private TextView tvBookingDate, tvAddress,tvState,tvBookingTime,tvName,tvPhoneNumber,tvEmail,tvBookingID;

    private ImageView profile;

    private String bookingId;
    private RelativeLayout Rlayout2;

    public UBookDetails() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_u_book_details, container, false);

        tvBookingDate = view.findViewById(R.id.tvBookingDate);
        tvBookingTime = view.findViewById(R.id.tvBookingTime);
        tvName = view.findViewById(R.id.tvName);
        tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber);
        tvEmail = view.findViewById(R.id.tvEmail);
        profile = view.findViewById(R.id.ivProfileImage);
        tvBookingID = view.findViewById(R.id.tvBookingID);

        tvAddress = view.findViewById(R.id.tvAddress);
        tvState = view.findViewById(R.id.tvState);

        Rlayout2 = view.findViewById(R.id.Rlayout2);//back
        Bundle bundle = getArguments();
        if (bundle != null) {
            String date = bundle.getString("booking_date");
            String time = bundle.getString("booking_time");
            String userUid = bundle.getString("user_uid");
            bookingId = bundle.getString("BookingID");
            Log.d("UBookDetails", "Received ID: " + bookingId);


            String userPhone = bundle.getString("dermPhone");
            String userName = bundle.getString("dermName");
            String userEmail = bundle.getString("dermEmail");
            String dermAddress = bundle.getString("dermAddress");
            String dermState = bundle.getString("dermState");
            String userProfileImg = bundle.getString("dermShopImg");
            tvBookingDate.setText(date);
            tvBookingTime.setText(time);
            tvName.setText(userName);
            tvPhoneNumber.setText(userPhone);
            tvEmail.setText(userEmail);
            tvAddress.setText(dermAddress);
            tvState.setText(dermState);
            tvBookingID.setText(bookingId);

            if (userProfileImg != null && !userProfileImg.isEmpty()) {
                Picasso.get().load(userProfileImg).placeholder(R.drawable.ic_profile).error(R.drawable.ic_profile).into(profile);
            } else {
                profile.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_profile));
            }

        }
        Rlayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new UBook2();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container1,fragment).commit();
            }
        });
        return view;
    }
}