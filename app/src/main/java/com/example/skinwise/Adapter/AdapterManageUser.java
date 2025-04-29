package com.example.skinwise.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skinwise.Model.ModelUser;
import com.example.skinwise.R;

import java.util.List;

public class AdapterManageUser extends RecyclerView.Adapter<AdapterManageUser.ViewHolder> {

    private Context context;
    private List<ModelUser> userList;
    public interface UserClickListener {
        void onUserClicked(ModelUser user);
    }

    private UserClickListener listener;

    public void setUserClickListener(UserClickListener listener) {
        this.listener = listener;
    }



    public AdapterManageUser(Context context, List<ModelUser> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelUser user = userList.get(position);


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
