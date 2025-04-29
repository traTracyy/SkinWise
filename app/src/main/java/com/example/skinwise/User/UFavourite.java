package com.example.skinwise.User;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skinwise.Adapter.FavouriteAdapter;
import com.example.skinwise.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UFavourite extends Fragment {

    private RecyclerView recyclerView;
    private FavouriteAdapter adapter;
    private TextView text;
    private List<String> favouriteList;
    private FirebaseAuth firebaseAuth;


    public UFavourite() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_u_favourite, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        recyclerView = view.findViewById(R.id.FavRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        text = view.findViewById(R.id.text);

        loadFavourites();

        return view;
    }
    private void loadFavourites() {
        favouriteList = new ArrayList<>();
        // Again, assuming a method to get current user's UID
        String userId = firebaseAuth.getUid();
        DatabaseReference favRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(userId).child("Favorites");

        favRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                favouriteList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    favouriteList.add(snapshot.getKey());
                }
                if (favouriteList.isEmpty()) {
                    text.setText("No Bookmark Saved");
                }else {
                    adapter = new FavouriteAdapter(favouriteList, new FavouriteAdapter.OnFavouriteItemClickListener() {
                        @Override
                        public void onItemClick(String lesionType) {
                            USkinLesion3 uSkinLesion3 = new USkinLesion3();
                            Bundle args = new Bundle();
                            args.putString("lesionType", lesionType);
                            uSkinLesion3.setArguments(args);

                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.container1, uSkinLesion3).addToBackStack(null).commit();

                        }
                    });
                }

                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }
}