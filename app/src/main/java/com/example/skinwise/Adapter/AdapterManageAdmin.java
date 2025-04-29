package com.example.skinwise.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skinwise.Model.ModelAdmin;
import com.example.skinwise.R;

import java.util.List;

public class AdapterManageAdmin  extends RecyclerView.Adapter<AdapterManageAdmin.ViewHolder> {
    private Context context;
    private List<ModelAdmin> userList;

    public interface UserClickListener {
        void onUserClicked(ModelAdmin user);
    }

    private AdapterManageAdmin.UserClickListener listener;

    public void setUserClickListener(AdapterManageAdmin.UserClickListener listener) {
        this.listener = listener;
    }
    public AdapterManageAdmin(Context context, List<ModelAdmin> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public AdapterManageAdmin.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin, parent, false);
        return new AdapterManageAdmin.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull AdapterManageAdmin.ViewHolder holder, int position) {
        ModelAdmin user = userList.get(position);


        holder.textemail.setText(user.getEmail());
        holder.textrole.setText(user.getPhnum());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onUserClicked(userList.get(position));
                }
            }
        });
    }

        @Override
        public int getItemCount() {
            return userList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView textrole, textemail;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textrole = itemView.findViewById(R.id.role);
                textemail = itemView.findViewById(R.id.email);
            }
        }
    }




