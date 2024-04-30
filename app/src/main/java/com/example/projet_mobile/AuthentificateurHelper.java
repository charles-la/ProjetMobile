package com.example.projet_mobile;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
public class AuthentificateurHelper {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void verifyUserCredentials(String email, String password, AuthCallback callback) {
        db.collection("candidat")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                String storedPassword = document.getString("password");
                                if (storedPassword != null && storedPassword.equals(password)) {
                                    // Le mot de passe correspond, l'utilisateur peut se connecter
                                    callback.onSuccess();
                                } else {
                                    // Le mot de passe ne correspond pas
                                    callback.onFailure("Incorrect password");
                                }
                            }
                        } else {
                            // Aucun document correspondant à l'e-mail
                            callback.onFailure("Email does not exist");
                        }
                    } else {
                        // Échec de la requête, gérer l'erreur
                        callback.onFailure("Failed to query database: " + task.getException().getMessage());
                    }
                });
    }
}
