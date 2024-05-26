package com.example.projet_mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AccueilCandidat extends AppCompatActivity {

    private Button buttonAccountCreation;
    private Button buttonDeconnexion;
    private Button buttonGestionCandidature;
    private FirebaseFirestore db;
    private String userEmail;
    private String userId;
    private EditText searchEditText;
    private JobAdapter jobAdapter;
    private List<Job> jobList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil_candidat);

        buttonAccountCreation = findViewById(R.id.buttonCreateAccount);
        buttonDeconnexion = findViewById(R.id.buttonDeconnexion);
        buttonGestionCandidature = findViewById(R.id.buttonGestionCandidature);
        searchEditText = findViewById(R.id.searchEditText);
        jobList = new ArrayList<>();

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("userEmail")) {
                userEmail = intent.getStringExtra("userEmail");
            }
            if (intent.hasExtra("userId")) {
                userId = intent.getStringExtra("userId");
            }
        }

        if (userEmail == null) {
            buttonGestionCandidature.setVisibility(View.GONE);
        } else {
            buttonAccountCreation.setVisibility(View.GONE);
        }

        buttonAccountCreation.setOnClickListener(v ->
                startActivity(new Intent(AccueilCandidat.this, AccountCreation.class))
        );

        buttonDeconnexion.setOnClickListener(v ->
                startActivity(new Intent(AccueilCandidat.this, Connection.class))
        );

        buttonGestionCandidature.setOnClickListener(v -> {
            Intent gestionCandidatureIntent = new Intent(AccueilCandidat.this, GestionCandidature.class);
            gestionCandidatureIntent.putExtra("userId", userId); // Pass userId
            startActivity(gestionCandidatureIntent);
        });

        initFirestore();
        loadJobOffers();

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void initFirestore() {
        db = FirebaseFirestore.getInstance();
    }

    private void loadJobOffers() {
        db.collection("offres")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        jobList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Job job = document.toObject(Job.class);
                            job.setId(document.getId());
                            jobList.add(job);
                        }
                        RecyclerView recyclerView = findViewById(R.id.recyclerView);
                        jobAdapter = new JobAdapter(jobList, userEmail, userId); // Pass userId to JobAdapter
                        recyclerView.setAdapter(jobAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    } else {
                        Log.w("AccueilCandidat", "Error getting documents.", task.getException());
                    }
                });
    }

    private void filter(String text) {
        List<Job> filteredList = new ArrayList<>();
        for (Job job : jobList) {
            if (job.getCompanyName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(job);
            }
        }
        jobAdapter.filterList(filteredList);
    }
}
