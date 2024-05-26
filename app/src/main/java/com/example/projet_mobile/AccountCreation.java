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
import android.widget.Toast;

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
    private EditText emailEditText;
    private TextView emailLabel;
    private EditText villeEditText;
    private TextView villeLabel;
    private EditText commentaireEditText;
    private TextView commentaireLabel;
    private EditText passwordEditText;
    private TextView passwordLabel;
    private Button submitButton;
    private int selectedId;
    private RadioButton selectedRadioButton;

    // Entreprise

    private TextView nomEntrepriseLabel;
    private EditText nomEntrepriseEditText;
    private TextView numEmployeurLabel;
    private EditText numEmployeurEditText;
    private TextView emailEmployeurLabel;
    private EditText emailEmployeurEditText;
    private TextView adresseEmployeurLabel;
    private EditText adresseEmployeurEditText;
    private TextView villeEmployeurLabel;
    private TextView villeEmployeurEditText;
    private TextView liensPublicsEmployeurLabel;
    private TextView liensPublicsEmployeurEditText;
    private EditText passwordEmployeurEditText;
    private TextView passwordEmployeurLabel;
    private Button submitEmployeurButton;


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
        emailLabel = findViewById(R.id.email_label);
        emailEditText = findViewById(R.id.email_edit_text);
        villeLabel = findViewById(R.id.ville_label);
        villeEditText = findViewById(R.id.ville_edit_text);
        commentaireLabel = findViewById(R.id.commentaire_label);
        commentaireEditText = findViewById(R.id.commentaire_edit_text);
        passwordLabel = findViewById(R.id.password_label);
        passwordEditText = findViewById(R.id.password_edit_text);
        submitButton = findViewById(R.id.submit_button);

        nomEntrepriseLabel = findViewById(R.id.nom_entreprise_label);
        nomEntrepriseEditText = findViewById(R.id.nom_entreprise_edit_text);
        numEmployeurLabel = findViewById(R.id.telephone_employeur_label);
        numEmployeurEditText = findViewById(R.id.telephone_employeur_edit_text);
        emailEmployeurLabel = findViewById(R.id.mail_employeur_label);
        emailEmployeurEditText = findViewById(R.id.mail_employeur_edit_text);
        adresseEmployeurLabel = findViewById(R.id.adresse_employeur_label);
        adresseEmployeurEditText = findViewById(R.id.adresse_employeur_edit_text);
        villeEmployeurLabel = findViewById(R.id.ville_employeur_label);
        villeEmployeurEditText = findViewById(R.id.ville_employeur_edit_text);
        liensPublicsEmployeurLabel = findViewById(R.id.liens_publics_employeur_label);
        liensPublicsEmployeurEditText = findViewById(R.id.liens_publics_employeur_edit_text);
        passwordEmployeurLabel = findViewById(R.id.password_employeur_label);
        passwordEmployeurEditText = findViewById(R.id.password_employeur_edit_text);
        submitEmployeurButton = findViewById(R.id.submit_employeur_button);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("userEmail")) {
                String userEmail = intent.getStringExtra("userEmail");
                emailEditText.setText(userEmail);
            }
            if (intent.hasExtra("userId")) {
                String userId = intent.getStringExtra("userId");
                // Utiliser userId comme nécessaire
            }
        }

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
                    emailLabel.setVisibility(View.VISIBLE);
                    emailEditText.setVisibility(View.VISIBLE);
                    villeLabel.setVisibility(View.VISIBLE);
                    villeEditText.setVisibility(View.VISIBLE);
                    commentaireLabel.setVisibility(View.VISIBLE);
                    commentaireEditText.setVisibility(View.VISIBLE);
                    passwordLabel.setVisibility(View.VISIBLE);
                    passwordEditText.setVisibility(View.VISIBLE);
                    submitButton.setVisibility(View.VISIBLE);

                    nomEntrepriseLabel.setVisibility(View.GONE);
                    nomEntrepriseEditText.setVisibility(View.GONE);
                    numEmployeurLabel.setVisibility(View.GONE);
                    numEmployeurEditText.setVisibility(View.GONE);
                    emailEmployeurLabel.setVisibility(View.GONE);
                    emailEmployeurEditText.setVisibility(View.GONE);
                    adresseEmployeurLabel.setVisibility(View.GONE);
                    adresseEmployeurEditText.setVisibility(View.GONE);
                    villeEmployeurLabel.setVisibility(View.GONE);
                    villeEmployeurEditText.setVisibility(View.GONE);
                    liensPublicsEmployeurLabel.setVisibility(View.GONE);
                    liensPublicsEmployeurEditText.setVisibility(View.GONE);
                    passwordEmployeurLabel.setVisibility(View.GONE);
                    passwordEmployeurEditText.setVisibility(View.GONE);
                    submitEmployeurButton.setVisibility(View.GONE);


                } else if (checkedId == R.id.radioButtonEmployee) {
                    nomEditText.setVisibility(View.GONE);
                    nomLabel.setVisibility(View.GONE);
                    prenomEditText.setVisibility(View.GONE);
                    prenomLabel.setVisibility(View.GONE);
                    nationEditText.setVisibility(View.GONE);
                    nationLabel.setVisibility(View.GONE);
                    naissanceEditText.setVisibility(View.GONE);
                    naissanceLabel.setVisibility(View.GONE);
                    numEditText.setVisibility(View.GONE);
                    numLabel.setVisibility(View.GONE);
                    emailLabel.setVisibility(View.GONE);
                    emailEditText.setVisibility(View.GONE);
                    villeLabel.setVisibility(View.GONE);
                    villeEditText.setVisibility(View.GONE);
                    commentaireLabel.setVisibility(View.GONE);
                    commentaireEditText.setVisibility(View.GONE);
                    passwordLabel.setVisibility(View.GONE);
                    passwordEditText.setVisibility(View.GONE);
                    submitButton.setVisibility(View.GONE);

                    nomEntrepriseLabel.setVisibility(View.VISIBLE);
                    nomEntrepriseEditText.setVisibility(View.VISIBLE);
                    numEmployeurLabel.setVisibility(View.VISIBLE);
                    numEmployeurEditText.setVisibility(View.VISIBLE);
                    emailEmployeurLabel.setVisibility(View.VISIBLE);
                    emailEmployeurEditText.setVisibility(View.VISIBLE);
                    adresseEmployeurLabel.setVisibility(View.VISIBLE);
                    adresseEmployeurEditText.setVisibility(View.VISIBLE);
                    villeEmployeurLabel.setVisibility(View.VISIBLE);
                    villeEmployeurEditText.setVisibility(View.VISIBLE);
                    liensPublicsEmployeurLabel.setVisibility(View.VISIBLE);
                    liensPublicsEmployeurEditText.setVisibility(View.VISIBLE);
                    passwordEmployeurLabel.setVisibility(View.VISIBLE);
                    passwordEmployeurEditText.setVisibility(View.VISIBLE);
                    submitEmployeurButton.setVisibility(View.VISIBLE);
                }
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Si un editText est empty on ne peut pas ajouter le user
                if(!nomEditText.getText().toString().isEmpty() && !prenomEditText.getText().toString().isEmpty() && !emailEditText.getText().toString().isEmpty() && !passwordEditText.getText().toString().isEmpty()) {
                    addCandidat();
                    startActivity(new Intent(AccountCreation.this, Connection.class));
                }
                else{
                    Toast.makeText(getApplicationContext(), "Creation fail", Toast.LENGTH_SHORT).show();
                }
            }
        });

        submitEmployeurButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Si un editText est empty on ne peut pas ajouter le user
                if(!nomEntrepriseEditText.getText().toString().isEmpty() && !emailEmployeurEditText.getText().toString().isEmpty() && !passwordEmployeurEditText.getText().toString().isEmpty()) {
                    addEmployeur();
                    startActivity(new Intent(AccountCreation.this, Connection.class));
                }
                else{
                    Toast.makeText(getApplicationContext(), "Creation fail", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addCandidat() {
        // Create a new user object
        Map<String, Object> user = new HashMap<>();
        user.put("name", nomEditText.getText().toString());
        user.put("prenom", prenomEditText.getText().toString());
        user.put("nationnalite", nationEditText.getText().toString());
        user.put("dateDeNaissance", naissanceEditText.getText().toString());
        user.put("numeroDeTelephone", numEditText.getText().toString());
        user.put("email", emailEditText.getText().toString());
        user.put("ville", villeEditText.getText().toString());
        user.put("commentaire", commentaireEditText.getText().toString());
        user.put("password", passwordEditText.getText().toString());

        selectedId = radioGroup.getCheckedRadioButtonId();
        selectedRadioButton = findViewById(selectedId);
        user.put("typeUtilisateur", selectedRadioButton.getText().toString());

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    // Document added successfully
                    Toast.makeText(getApplicationContext(), "Creation successful", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Creation fail !!!", Toast.LENGTH_SHORT).show();
                });
    }

    private void addEmployeur() {
        // Create a new user object
        Map<String, Object> user = new HashMap<>();
        user.put("nomEntreprise", nomEntrepriseEditText.getText().toString());
        user.put("telephone", numEmployeurEditText.getText().toString());
        user.put("email", emailEmployeurEditText.getText().toString());
        user.put("adresse", adresseEmployeurEditText.getText().toString());
        user.put("ville", villeEmployeurEditText.getText().toString());
        user.put("liensPublics", liensPublicsEmployeurEditText.getText().toString());
        user.put("password", passwordEmployeurEditText.getText().toString());

        selectedId = radioGroup.getCheckedRadioButtonId();
        selectedRadioButton = findViewById(selectedId);
        user.put("typeUtilisateur", selectedRadioButton.getText().toString());

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    // Document added successfully
                    Toast.makeText(getApplicationContext(), "Creation successful", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Creation fail !", Toast.LENGTH_SHORT).show();
                });
    }
}