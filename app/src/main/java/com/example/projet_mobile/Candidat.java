package com.example.projet_mobile;


public class Candidat {
    private String userId;
    private String etat;

    public Candidat() {
        // Firestore requires a no-argument constructor
    }

    public Candidat(String userId, String etat) {
        this.userId = userId;
        this.etat = etat;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }
}

