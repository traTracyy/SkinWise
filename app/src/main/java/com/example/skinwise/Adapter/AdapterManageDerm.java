package com.example.skinwise.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.skinwise.Model.ModelDerm;
import com.example.skinwise.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterManageDerm extends RecyclerView.Adapter<AdapterManageDerm.HolderManageDerm> {

    private Context context;
    private ArrayList<ModelDerm> dermaList;
    private OnDermatologistClickListener listener;

    public interface OnDermatologistClickListener {
        void onDermatologistClick(ModelDerm dermatologist);
    }

    public void setOnDermatologistClickListener(OnDermatologistClickListener listener) {
        this.listener = listener;
    }

    // Constructor
    public AdapterManageDerm(Context context, ArrayList<ModelDerm> dermaList) {
        this.context = context;
        this.dermaList = dermaList;
    }

    @NonNull
    @Override
    public HolderManageDerm onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your custom layout for each item
        View view = LayoutInflater.from(context).inflate(R.layout.urow_derm2, parent, false);
        return new HolderManageDerm(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderManageDerm holder, int position) {
        // Get current dermatologist from the list
        ModelDerm modelDerm = dermaList.get(position);
        final String uid = modelDerm.getUid();
        String nameTv = modelDerm.getName();
        String location = modelDerm.getState();
        String ShopImg = modelDerm.getProfileImage();

        holder.nameTv.setText(nameTv);
        holder.location.setText(location);

        try{
            Picasso.get().load(ShopImg).placeholder(R.drawable.ic_profile).into(holder.ShopImg);
        }
        catch (Exception e){
            holder.ShopImg.setImageResource(R.drawable.ic_profile);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDermatologistClick(modelDerm);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dermaList.size();
    }

    class HolderManageDerm extends RecyclerView.ViewHolder {
        private ImageView ShopImg;
        private TextView nameTv,location;

        public HolderManageDerm(@NonNull View itemView) {
            super(itemView);
            ShopImg = itemView.findViewById(R.id.ShopImg);
            nameTv = itemView.findViewById(R.id.ShopName);
            location = itemView.findViewById(R.id.location);
        }
    }
}
