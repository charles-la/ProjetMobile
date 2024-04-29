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

public class Connection extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        Button buttonConnection = findViewById(R.id.connection_button);
        Button buttonAnonyme = findViewById(R.id.anonyme_button);
        Button buttonAccountCreation = findViewById(R.id.account_creation_button);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
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
                startActivity(new Intent(Connection.this, AccueilCandidat.class));
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