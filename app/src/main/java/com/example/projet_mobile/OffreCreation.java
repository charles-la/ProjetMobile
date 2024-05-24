package com.example.projet_mobile;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class OffreCreation extends AppCompatActivity {

    private FirebaseFirestore db;
    private EditText companyNameEditText;
    private EditText descriptionEditText;
    private EditText locationEditText;
    private EditText periodEditText;
    private EditText moneyEditText;
    private EditText metierEditText;
    private Button submitButton;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offre_creation);

        db = FirebaseFirestore.getInstance();

        companyNameEditText = findViewById(R.id.company_name_edit_text);
        descriptionEditText = findViewById(R.id.description_edit_text);
        locationEditText = findViewById(R.id.location_edit_text);
        periodEditText = findViewById(R.id.period_edit_text);
        moneyEditText = findViewById(R.id.money_edit_text);
        metierEditText = findViewById(R.id.metier_edit_text);
        submitButton = findViewById(R.id.submit_button);

        // Get the passed email from the intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("userEmail")) {
            userEmail = intent.getStringExtra("userEmail");
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!companyNameEditText.getText().toString().isEmpty() &&
                        !descriptionEditText.getText().toString().isEmpty() &&
                        !locationEditText.getText().toString().isEmpty() &&
                        !periodEditText.getText().toString().isEmpty() &&
                        !moneyEditText.getText().toString().isEmpty() &&
                        !metierEditText.getText().toString().isEmpty()) {

                    addOffre();
                    Intent intent = new Intent(OffreCreation.this, AccueilEmployeur.class);
                    intent.putExtra("userEmail", userEmail);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Creation failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addOffre() {
        Map<String, Object> offre = new HashMap<>();
        offre.put("companyName", companyNameEditText.getText().toString());
        offre.put("description", descriptionEditText.getText().toString());
        offre.put("location", locationEditText.getText().toString());
        offre.put("date", periodEditText.getText().toString());
        offre.put("remuneration", moneyEditText.getText().toString());
        offre.put("metierCible", metierEditText.getText().toString());
        if (userEmail != null) {
            offre.put("createdBy", userEmail);
        }

        db.collection("offres")
                .add(offre)
                .addOnSuccessListener(documentReference ->
                        Toast.makeText(getApplicationContext(), "Creation successful", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(getApplicationContext(), "Creation failed!", Toast.LENGTH_SHORT).show());
    }
}
