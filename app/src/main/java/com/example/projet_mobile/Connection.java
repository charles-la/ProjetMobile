package com.example.projet_mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.Manifest;
import android.widget.EditText;
import android.widget.Toast;

public class Connection extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1;
    private EditText emailEditText;
    private EditText passwordEditText;
    private AuthentificateurHelper authHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        Button buttonConnection = findViewById(R.id.connection_button);
        Button buttonAnonyme = findViewById(R.id.anonyme_button);
        Button buttonAccountCreation = findViewById(R.id.account_creation_button);

        authHelper = new AuthentificateurHelper();

        // Get Email
        emailEditText = findViewById(R.id.email_edit_text);
        // Get Password
        passwordEditText = findViewById(R.id.mot_de_passe_edit_text);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // sees the explanation, try again to request the permission.
                showExplanationDialog();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            }
        } else {
            // Permission has already been granted
            //initLocationAccess();
        }

        buttonConnection.setOnClickListener(new View.OnClickListener() {
            // Ajouter des verifications avec la base de donnees pour voir si lutilisateur existe
            @Override
            public void onClick(View v) {
                String userEmail = emailEditText.getText().toString();
                String userPassword = passwordEditText.getText().toString();

                authHelper.verifyUserCredentials(userEmail, userPassword, new AuthCallback() {
                    @Override
                    public void onSuccessCandidat() {
                        // Logic when login is successful
                        Toast.makeText(getApplicationContext(), "Connection candidat successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Connection.this, AccueilCandidat.class));
                    }
                    public void onSuccessEmployeur() {
                        // Logic when login is successful
                        Toast.makeText(getApplicationContext(), "Connection employeur successful", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String message) {
                        // Logic when login fails, handle error message
                        Toast.makeText(getApplicationContext(), "Connection fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        buttonAnonyme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Connection.this, AccueilCandidat.class));
            }
        });

        buttonAccountCreation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Connection.this, AccountCreation.class));
            }
        });
    }

    private void showExplanationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Location Permission Needed")
                .setMessage("This app needs the location permission to provide location-based services. Please grant the permission.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(Connection.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }
}