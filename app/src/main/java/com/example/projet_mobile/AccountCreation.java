package com.example.projet_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class AccountCreation extends AppCompatActivity {
    private FirebaseFirestore db;
    private RadioGroup radioGroup;
    private EditText nomEditText;
    private TextView nomLabel;
    private EditText prenomEditText;
    private TextView prenomLabel;
    private EditText nationEditText;
    private TextView nationLabel;
    private EditText naissanceEditText;
    private TextView naissanceLabel;
    private EditText numEditText;
    private TextView numLabel;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_creation);
        db = FirebaseFirestore.getInstance();
        radioGroup = findViewById(R.id.type_checkbox);
        nomLabel = findViewById(R.id.name_label);
        nomEditText = findViewById(R.id.nom_edit_text);
        prenomLabel = findViewById(R.id.prenom_label);
        prenomEditText = findViewById(R.id.prenom_edit_text);
        nationLabel = findViewById(R.id.nationalite_label);
        nationEditText = findViewById(R.id.nationalite_edit_text);
        naissanceLabel = findViewById(R.id.date_label);
        naissanceEditText = findViewById(R.id.date_naissance_edit_text);
        numLabel = findViewById(R.id.telephone_label);
        numEditText = findViewById(R.id.telephone_edit_text);
        submitButton = findViewById(R.id.submit_button);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonCandidate) {
                    nomEditText.setVisibility(View.VISIBLE);
                    nomLabel.setVisibility(View.VISIBLE);
                    prenomEditText.setVisibility(View.VISIBLE);
                    prenomLabel.setVisibility(View.VISIBLE);
                    nationEditText.setVisibility(View.VISIBLE);
                    nationLabel.setVisibility(View.VISIBLE);
                    naissanceEditText.setVisibility(View.VISIBLE);
                    naissanceLabel.setVisibility(View.VISIBLE);
                    numEditText.setVisibility(View.VISIBLE);
                    numLabel.setVisibility(View.VISIBLE);
                    submitButton.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.radioButtonEmployee) {
                    nomEditText.setVisibility(View.GONE);
                    nomLabel.setVisibility(View.GONE);
                    submitButton.setVisibility(View.VISIBLE);
                }
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });
    }

    private void addUser() {
        // Create a new user object
        Map<String, Object> user = new HashMap<>();
        user.put("name", nomEditText.getText().toString());
        user.put("prenom", prenomEditText.getText().toString());
        user.put("nationnalite", nationEditText.getText().toString());

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    // Document added successfully
                    // You can perform any further actions here if needed
                })
                .addOnFailureListener(e -> {
                    // Error adding document
                });
    }

}