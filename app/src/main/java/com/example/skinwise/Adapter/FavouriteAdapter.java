package com.example.skinwise.Adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.skinwise.R;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {

    private List<String> favourites;
    private OnFavouriteItemClickListener listener;

    public FavouriteAdapter(List<String> favourites, OnFavouriteItemClickListener listener) {
        this.favourites = favourites;
        this.listener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.urow_favourite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String lesionType = favourites.get(position);
        holder.lesionTypeTextView.setText(lesionType);

//        holder.itemView.setOnClickListener(v -> listener.onItemClick(lesionType));
        holder.itemView.setOnClickListener(v -> {
            // Get lesionType directly from the TextView
            String clickedLesionType = holder.lesionTypeTextView.getText().toString();
            listener.onItemClick(clickedLesionType);
        });
    }

    @Override
    public int getItemCount() {
        return favourites.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView lesionTypeTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            lesionTypeTextView = itemView.findViewById(R.id.lesionTypeTextView);
        }
    }
    public interface OnFavouriteItemClickListener {
        void onItemClick(String lesionType);
    }

}
