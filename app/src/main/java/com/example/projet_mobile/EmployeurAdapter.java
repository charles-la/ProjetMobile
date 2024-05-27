package com.example.projet_mobile;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class EmployeurAdapter extends RecyclerView.Adapter<EmployeurAdapter.EmployeurViewHolder> {
    private List<Job> jobList;
    private String userEmail;
    private Context context;
    private FirebaseFirestore db;

    public EmployeurAdapter(List<Job> jobList, String userEmail, Context context) {
        this.jobList = jobList;
        this.userEmail = userEmail;
        this.context = context;
        this.db = FirebaseFirestore.getInstance(); // Initialize Firestore instance
    }

    @NonNull
    @Override
    public EmployeurViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_item_employeur, parent, false);
        return new EmployeurViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeurViewHolder holder, int position) {
        Job job = jobList.get(position);
        holder.companyName.setText(job.getCompanyName());
        holder.email.setText(job.getCreatedBy());

        holder.candidatureButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, CandidatureEnCours.class);
            intent.putExtra("jobId", job.getId()); // Pass the jobId as an extra
            context.startActivity(intent);
        });


        holder.acceptButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, CandidatureAccepte.class);
            intent.putExtra("jobId", job.getId()); // Pass the jobId as an extra
            context.startActivity(intent);
        });

        holder.editButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, OffreCreation.class);
            intent.putExtra("jobId", job.getId());
            intent.putExtra("companyName", job.getCompanyName());
            intent.putExtra("description", job.getDescription());
            intent.putExtra("location", job.getLocation());
            intent.putExtra("period", job.getDate());
            intent.putExtra("money", job.getRemuneration());
            intent.putExtra("metier", job.getMetierCible());
            intent.putExtra("userEmail", userEmail);
            context.startActivity(intent);
        });

        holder.deleteButton.setOnClickListener(v -> {
            deleteJob(job.getId(), position);
        });
    }

    private void deleteJob(String jobId, int position) {
        db.collection("offres").document(jobId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    jobList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Job deleted successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error deleting job", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public static class EmployeurViewHolder extends RecyclerView.ViewHolder {
        TextView companyName, email;
        Button candidatureButton, acceptButton, editButton, deleteButton;

        public EmployeurViewHolder(@NonNull View itemView) {
            super(itemView);
            companyName = itemView.findViewById(R.id.companyName);
            email = itemView.findViewById(R.id.email);
            candidatureButton = itemView.findViewById(R.id.CandidatureButton);
            acceptButton = itemView.findViewById(R.id.CandidatureAccButton);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
