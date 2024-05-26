package com.example.projet_mobile;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class CandidatureEnCours extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private CandidateAdapter candidateAdapter;
    private List<Candidat> candidateList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidature_en_cours);

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        candidateList = new ArrayList<>();

        // Retrieve the jobId from the Intent
        String jobId = getIntent().getStringExtra("jobId");
        if (jobId != null) {
            candidateAdapter = new CandidateAdapter(candidateList, this, jobId);  // Pass the context and jobId to the adapter
            recyclerView.setAdapter(candidateAdapter);
            loadCandidates(jobId);
        } else {
            Toast.makeText(this, "Job ID not found!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadCandidates(String jobId) {
        db.collection("offres")
                .document(jobId)
                .collection("candidatId")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Candidat candidate = document.toObject(Candidat.class);
                            candidateList.add(candidate);
                        }
                        candidateAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(CandidatureEnCours.this, "Failed to load candidates!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
