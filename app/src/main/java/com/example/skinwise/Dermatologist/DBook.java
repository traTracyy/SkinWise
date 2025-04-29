package com.example.skinwise.Dermatologist;

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

import com.example.skinwise.Adapter.AdapterBooking;
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

public class DBook extends Fragment {

    private RecyclerView recyclerView;
    private TextView text,bookingtxt;
    private AdapterBooking adapter;
    private List<ModelBooking> bookingList;
    private RelativeLayout filter;
    private String selectedStatus = "Upcoming";
    public DBook() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_d_book, container, false);

        recyclerView = view.findViewById(R.id.Dbook);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        filter = view.findViewById(R.id.filter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStatusFilterDialog();
            }
        });

        bookingList = new ArrayList<>();
        adapter = new AdapterBooking(bookingList, new AdapterBooking.OnItemClickListener() {
            @Override
            public void onItemClick(ModelBooking booking) {
                showBookingDetails(booking);
            }
        });

        recyclerView.setAdapter(adapter);
        text = view.findViewById(R.id.text);
        bookingtxt = view.findViewById(R.id.bookingtxt);

        loadBookings();

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
        // Create a new instance of DBookDetails fragment and pass data
        DBookDetails detailsFragment = new DBookDetails();

        // Assuming you're passing the booking data as a Serializable or Parcelable
        Bundle bundle = new Bundle();
        bundle.putString("booking_date", booking.getDate());
        bundle.putString("booking_time", booking.getTime());
        bundle.putString("user_uid", booking.getUserUid());
        bundle.putString("userName", booking.getUserName());
        bundle.putString("userEmail", booking.getUserEmail());
        bundle.putString("userPhone", booking.getUserphone());
        bundle.putString("BookingID", booking.getBookingID());
        bundle.putString("userProfileImg", booking.getUserProfileImg());
//        bundle.putString("dermName", booking.getDermName());
//        bundle.putString("dermEmail", booking.getDermEmail());
//        bundle.putString("dermAddress", booking.getDermAddress());
//        bundle.putString("dermState", booking.getDermState());
//        bundle.putString("dermShopImg", booking.getDermShopImg());



        detailsFragment.setArguments(bundle);

        // Perform the fragment transaction
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container2, detailsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void loadBookings() {
        String dermUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Bookings");

        databaseRef.orderByChild("dermUid").equalTo(dermUid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        bookingList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ModelBooking booking = snapshot.getValue(ModelBooking.class);
                            if (booking != null) {
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
                        // Handle possible errors
                    }
                });
    }
}