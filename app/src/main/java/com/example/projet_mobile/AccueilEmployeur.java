package com.example.projet_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AccueilEmployeur extends AppCompatActivity {

    private Button buttonOffreCreation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil_employeur);

        buttonOffreCreation = findViewById(R.id.buttonCreateOffre);

        buttonOffreCreation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccueilEmployeur.this, OffreCreation.class));
            }
        });
    }
}