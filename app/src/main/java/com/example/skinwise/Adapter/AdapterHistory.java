package com.example.skinwise.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skinwise.Model.ModelHistory;
import com.example.skinwise.R;
import com.example.skinwise.User.UHisDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.HolderHis> {

    private Context context;
    private ArrayList<ModelHistory> HisList;


    // Constructor
    public AdapterHistory(Context context, ArrayList<ModelHistory> HisList) {
        this.context = context;
        this.HisList = HisList;
    }

    @NonNull
    @Override
    public AdapterHistory.HolderHis onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your custom layout for each item
        View view = LayoutInflater.from(context).inflate(R.layout.urow_history, parent, false);
        return new AdapterHistory.HolderHis(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderHis holderHis, int position) {
        // Get current dermatologist from the list
        ModelHistory modelHistory = HisList.get(position);

        final String uid = modelHistory.getUid();;
        String Hisuid = modelHistory.getHisuid();
        String dateTv = modelHistory.getDate();
        String timeimeTV = modelHistory.getTime();
        String lesionImg = modelHistory.getImageURL();

        holderHis.DateTv.setText(dateTv);
        holderHis.TimeTV.setText(timeimeTV);


        try{
            Picasso.get().load(lesionImg).placeholder(R.drawable.round_corner3).into(holderHis.lesionImg);
        }
        catch (Exception e){
            holderHis.lesionImg.setImageResource(R.drawable.round_corner3);
        }

        holderHis.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String historyUid = HisList.get(position).getHisuid(); // Ensure this gets the right UID
                // Pass the history UID to UHisDetails fragment
                UHisDetails uHisDetails = new UHisDetails();
                Bundle args = new Bundle();
                args.putString("historyUid", historyUid); // Correct key here
                uHisDetails.setArguments(args);

                // Fragment transaction
                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container1, uHisDetails)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return HisList.size();
    }

    class HolderHis extends RecyclerView.ViewHolder {
        private ImageView nextIv,lesionImg;
        private TextView TimeTV,DateTv;


        public HolderHis(@NonNull View itemView) {
            super(itemView);

            DateTv = itemView.findViewById(R.id.DateTv);
            TimeTV = itemView.findViewById(R.id.TimeTv);
            nextIv = itemView.findViewById(R.id.nextIv);
            lesionImg = itemView.findViewById(R.id.lesionImg);

        }
    }
}