package com.example.skinwise.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skinwise.Adapter.ReportAdapter;
import com.example.skinwise.Model.ModelReport;
import com.example.skinwise.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AFeedback extends Fragment {

    private RecyclerView recyclerView;
    private ReportAdapter adapter;
    private List<ModelReport> reportList;

    public AFeedback() {    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a_feedback, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewReports);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reportList = new ArrayList<>();
        adapter = new ReportAdapter(reportList);
        recyclerView.setAdapter(adapter);

        fetchReports();

        adapter.setOnReportClickListener(new ReportAdapter.OnReportClickListener() {
            @Override
            public void onReportClick(ModelReport report) {
                navigateToReportDetails(report);
            }
        });

        return view;
    }
    private void navigateToReportDetails(ModelReport report) {
        AFeedbackDetails detailsFragment = new AFeedbackDetails();

        Bundle args = new Bundle();
        args.putString("reportId", report.getReportId());
        args.putString("userId", report.getUserId());
        args.putString("description", report.getDescription());
        args.putString("contactInfo", report.getContactInfo());
        args.putString("dateTime", report.getDateTime());

        List<String> images = report.getImages();
        args.putStringArrayList("images", images != null ? new ArrayList<>(images) : new ArrayList<String>());

        detailsFragment.setArguments(args);

        // Perform the fragment transaction to show AFeedbackDetails
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container3, detailsFragment)
                .addToBackStack(null)
                .commit();
    }

    private void fetchReports() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Report");
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reportList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ModelReport report = postSnapshot.getValue(ModelReport.class);
                    reportList.add(report);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }

}