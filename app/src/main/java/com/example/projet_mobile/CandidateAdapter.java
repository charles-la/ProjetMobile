package com.example.projet_mobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class CandidateAdapter extends RecyclerView.Adapter<CandidateAdapter.CandidateViewHolder> {
    private List<Candidat> candidateList;
    private Context context;
    private String jobId;

    public CandidateAdapter(List<Candidat> candidateList, Context context, String jobId) {
        this.candidateList = candidateList;
        this.context = context;
        this.jobId = jobId;
    }

    @NonNull
    @Override
    public CandidateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_candidate, parent, false);
        return new CandidateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CandidateViewHolder holder, int position) {
        Candidat candidate = candidateList.get(position);
        holder.userIdTextView.setText(candidate.getUserId());
        holder.etatTextView.setText(candidate.getEtat());

        holder.candidatureButton.setOnClickListener(v -> updateCandidateStatus(candidate, "Accepter"));
    }

    @Override
    public int getItemCount() {
        return candidateList.size();
    }

    public class CandidateViewHolder extends RecyclerView.ViewHolder {
        TextView userIdTextView;
        TextView etatTextView;
        Button candidatureButton;

        public CandidateViewHolder(@NonNull View itemView) {
            super(itemView);
            userIdTextView = itemView.findViewById(R.id.userIdTextView);
            etatTextView = itemView.findViewById(R.id.etatTextView);
            candidatureButton = itemView.findViewById(R.id.CandidatureButton);
        }
    }

    private void updateCandidateStatus(Candidat candidate, String newStatus) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("offres")
                .document(jobId)
                .collection("candidatId")
                .whereEqualTo("userId", candidate.getUserId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            db.collection("offres")
                                    .document(jobId)
                                    .collection("candidatId")
                                    .document(document.getId())
                                    .update("etat", newStatus)
                                    .addOnSuccessListener(aVoid -> {
                                        candidate.setEtat(newStatus);
                                        notifyDataSetChanged();
                                        Toast.makeText(context, "Status updated to " + newStatus, Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(context, "Failed to update status!", Toast.LENGTH_SHORT).show());
                        }
                    } else {
                        Toast.makeText(context, "Candidate not found!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(context, "Error getting documents: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
