package com.example.skinwise.Adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skinwise.Model.Messages;
import com.example.skinwise.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Messages> messagesArrayList;
    String senderProfileImageUrl;
    int ITEM_SEND=1;
    int ITEM_RECIEVE=2;

    public MessagesAdapter(Context context, ArrayList<Messages> messagesArrayList, String senderProfileImageUrl) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
        this.senderProfileImageUrl = senderProfileImageUrl;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==ITEM_SEND)
        {
            View view= LayoutInflater.from(context).inflate(R.layout.item_container_sent_message,parent,false);
            return new SenderViewHolder(view);
        }
        else
        {
            View view= LayoutInflater.from(context).inflate(R.layout.item_container_received_message,parent,false);
            return new RecieverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Messages messages = messagesArrayList.get(position);

        if(holder instanceof SenderViewHolder) {
            SenderViewHolder senderViewHolder = (SenderViewHolder) holder;
            senderViewHolder.textViewmessaage.setText(messages.getMessage());
            senderViewHolder.timeofmessage.setText(messages.getCurrenttime());
            if (senderViewHolder.imageProfile != null && senderProfileImageUrl != null) {
                Picasso.get().load(senderProfileImageUrl).placeholder(R.drawable.ic_profile).into(senderViewHolder.imageProfile);
            }
        } else if (holder instanceof RecieverViewHolder) {
            RecieverViewHolder receiverViewHolder = (RecieverViewHolder) holder;
            receiverViewHolder.textViewmessaage.setText(messages.getMessage());
            receiverViewHolder.timeofmessage.setText(messages.getCurrenttime());
            if (receiverViewHolder.imageProfile != null && senderProfileImageUrl != null) {
                Picasso.get().load(senderProfileImageUrl).placeholder(R.drawable.ic_profile).into(receiverViewHolder.imageProfile);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        Messages messages=messagesArrayList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId()))

        {
            return  ITEM_SEND;
        }
        else
        {
            return ITEM_RECIEVE;
        }
    }

    @Override
    public int getItemCount() {
        Log.d("MessagesAdapter", "Messages count: " + messagesArrayList.size());
        return messagesArrayList.size();
    }


    class SenderViewHolder extends RecyclerView.ViewHolder
    {

        TextView textViewmessaage;
        TextView timeofmessage;
        ImageView imageProfile;


        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewmessaage=itemView.findViewById(R.id.textMessage);
            timeofmessage=itemView.findViewById(R.id.textDateTime);
            imageProfile = itemView.findViewById(R.id.imageProfile);
        }
    }

    class RecieverViewHolder extends RecyclerView.ViewHolder
    {

        TextView textViewmessaage;
        TextView timeofmessage;
        ImageView imageProfile;


        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewmessaage=itemView.findViewById(R.id.textMessage);
            timeofmessage=itemView.findViewById(R.id.textDateTime);
            imageProfile = itemView.findViewById(R.id.imageProfile);
        }
    }




}
