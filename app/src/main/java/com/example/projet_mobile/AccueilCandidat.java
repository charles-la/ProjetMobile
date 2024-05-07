package com.example.projet_mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AccueilCandidat extends AppCompatActivity {

    private Button buttonAccountCreation;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil_candidat);

        buttonAccountCreation = findViewById(R.id.buttonCreateAccount);
        buttonAccountCreation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccueilCandidat.this, AccountCreation.class));
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
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Job> jobs = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            jobs.add(document.toObject(Job.class));
                        }
                        RecyclerView recyclerView = findViewById(R.id.recyclerView);
                        JobAdapter adapter = new JobAdapter(jobs);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    } else {
                        Log.w("AccueilCandidat", "Error getting documents.", task.getException());
                    }
                });
    }
}
