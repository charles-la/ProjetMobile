package com.example.projet_mobile;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CandidatureAdapter extends RecyclerView.Adapter<CandidatureAdapter.CandidatureViewHolder> {
    private static final String TAG = "CandidatureAdapter";
    private List<Job> jobList;
    private String userEmail;
    private String userId;
    private FirebaseFirestore db;

    public CandidatureAdapter(List<Job> jobList, String userEmail, String userId) {
        this.jobList = jobList;
        this.userEmail = userEmail;
        this.userId = userId;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public CandidatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.candidature, parent, false);
        return new CandidatureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CandidatureViewHolder holder, int position) {
        Job job = jobList.get(position);
        holder.companyName.setText(job.getCompanyName());

        if (job.getId() != null) {
            getJobApplicationEtat(job.getId(), etat -> {
                if (etat != null) {
                    holder.etat.setText(etat);
                } else {
                    holder.etat.setText("Unknown");
                    Log.e(TAG, "Etat is null for jobId: " + job.getId());
                }
            });
        } else {
            Log.e(TAG, "Job ID is null for position: " + position);
            holder.etat.setText("Unknown");
        }

        holder.acceptButton.setOnClickListener(v -> handleButtonAction(holder, "Accepted", job.getId()));
        holder.denieButton.setOnClickListener(v -> handleButtonAction(holder, "Denied", job.getId()));
        holder.contactButton.setOnClickListener(v -> handleButtonAction(holder, "Contacted", job.getId()));
    }

    private void getJobApplicationEtat(String jobId, final EtatCallback callback) {
        if (userId == null) {
            Log.e(TAG, "User ID is null.");
            callback.onCallback(null);
            return;
        }

        Log.d(TAG, "Fetching etat for jobId: " + jobId + " for userId: " + userId);

        db.collection("users")
                .document(userId)
                .collection("offreId")
                .whereEqualTo("jobId", jobId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            String etat = document.getString("etat");
                            Log.d(TAG, "Found etat: " + etat + " for jobId: " + jobId);
                            callback.onCallback(etat);
                            return;
                        }
                        Log.e(TAG, "No document found for jobId: " + jobId);
                        callback.onCallback(null);
                    } else {
                        Log.e(TAG, "Error getting documents: ", task.getException());
                        callback.onCallback(null);
                    }
                }).addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting documents: ", e);
                    callback.onCallback(null);
                });
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    private void handleButtonAction(CandidatureViewHolder holder, String action, String jobId) {
        if (jobId == null) {
            Log.e(TAG, "Job ID is null for action: " + action);
            return;
        }
        Map<String, Object> update = new HashMap<>();
        update.put("status", action);
        db.collection("jobs").document(jobId)
                .update(update)
                .addOnSuccessListener(aVoid -> Toast.makeText(holder.itemView.getContext(), action + " successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(holder.itemView.getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    public void filterList(List<Job> filteredList) {
        this.jobList = filteredList;
        notifyDataSetChanged();
    }

    public static class CandidatureViewHolder extends RecyclerView.ViewHolder {
        TextView companyName;
        TextView etat;
        Button acceptButton;
        Button denieButton;
        Button contactButton;

        public CandidatureViewHolder(View itemView) {
            super(itemView);
            companyName = itemView.findViewById(R.id.companyName);
            etat = itemView.findViewById(R.id.etat);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            denieButton = itemView.findViewById(R.id.denieButton);
            contactButton = itemView.findViewById(R.id.contactButton);
        }
    }

    private interface EtatCallback {
        void onCallback(String etat);
    }
}
