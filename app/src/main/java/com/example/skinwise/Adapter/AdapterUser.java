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

import com.example.skinwise.Dermatologist.ChatActivity2;
import com.example.skinwise.Model.ModelUser;
import com.example.skinwise.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.ViewHolder> {

    private Context context;
    private List<ModelUser> userList;

    // Constructor
    public AdapterUser(Context context, List<ModelUser> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.urow_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelUser user = userList.get(position);

        final String uid = user.getUid();

        String nameTv = user.getName();
        String ShopImg = user.getProfileImage();
        String online = user.getOnline();
        if (online.equals("true")){
            holder.online.setVisibility(View.VISIBLE);
        }else {holder.online.setVisibility(View.GONE);}


        holder.nameTv.setText(nameTv);
        try{
            Picasso.get().load(ShopImg).placeholder(R.drawable.ic_profile).into(holder.ShopImg);
        }
        catch (Exception e){
            holder.ShopImg.setImageResource(R.drawable.ic_profile);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatActivity2 ChatActivity2 = new ChatActivity2();

                // Set the arguments for the Fragment
                Bundle args = new Bundle();
                args.putString("derm_id", uid);
                args.putString("derm_name", nameTv);
                ChatActivity2.setArguments(args);

                // Replace the current Fragment with the new one
                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container2, ChatActivity2)
                        .addToBackStack(null)
                        .commit();
            }
        });



    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ShopImg,online;
        private TextView nameTv;
        ;
        // Other views like ImageView, etc.

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.ShopName);
            ShopImg = itemView.findViewById(R.id.ShopImg);
            online = itemView.findViewById(R.id.online);
        }
    }
}
