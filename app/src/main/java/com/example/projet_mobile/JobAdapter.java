package com.example.projet_mobile;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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
        holder.companyName.setText(job.getCompanyName());
        holder.createdBy.setText(job.getCreatedBy());
        holder.location.setText(job.getLocation());
        holder.money.setText(job.getRemuneration());
        holder.date.setText(job.getDate());
        holder.metier.setText(job.getMetierCible());
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public void filterList(List<Job> filteredList) {
        this.jobList = filteredList;
        notifyDataSetChanged();
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView companyName, createdBy, location, money, date, metier;
        Button viewButton;

        public JobViewHolder(View itemView) {
            super(itemView);
            companyName = itemView.findViewById(R.id.companyName);
            createdBy = itemView.findViewById(R.id.createdBy);
            location = itemView.findViewById(R.id.location);
            money = itemView.findViewById(R.id.money);
            date = itemView.findViewById(R.id.date);
            metier = itemView.findViewById(R.id.metier);
            viewButton = itemView.findViewById(R.id.viewButton);

            viewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showJobDetailDialog(itemView, companyName.getText().toString(), createdBy.getText().toString(), location.getText().toString(), money.getText().toString(), date.getText().toString(), metier.getText().toString());
                }
            });
        }

        private void showJobDetailDialog(View anchorView, String companyName, String createdBy, String location, String money, String date, String metier) {
            final Dialog dialog = new Dialog(anchorView.getContext());
            dialog.setContentView(R.layout.job_detail_dialog);
            TextView dialogCompanyName = dialog.findViewById(R.id.dialogCompanyName);
            TextView dialogCreatedBy = dialog.findViewById(R.id.dialogCreatedBy);
            TextView dialogLocation = dialog.findViewById(R.id.dialogLocation);
            TextView dialogMoney = dialog.findViewById(R.id.dialogMoney);
            TextView dialogDate = dialog.findViewById(R.id.dialogDate);
            TextView dialogMetier = dialog.findViewById(R.id.dialogMetier);
            Button closeButton = dialog.findViewById(R.id.dialogCloseButton);

            dialogCompanyName.setText(companyName);
            dialogCreatedBy.setText(createdBy);
            dialogLocation.setText(location);
            dialogMoney.setText(money);
            dialogDate.setText(date);
            dialogMetier.setText(metier);

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