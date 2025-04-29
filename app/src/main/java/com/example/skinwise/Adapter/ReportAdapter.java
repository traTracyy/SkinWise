package com.example.skinwise.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.skinwise.Model.ModelReport;
import com.example.skinwise.R;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

    private List<ModelReport> reportList;

    private OnReportClickListener listener;

    public ReportAdapter(List<ModelReport> reportList) {
        this.reportList = reportList;
    }
    public interface OnReportClickListener {
        void onReportClick(ModelReport report);
    }

    public void setOnReportClickListener(OnReportClickListener listener) {
        this.listener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ModelReport report = reportList.get(position);
        holder.Descriptiontext.setText(report.getDescription());
        holder.datetime.setText(report.getDateTime());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onReportClick(report);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView Descriptiontext,datetime;
        // Other views for the report data

        public ViewHolder(View view) {
            super(view);
            Descriptiontext = view.findViewById(R.id.Description);
            datetime = view.findViewById(R.id.DateTv);
        }
    }
}
