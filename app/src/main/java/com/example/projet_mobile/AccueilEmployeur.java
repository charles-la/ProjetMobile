package com.example.projet_mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AccueilEmployeur extends AppCompatActivity {

    private Button buttonOffreCreation;
    private String userEmail;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil_employeur);

        buttonOffreCreation = findViewById(R.id.buttonCreateOffre);

        // Get the passed email from the intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("userEmail")) {
            userEmail = intent.getStringExtra("userEmail");
        }

        buttonOffreCreation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccueilEmployeur.this, OffreCreation.class);
                intent.putExtra("userEmail", userEmail);
                startActivity(intent);
            }
        });

        initFirestore();
        loadJobOffers();
    }

    private void initFirestore() {
        db = FirebaseFirestore.getInstance();
    }

    private void loadJobOffers() {
        db.collection("offres")
                .whereEqualTo("createdBy", userEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Job> jobs = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Job job = document.toObject(Job.class);
                            job.setId(document.getId()); // Set the document ID
                            jobs.add(job);
                        }
                        RecyclerView recyclerView = findViewById(R.id.recyclerView);
                        JobAdapter adapter = new JobAdapter(jobs, userEmail, null); // Pass userEmail and null for userId
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    } else {
                        Log.w("AccueilEmployeur", "Error getting documents.", task.getException());
                    }
                });
    }
}
