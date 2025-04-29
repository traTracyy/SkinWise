package com.example.skinwise.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skinwise.Model.ModelDerm;
import com.example.skinwise.R;

import java.util.List;

public class AdapterManageDerma  extends RecyclerView.Adapter<AdapterManageDerma.ViewHolder> {

    private Context context;
    private List<ModelDerm> userList;

    public interface UserClickListener {
        void onUserClicked(ModelDerm user);
    }

    private AdapterManageDerma.UserClickListener listener;
    public void setUserClickListener(AdapterManageDerma.UserClickListener userClickListener) {
        this.listener = userClickListener;
    }


    public AdapterManageDerma(Context context, List<ModelDerm> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public AdapterManageDerma.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_derm, parent, false);
        return new AdapterManageDerma.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterManageDerma.ViewHolder holder, int position) {
        ModelDerm user = userList.get(position);


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