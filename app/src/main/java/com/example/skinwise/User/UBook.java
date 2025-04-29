package com.example.skinwise.User;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.skinwise.Model.ModelDerm;
import com.example.skinwise.Model.ModelUser;
import com.example.skinwise.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class UBook extends Fragment {

    private Button confirmButton;
    private String dermUid, userUid;

    private DatabaseReference dermRef;
    private TimePicker timePicker;
    private DatePicker datePicker;
    private RelativeLayout Rlayout2;

    Calendar calendar;

    public UBook() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_u_book, container, false);

        if (getArguments() != null) {
            dermUid = getArguments().getString("derm_id");
            userUid = getArguments().getString("user_id");
        }
        dermRef = FirebaseDatabase.getInstance().getReference("Users").child(dermUid);

        datePicker = view.findViewById(R.id.datePicker);
        timePicker = view.findViewById(R.id.timePicker);
        confirmButton = view.findViewById(R.id.confirmBookingButton);

        timePicker.setIs24HourView(true);
        calendar = Calendar.getInstance();

        Rlayout2 = view.findViewById(R.id.Rlayout2);//back
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        datePicker.setMinDate(calendar.getTimeInMillis());

        Rlayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new UConsult();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container1,fragment).commit();
            }
        });
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                if (hourOfDay < 10) {
                    timePicker.setCurrentHour(10);
                } else if (hourOfDay >= 18) {
                    timePicker.setCurrentHour(17);
                }
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkShopStatusAndBook();
//                int day = datePicker.getDayOfMonth();
//                int month = datePicker.getMonth();
//                int year = datePicker.getYear();
//                int hour = timePicker.getCurrentHour();
//                int minute = timePicker.getCurrentMinute();
//
//                Calendar selectedDate = Calendar.getInstance();
//                selectedDate.set(year, month, day, hour, minute);
//
//                // Format the date and time as strings
//                String date = String.format("%d-%d-%d", day, month + 1, year);
//                String time = String.format("%02d:%02d", hour, minute);
//
//                // Call saveBooking with the selected date and time
//                saveBooking(date, time);
            }
        });


        return view;
    }
    private void checkShopStatusAndBook() {
        dermRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String shopOpen = dataSnapshot.child("shopOpen").getValue(String.class);
                    if ("true".equals(shopOpen)) {
                        proceedWithBooking();
                    } else {
                        Toast.makeText(getActivity(), "The clinic is currently closed.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Failed to check clinic status", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void proceedWithBooking() {
        // Format the date and time as strings
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();
        String date = String.format("%d-%d-%d", day, month + 1, year);
        String time = String.format("%02d:%02d", hour, minute);

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userUid);
        DatabaseReference dermRef = FirebaseDatabase.getInstance().getReference("Users").child(dermUid);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ModelUser user = dataSnapshot.getValue(ModelUser.class);
                    String userName = user.getName();
                    String userEmail = user.getEmail();
                    String userProfileImg = user.getProfileImage();
                    String userphone = user.getPhnum();

                    dermRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                ModelDerm derm = dataSnapshot.getValue(ModelDerm.class);
                                String dermName = derm.getName();
                                String dermEmail = derm.getEmail();
                                String dermPhone = derm.getPhnum();
                                String dermAddress = derm.getAddress();
                                String dermState = derm.getState();
                                String dermShopImg = derm.getProfileImage();

                                saveBooking(date, time, userName, userEmail, userProfileImg,userphone, dermName, dermEmail, dermPhone, dermAddress, dermState, dermShopImg);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getActivity(), "Failed to retrieve dermatologist details", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Failed to retrieve user details", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void saveBooking(String date, String time, String userName, String userEmail, String userProfileImg, String userphone, String dermName, String dermEmail, String dermPhone, String dermAddress, String dermState, String dermShopImg) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Bookings");

        String bookingId = databaseRef.push().getKey();
        Map<String, Object> booking = new HashMap<>();

        booking.put("bookingID", bookingId);
        booking.put("dermUid", dermUid);
        booking.put("userUid", userUid);
        booking.put("date", date);
        booking.put("time", time);
        booking.put("userName", userName);
        booking.put("userEmail", userEmail);
        booking.put("userProfileImg", userProfileImg);
        booking.put("userPhone", userphone);
        booking.put("dermName", dermName);
        booking.put("dermEmail", dermEmail);
        booking.put("dermPhone", dermPhone);
        booking.put("dermAddress", dermAddress);
        booking.put("dermState", dermState);
        booking.put("dermShopImg", dermShopImg);
        booking.put("donestatus", "false");

        databaseRef.child(bookingId).setValue(booking)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Successfully Booked", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Unsuccessful, Please Try Again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}