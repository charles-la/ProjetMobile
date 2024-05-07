package com.example.projet_mobile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import android.app.Dialog;


import com.example.projet_mobile.Job; // Import the Job class if it's in a specific package

import java.util.List;

import java.util.ArrayList;
import java.util.List;


public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {
    private List<Job> jobList;

    public JobAdapter(List<Job> jobList) {
        this.jobList = jobList;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_item, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Job job = jobList.get(position);
//        holder.jobId.setText(job.getId());
        holder.companyName.setText(job.getCompanyName());
        holder.location.setText(job.getLocation());
        holder.date.setText(job.getDate());
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }


    public static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView companyName, location, date;
        Button viewButton;

        public JobViewHolder(View itemView) {
            super(itemView);
            companyName = itemView.findViewById(R.id.companyName);
            location = itemView.findViewById(R.id.location);
            date = itemView.findViewById(R.id.date);
            viewButton = itemView.findViewById(R.id.viewButton);

            viewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showJobDetailDialog(itemView, companyName.getText().toString(), location.getText().toString(), date.getText().toString());
                }
            });
        }

        private void showJobDetailDialog(View anchorView, String companyName, String location, String date) {
            final Dialog dialog = new Dialog(anchorView.getContext());
            dialog.setContentView(R.layout.job_detail_dialog);
            TextView dialogCompanyName = dialog.findViewById(R.id.dialogCompanyName);
            TextView dialogLocation = dialog.findViewById(R.id.dialogLocation);
            TextView dialogDate = dialog.findViewById(R.id.dialogDate);
            Button closeButton = dialog.findViewById(R.id.dialogCloseButton);

            dialogCompanyName.setText(companyName);
            dialogLocation.setText(location);
            dialogDate.setText(date);

            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
    }
}
