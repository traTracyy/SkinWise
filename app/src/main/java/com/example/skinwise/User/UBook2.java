package com.example.skinwise.User;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skinwise.Adapter.AdapterBookingUser;
import com.example.skinwise.Constants;
import com.example.skinwise.Model.ModelBooking;
import com.example.skinwise.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UBook2 extends Fragment {

    private RecyclerView recyclerView;
    private AdapterBookingUser adapter;
    private List<ModelBooking> bookingList;
    private RelativeLayout Rlayout2;
    private ProgressDialog progressDialog;
    private RelativeLayout filter;
    private TextView bookingtxt;
    private String selectedStatus = "Upcoming";

    private TextView text;
    public UBook2() {    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_u_book2, container, false);

        filter = view.findViewById(R.id.filter);
        recyclerView = view.findViewById(R.id.bookingsRecyclerView); // Make sure ID matches
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Booking...");
        progressDialog.setCanceledOnTouchOutside(false);
        text = view.findViewById(R.id.text);

        Rlayout2 = view.findViewById(R.id.Rlayout2);//back
        bookingList = new ArrayList<>();
        bookingtxt = view.findViewById(R.id.bookingtxt);

        adapter = new AdapterBookingUser(bookingList, new AdapterBookingUser.OnItemClickListener() {
            @Override
            public void onItemClick(ModelBooking booking) {
                showBookingDetails(booking);
            }
        });

        recyclerView.setAdapter(adapter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStatusFilterDialog();
            }
        });

        Rlayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new UConsult();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container1,fragment).commit();
            }
        });

        loadUserBookings();


        return view;
    }

    private void showStatusFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Status")
                .setItems(Constants.status, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedStatus = Constants.status[which];
                        filterstatuss();
                    }
                }).show();
    }
    private void filterstatuss() {
        List<ModelBooking> filteredList = new ArrayList<>();
        for (ModelBooking booking : bookingList) {
            if (selectedStatus.equals("All")) {
                filteredList.add(booking);
                if (filteredList.isEmpty()) {
                    text.setText("No Bookings");
                }
            } else if (selectedStatus.equals("Completed") && booking.getDonestatus().equals("true")) {
                filteredList.add(booking);
                if (filteredList.isEmpty()) {
                    text.setText("No Completed Bookings");
                }
            } else if (selectedStatus.equals("Upcoming") && booking.getDonestatus().equals("false")) {
                filteredList.add(booking);
                if (filteredList.isEmpty()) {
                    text.setText("No Upcoming Bookings");
                }
            }
        }
        adapter.updateData(filteredList);
        if (selectedStatus.equals("Upcoming")) {
            bookingtxt.setText("Upcoming Bookings");
        } else if (selectedStatus.equals("Completed")) {
            bookingtxt.setText("Completed Bookings");
        } else if (selectedStatus.equals("All")) {
            bookingtxt.setText("All Bookings");
        }
    }



    private void showBookingDetails(ModelBooking booking) {
        UBookDetails detailsFragment = new UBookDetails();

        // Assuming you're passing the booking data as a Serializable or Parcelable
        Bundle bundle = new Bundle();
        bundle.putString("booking_date", booking.getDate());
        bundle.putString("booking_time", booking.getTime());
        bundle.putString("user_uid", booking.getUserUid());
        bundle.putString("BookingID", booking.getBookingID());
        bundle.putString("dermName", booking.getDermName());
        bundle.putString("dermEmail", booking.getDermEmail());
        bundle.putString("dermAddress", booking.getDermAddress());
        bundle.putString("dermState", booking.getDermState());
        bundle.putString("dermShopImg", booking.getDermShopImg());
        bundle.putString("dermPhone", booking.getDermPhone());



        detailsFragment.setArguments(bundle);

        // Perform the fragment transaction
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container1, detailsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void loadUserBookings() {
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Bookings");

        databaseRef.orderByChild("userUid").equalTo(userUid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        bookingList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ModelBooking booking = snapshot.getValue(ModelBooking.class);
                            if (booking != null) { // Check if booking is not null
                                bookingList.add(booking);
                            }
                        }
                        if (bookingList.isEmpty()) {
                            text.setText("No Bookings");
//                            Toast.makeText(getContext(), "No bookings", Toast.LENGTH_LONG).show();
                        }
                        filterstatuss();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
//                        Toast.makeText(getContext(), "Failed to load details", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}