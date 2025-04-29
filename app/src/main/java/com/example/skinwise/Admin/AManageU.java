package com.example.skinwise.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.skinwise.R;

public class AManageU extends Fragment {


    private Button addadminbtn,manageadminbtn,managedermabtn,manageuserbtn;
    public AManageU() {    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a_manage_u, container, false);

        addadminbtn = view.findViewById(R.id.addadminbtn);
        manageadminbtn = view.findViewById(R.id.manageadminbtn);
        managedermabtn = view.findViewById(R.id.managedermabtn);
        manageuserbtn = view.findViewById(R.id.manageuserbtn);



        addadminbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                AAddAdmin aAddAdmin = new AAddAdmin();
                transaction.replace(R.id.container3, aAddAdmin).commit();
            }
        });
        manageuserbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                AManageUser aManageUser = new AManageUser();
                transaction.replace(R.id.container3, aManageUser).commit();
            }
        });

        managedermabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                AManageDerma aManageDerma = new AManageDerma();
                transaction.replace(R.id.container3, aManageDerma).commit();
            }
        });
        manageadminbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                AManageAdmin aManageAdmin = new AManageAdmin();
                transaction.replace(R.id.container3, aManageAdmin).commit();
            }
        });

        return view;
    }
}