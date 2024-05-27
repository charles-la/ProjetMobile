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

public class CandidatureAccepteAdapter extends RecyclerView.Adapter<CandidatureAccepteAdapter.CandidateViewHolder> {
    private List<Candidat> candidateList;
    private Context context;
    private String jobId;

    public CandidatureAccepteAdapter(List<Candidat> candidateList, Context context, String jobId) {
        this.candidateList = candidateList;
        this.context = context;
        this.jobId = jobId;
    }

    @NonNull
    @Override
    public CandidateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_candidat_accept, parent, false);
        return new CandidateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CandidateViewHolder holder, int position) {
        Candidat candidate = candidateList.get(position);

        if ("Accepter-2".equals(candidate.getEtat())) {
            fetchAndBindUserData(holder, candidate.getUserId(), candidate);
        } else {
            // Masquer la vue du candidat
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
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
        Button contactButton;

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
            contactButton = itemView.findViewById(R.id.ContactButton);
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
//                        holder.contactButton.setOnClickListener();
                    } else {
                        Toast.makeText(context, "User data not found!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(context, "Error fetching user data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
