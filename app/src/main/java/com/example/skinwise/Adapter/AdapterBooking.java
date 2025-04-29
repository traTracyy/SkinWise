package com.example.skinwise.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.skinwise.Model.ModelBooking;
import com.example.skinwise.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AdapterBooking extends RecyclerView.Adapter<AdapterBooking.ViewHolder> {

    private List<ModelBooking> bookingList;
    private Context context;

    public AdapterBooking(List<ModelBooking> bookingList) {
        this.bookingList = bookingList;
    }
    public interface OnItemClickListener {
        void onItemClick(ModelBooking booking);
    }
    private OnItemClickListener listener;

    // Constructor modification
    public AdapterBooking(List<ModelBooking> bookingList, OnItemClickListener listener) {
        this.bookingList = bookingList;
        this.listener = listener;
    }

    public void updateData(List<ModelBooking> data) {
        this.bookingList = data;
        notifyDataSetChanged();
    }
    @Override
    public AdapterBooking.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterBooking.ViewHolder holder, int position) {
        ModelBooking booking = bookingList.get(position);
        holder.dateTextView.setText(booking.getDate());
        holder.timeTextView.setText(booking.getTime());
        fetchUserName(booking.getUserUid(), holder.NameTextView);
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }
    private void fetchUserName(String userUid, TextView nameTextView) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userUid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String userName = dataSnapshot.child("name").getValue(String.class);
                    nameTextView.setText(userName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTextView;
        public TextView timeTextView;
        public TextView NameTextView;
        // Declare other views

        public ViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.date);
            timeTextView = itemView.findViewById(R.id.time);
            NameTextView = itemView.findViewById(R.id.username);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        listener.onItemClick(bookingList.get(getAdapterPosition()));
                    }
                }
            });

        }
    }
}

