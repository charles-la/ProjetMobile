package com.example.projet_mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class GestionCandidature extends AppCompatActivity {

    private FirebaseFirestore db;
    private String userId;
    private JobAdapter jobAdapter;
    private List<Job> jobList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_candidature);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        jobList = new ArrayList<>();
        jobAdapter = new JobAdapter(jobList, null, userId); // userEmail not needed here
        recyclerView.setAdapter(jobAdapter);

        db = FirebaseFirestore.getInstance();

        if (getIntent().hasExtra("userId")) {
            userId = getIntent().getStringExtra("userId");
            loadUserApplications();
        } else {
            Log.e("GestionCandidature", "No userId passed in intent");
        }
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
                                jobAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.e("GestionCandidature", "Error getting job details: ", task.getException());
                        }
                    });
        }
    }
}
