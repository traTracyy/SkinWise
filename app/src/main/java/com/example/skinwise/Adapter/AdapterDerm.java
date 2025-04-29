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

import com.example.skinwise.User.ChatActivity;
import com.example.skinwise.Model.ModelDerm;
import com.example.skinwise.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterDerm extends RecyclerView.Adapter<AdapterDerm.HolderDerm> {

    private Context context;
    private ArrayList<ModelDerm> dermaList;

//    private final UserListener userListener;


    // Constructor
    public AdapterDerm(Context context, ArrayList<ModelDerm> dermaList) {
        this.context = context;
        this.dermaList = dermaList;
    }

    @NonNull
    @Override
    public HolderDerm onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your custom layout for each item
        View view = LayoutInflater.from(context).inflate(R.layout.urow_derm, parent, false);
        return new HolderDerm(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderDerm holder, int position) {
        // Get current dermatologist from the list
        ModelDerm modelDerm = dermaList.get(position);
        final String uid = modelDerm.getUid();
        String email = modelDerm.getEmail();
        String nameTv = modelDerm.getName();
        String location = modelDerm.getState();
        String online = modelDerm.getOnline();
        String ShopImg = modelDerm.getProfileImage();
        String shopOpen = modelDerm.getShopOpen();
        String role = modelDerm.getRole();


        holder.nameTv.setText(nameTv);
        holder.location.setText(location);

        if (online.equals("true")){
            holder.online.setVisibility(View.VISIBLE);
        }else {holder.online.setVisibility(View.GONE);}
        if (shopOpen.equals("true")){
            holder.Close.setVisibility(View.GONE);
        }else {holder.Close.setVisibility(View.VISIBLE);}

        try{
            Picasso.get().load(ShopImg).placeholder(R.drawable.ic_profile).into(holder.ShopImg);
        }
        catch (Exception e){
            holder.ShopImg.setImageResource(R.drawable.ic_profile);
        }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChatActivity ChatActivity = new ChatActivity();

                    // Set the arguments for the Fragment
                    Bundle args = new Bundle();
                    args.putString("derm_id", uid);
                    args.putString("derm_name", nameTv);
                    ChatActivity.setArguments(args);

                    // Replace the current Fragment with the new one
                    FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container1, ChatActivity)
                            .addToBackStack(null)
                            .commit();
                }
            });



    }

    @Override
    public int getItemCount() {
        return dermaList.size();
    }

    class HolderDerm extends RecyclerView.ViewHolder {
        private ImageView ShopImg,online;
        private TextView nameTv,Close,location;
;

        public HolderDerm(@NonNull View itemView) {
            super(itemView);

            ShopImg = itemView.findViewById(R.id.ShopImg);
            online = itemView.findViewById(R.id.online);
            nameTv = itemView.findViewById(R.id.ShopName);
            Close = itemView.findViewById(R.id.Close);
            location = itemView.findViewById(R.id.location);

        }
    }
}
