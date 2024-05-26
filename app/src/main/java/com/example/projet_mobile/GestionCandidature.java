package com.example.projet_mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestionCandidature extends AppCompatActivity {

    private FirebaseFirestore db;
    private String userId;
    private CandidatureAdapter candidatureAdapter;
    private List<Job> jobList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_candidature);

        if (getIntent().hasExtra("userId")) {
            userId = getIntent().getStringExtra("userId");
        } else {
            Log.e("GestionCandidature", "No userId passed in intent");
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        jobList = new ArrayList<>();
        candidatureAdapter = new CandidatureAdapter(jobList, null, userId,this); // userEmail not needed here
        recyclerView.setAdapter(candidatureAdapter);

        db = FirebaseFirestore.getInstance();
        loadUserApplications();

    }

    private void loadUserApplications() {
        db.collection("users")
                .document(userId)
                .collection("offreId")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> jobIds = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String jobId = document.getString("jobId");
                            jobIds.add(jobId);
                        }
                        loadJobsDetails(jobIds);
                    } else {
                        Log.e("GestionCandidature", "Error getting documents: ", task.getException());
                    }
                });
    }

    private void loadJobsDetails(List<String> jobIds) {
        for (String jobId : jobIds) {
            db.collection("offres")
                    .document(jobId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Job job = document.toObject(Job.class);
                                job.setId(document.getId());
                                jobList.add(job);
                                candidatureAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.e("GestionCandidature", "Error getting job details: ", task.getException());
                        }
                    });
        }
    }

    private void addJobToUserApplications(String jobId) {
        Map<String, Object> jobApplication = new HashMap<>();
        jobApplication.put("jobId", jobId);
        jobApplication.put("etat", "Wait");

        db.collection("users")
                .document(userId)
                .collection("offreId")
                .add(jobApplication)
                .addOnSuccessListener(documentReference ->
                        Toast.makeText(GestionCandidature.this, "Application successful", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(GestionCandidature.this, "Failed to add job to user applications", Toast.LENGTH_SHORT).show());
    }
}
