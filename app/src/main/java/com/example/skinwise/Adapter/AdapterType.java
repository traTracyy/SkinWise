package com.example.skinwise.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skinwise.Model.ModelType;
import com.example.skinwise.R;
import com.example.skinwise.User.USkinLesion2;

import java.util.ArrayList;

public class AdapterType extends RecyclerView.Adapter<AdapterType.HolderType>{
    private ArrayList<ModelType> lesionList;
    private Context context;
    private OnLesionClickListener listener;
    public AdapterType(Context context, ArrayList<ModelType> lesionList) {
        this.context = context;
        this.lesionList = lesionList;
    }
    @NonNull
    @Override
    public HolderType onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your custom layout for each item
        View view = LayoutInflater.from(context).inflate(R.layout.item_skintype, parent, false);
        return new HolderType(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterType.HolderType holder, int position) {
        ModelType lesion = lesionList.get(position);
        String nameTv = lesion.getName();
        holder.myTextView.setText(nameTv);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onLesionClick(lesionList.get(holder.getAdapterPosition()));
                }
                USkinLesion2 uSkinLesion2 = new USkinLesion2();

                // Set the arguments for the Fragment
                Bundle args = new Bundle();
                args.putString("lesionType", nameTv);
                uSkinLesion2.setArguments(args);

                // Replace the current Fragment with the new one
                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container1, uSkinLesion2)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
    public interface OnLesionClickListener {
        void onLesionClick(ModelType lesion);
    }
    public void setOnLesionClickListener(OnLesionClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return lesionList.size();
    }




    class HolderType extends RecyclerView.ViewHolder {
        TextView myTextView;

        public HolderType(@NonNull View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.type);

        }
    }

}
