package com.example.projet_mobile;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {
    private List<Job> jobList;
    private String userEmail;
    private String userId;
    private FirebaseFirestore db;

    public JobAdapter(List<Job> jobList, String userEmail, String userId) {
        this.jobList = jobList;
        this.userEmail = userEmail;
        this.userId = userId;
        this.db = FirebaseFirestore.getInstance();
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

        holder.viewButton.setOnClickListener(v -> holder.showJobDetailDialog(
                holder.itemView,
                job.getCompanyName(),
                job.getCreatedBy(),
                job.getLocation(),
                job.getRemuneration(),
                job.getDate(),
                job.getMetierCible(),
                job.getDescription(),
                userEmail,
                job.getId() // Pass the document ID
        ));
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public void filterList(List<Job> filteredList) {
        this.jobList = filteredList;
        notifyDataSetChanged();
    }

    public class JobViewHolder extends RecyclerView.ViewHolder {
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
        }

        private void showJobDetailDialog(View anchorView, String companyName, String createdBy, String location, String money, String date, String metier, String description, String userEmail, String jobId) {
            final Dialog dialog = new Dialog(anchorView.getContext());
            dialog.setContentView(R.layout.job_detail_dialog);

            TextView dialogCompanyName = dialog.findViewById(R.id.dialogCompanyName);
            TextView dialogCreatedBy = dialog.findViewById(R.id.dialogCreatedBy);
            TextView dialogLocation = dialog.findViewById(R.id.dialogLocation);
            TextView dialogMoney = dialog.findViewById(R.id.dialogMoney);
            TextView dialogDate = dialog.findViewById(R.id.dialogDate);
            TextView dialogMetier = dialog.findViewById(R.id.dialogMetier);
            TextView dialogDescription = dialog.findViewById(R.id.dialogDescription);
            Button closeButton = dialog.findViewById(R.id.dialogCloseButton);
            Button applicationButton = dialog.findViewById(R.id.dialogApplyButton);

            if (userEmail == null) {
                applicationButton.setVisibility(View.GONE);
            } else {
                checkIfAlreadyApplied(jobId, applicationButton);
            }

            dialogCompanyName.setText("Nom de la compagnie : " + companyName);
            dialogCreatedBy.setText("Email : " + createdBy);
            dialogLocation.setText("Lieu : " + location);
            dialogMoney.setText("Salaire : " + money);
            dialogDate.setText("Date : " + date);
            dialogMetier.setText("Métier : " + metier);
            dialogDescription.setText("Description : " + description);

            closeButton.setOnClickListener(v -> dialog.dismiss());

            applicationButton.setOnClickListener(v -> applyForJob(jobId, applicationButton));

            dialog.show();
        }

        private void checkIfAlreadyApplied(String jobId, Button applicationButton) {
            db.collection("offres")
                    .document(jobId)
                    .collection("candidatId")
                    .whereEqualTo("userId", userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (!querySnapshot.isEmpty()) {
                                // Inform the user that they have already applied
                                Toast.makeText(itemView.getContext(), "Vous avez déjà postulé pour cette offre.", Toast.LENGTH_SHORT).show();
                                applicationButton.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(itemView.getContext(), "Erreur lors de la vérification du statut de la candidature.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        private void applyForJob(String jobId, Button applicationButton) {
            Map<String, Object> application = new HashMap<>();
            application.put("userId", userId);
            application.put("etat", "Attente");

            db.collection("offres")
                    .document(jobId)
                    .collection("candidatId")
                    .add(application)
                    .addOnSuccessListener(documentReference -> {
                        addJobToUserApplications(jobId);
                        applicationButton.setVisibility(View.GONE);
                    })
                    .addOnFailureListener(e -> Toast.makeText(itemView.getContext(), "Échec de l'envoi de la candidature !", Toast.LENGTH_SHORT).show());
        }

        private void addJobToUserApplications(String jobId) {
            Map<String, Object> jobApplication = new HashMap<>();
            jobApplication.put("jobId", jobId);
            jobApplication.put("etat", "Attente");

            db.collection("users")
                    .document(userId)
                    .collection("offreId")
                    .add(jobApplication)
                    .addOnSuccessListener(documentReference -> Toast.makeText(itemView.getContext(), "Candidature envoyée avec succès.", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(itemView.getContext(), "Échec de l'ajout de la candidature aux candidatures utilisateur.", Toast.LENGTH_SHORT).show());
        }
    }
}
