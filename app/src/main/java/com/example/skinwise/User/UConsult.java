package com.example.skinwise.User;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skinwise.Adapter.AdapterDerm;
import com.example.skinwise.Constants;
import com.example.skinwise.LoginActivity;
import com.example.skinwise.Model.ModelDerm;
import com.example.skinwise.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
// ...

public class UConsult extends Fragment {

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private ArrayList<ModelDerm> dermaList;
    private AdapterDerm adapterDerm;
    private RecyclerView dermRv;
    private RelativeLayout filter;
    private Button booking;
    private TextView statetxt;

    private String selectedState = "All";
    public UConsult() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_u_consult, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Dermatologists...");
        progressDialog.setCanceledOnTouchOutside(false);
        filter = view.findViewById(R.id.filter);
        statetxt = view.findViewById(R.id.statetxt);

        dermRv = view.findViewById(R.id.dermRv);
        booking = view.findViewById(R.id.booking);
        dermRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        checkuser();
        loadDermatologists();
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStateFilterDialog();
            }
        });

        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                UBook2 book2Fragment = new UBook2();
                transaction.replace(R.id.container1, book2Fragment).commit();
            }
        });

        return view;
    }



    private void showStateFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose State")
                .setItems(Constants.state1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedState = Constants.state1[which];
                        filterDermatologists();
                    }
                }).show();
    }
    private void filterDermatologists() {
        if (selectedState.equals("All")) {
            loadDermatologists(); // Load all dermatologists if "All" is selected
        } else {
            progressDialog.show();
            ArrayList<ModelDerm> filteredList = new ArrayList<>();
            for (ModelDerm model : dermaList) {
                if (model.getState().equals(selectedState)) {
                    filteredList.add(model);
                }
            }
            adapterDerm = new AdapterDerm(getActivity(), filteredList);
            dermRv.setAdapter(adapterDerm);

            if (selectedState.equals("All")) {
                statetxt.setText("State: [All]");
            } else if (selectedState.equals("Johor")) {
                statetxt.setText("State: [Johor]");
            } else if (selectedState.equals("Kedah")) {
                statetxt.setText("State: [Kedah]");
            }else if (selectedState.equals("Kelantan")) {
                statetxt.setText("State: [Kelantan]");
            }else if (selectedState.equals("Malacca")) {
                statetxt.setText("State: [Malacca]");
            }else if (selectedState.equals("Negeri Sembilan")) {
                statetxt.setText("State: [Negeri Sembilan]");
            }else if (selectedState.equals("Pahang")) {
                statetxt.setText("State: [Pahang]");
            }else if (selectedState.equals("Perak")) {
                statetxt.setText("State: [Perak]");
            }else if (selectedState.equals("Perlis")) {
                statetxt.setText("State: [Perlis]");
            }else if (selectedState.equals("Penang")) {
                statetxt.setText("State: [Penang]");
            }else if (selectedState.equals("Sarawak")) {
                statetxt.setText("State: [Sarawak]");
            }else if (selectedState.equals("Terengganu")) {
                statetxt.setText("State: [Terengganu]");
            }else if (selectedState.equals("Kuala Lumpur")) {
                statetxt.setText("State: [Kuala Lumpur]");
            }else if (selectedState.equals("Sabah")) {
                statetxt.setText("State: [Sabah]");
            }
            progressDialog.dismiss();
        }
    }
    private void loadDermatologists() {
        progressDialog.show();
        dermaList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("role").equalTo("Derma")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        dermaList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ModelDerm modelDerm = ds.getValue(ModelDerm.class);
//                            dermaList.add(modelDerm);

                            if (modelDerm != null && "true".equals(modelDerm.getApprove())) {
                                dermaList.add(modelDerm);
                            }
                        }
                        adapterDerm = new AdapterDerm(getActivity(), dermaList);
                        dermRv.setAdapter(adapterDerm);
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressDialog.dismiss();
                    }
                });
    }

    private void checkuser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }
    }
}
