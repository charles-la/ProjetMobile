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

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

        if ("Accepter-2".equals(candidate.getEtat()) || "Refuser-2".equals(candidate.getEtat())) {
            // Masquer la vue du candidat
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        } else {
            fetchAndBindUserData(holder, candidate.getUserId(), candidate);
        }
    }

    @Override
    public int getItemCount() {
        return candidateList.size();
    }

    public class CandidateViewHolder extends RecyclerView.ViewHolder {
        TextView userIdTextView;
        TextView userNameTextView;
        TextView userFirstNameTextView;
        TextView emailTextView;
        TextView numTextView;
        TextView nationalityTextView;
        TextView dateOfBirthTextView;
        TextView cityTextView;
        TextView commentTextView;
        TextView etatTextView;
        Button accepterButton;
        Button refuserButton;

        public CandidateViewHolder(@NonNull View itemView) {
            super(itemView);
            userIdTextView = itemView.findViewById(R.id.userIdTextView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            userFirstNameTextView = itemView.findViewById(R.id.userFirstNameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            numTextView = itemView.findViewById(R.id.numTextView);
            nationalityTextView = itemView.findViewById(R.id.nationalityTextView);
            dateOfBirthTextView = itemView.findViewById(R.id.dateOfBirthTextView);
            cityTextView = itemView.findViewById(R.id.cityTextView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            etatTextView = itemView.findViewById(R.id.etatTextView);
            accepterButton = itemView.findViewById(R.id.AccepterButton);
            refuserButton = itemView.findViewById(R.id.RefuserButton);
        }
    }

    private void fetchAndBindUserData(CandidateViewHolder holder, String userId, Candidat candidate) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        holder.userIdTextView.setText(documentSnapshot.getString("userId"));
                        holder.userNameTextView.setText(documentSnapshot.getString("name"));
                        holder.userFirstNameTextView.setText(documentSnapshot.getString("prenom"));
                        holder.emailTextView.setText(documentSnapshot.getString("email"));
                        holder.numTextView.setText(documentSnapshot.getString("numeroDeTelephone"));
                        holder.nationalityTextView.setText(documentSnapshot.getString("nationnalite"));
                        holder.dateOfBirthTextView.setText(documentSnapshot.getString("dateDeNaissance"));
                        holder.cityTextView.setText(documentSnapshot.getString("ville"));
                        holder.commentTextView.setText(documentSnapshot.getString("commentaire"));
                        holder.etatTextView.setText(candidate.getEtat());
                        holder.refuserButton.setOnClickListener(v -> updateCandidateStatus(candidate, "Refuser"));
                        holder.accepterButton.setOnClickListener(v -> updateCandidateStatus(candidate, "Accepter"));
                    } else {
                        Toast.makeText(context, "User data not found!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(context, "Error fetching user data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
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
                        for (DocumentSnapshot document : task.getResult()) {
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
