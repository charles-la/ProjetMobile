package com.example.projet_mobile;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CandidatureAdapter extends RecyclerView.Adapter<CandidatureAdapter.CandidatureViewHolder> {
    private static final String TAG = "CandidatureAdapter";
    private Context context;
    private List<Job> jobList;
    private String userEmail;
    private String userId;
    private FirebaseFirestore db;

    public CandidatureAdapter(List<Job> jobList, String userEmail, String userId, Context context) {
        this.jobList = jobList;
        this.userEmail = userEmail;
        this.userId = userId;
        this.db = FirebaseFirestore.getInstance();
        this.context = context;
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
        String jobId = job.getId();

        // Masquer le contenu initialement
        holder.contentLayout.setVisibility(View.GONE);

        // Reference to the 'candidats' sub-collection within the specific 'offres' document
        CollectionReference candidatsRef = db.collection("offres").document(jobId).collection("candidatId");

        // Query the 'candidats' collection for documents with the specific 'userId'
        candidatsRef.whereEqualTo("userId", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            // Extract the 'etat' field
                            String etat = document.getString("etat");
                            if (etat != null) {
                                holder.etat.setText(etat);
                                Log.d(TAG, "Etat: " + etat + " for userId: " + userId);
                                // Set button visibility based on etat
                                if (etat.equals("Accepter") || etat.equals("Accepter-2")) {
                                    holder.acceptButton.setVisibility(View.VISIBLE);
                                    holder.denieButton.setVisibility(View.VISIBLE);
                                    holder.contactButton.setVisibility(View.VISIBLE);
                                } else {
                                    holder.acceptButton.setVisibility(View.GONE);
                                    holder.denieButton.setVisibility(View.GONE);
                                    holder.contactButton.setVisibility(View.GONE);
                                }
                            } else {
                                holder.etat.setText("No status found");
                                Log.d(TAG, "Etat field is null for userId: " + userId);
                                holder.acceptButton.setVisibility(View.GONE);
                                holder.denieButton.setVisibility(View.GONE);
                                holder.contactButton.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        holder.etat.setText("No documents found for userId");
                        Log.d(TAG, "No documents found for userId: " + userId);
                        holder.acceptButton.setVisibility(View.GONE);
                        holder.denieButton.setVisibility(View.GONE);
                        holder.contactButton.setVisibility(View.GONE);
                    }
                } else {
                    holder.etat.setText("Error retrieving status");
                    Log.e(TAG, "Error getting documents: ", task.getException());
                    holder.acceptButton.setVisibility(View.GONE);
                    holder.denieButton.setVisibility(View.GONE);
                    holder.contactButton.setVisibility(View.GONE);
                }
            }
        });

        // Rendre le contenu visible après le chargement des données
        holder.contentLayout.setVisibility(View.VISIBLE);

        holder.acceptButton.setOnClickListener(v -> {
            Toast.makeText(holder.itemView.getContext(), "Poste accepte", Toast.LENGTH_SHORT).show();
            updateCandidateStatus(jobId, "Accepter-2", holder);
        });
        holder.denieButton.setOnClickListener(v -> {
            Toast.makeText(holder.itemView.getContext(), "Poste Refuse", Toast.LENGTH_SHORT).show();
            updateCandidateStatus(jobId, "Refuser-2", holder);
        });
        holder.contactButton.setOnClickListener(v -> {
            // Handle contact button action separately to start a new activity
            context.startActivity(new Intent(context, Contact.class));
        });
    }

    private void updateCandidateStatus(String jobId, String newStatus, CandidatureViewHolder holder) {
        CollectionReference candidatsRef = db.collection("offres").document(jobId).collection("candidatId");

        candidatsRef.whereEqualTo("userId", userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    candidatsRef.document(document.getId())
                            .update("etat", newStatus)
                            .addOnSuccessListener(aVoid -> {
                                holder.etat.setText(newStatus);
                                Toast.makeText(context, "Status updated to " + newStatus, Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> Toast.makeText(context, "Failed to update status!", Toast.LENGTH_SHORT).show());
                }
            } else {
                Toast.makeText(context, "Candidate not found!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(context, "Error getting documents: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return jobList.size();
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
        LinearLayout contentLayout;

        public CandidatureViewHolder(View itemView) {
            super(itemView);
            companyName = itemView.findViewById(R.id.companyName);
            etat = itemView.findViewById(R.id.etat);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            denieButton = itemView.findViewById(R.id.denieButton);
            contactButton = itemView.findViewById(R.id.contactButton);
            contentLayout = itemView.findViewById(R.id.contentLayout);
        }
    }
}
