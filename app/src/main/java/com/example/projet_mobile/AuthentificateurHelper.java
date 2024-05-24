package com.example.projet_mobile;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AuthentificateurHelper {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void verifyUserCredentials(String email, String password, AuthCallback callback) {
        db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                String storedPassword = document.getString("password");
                                String userType = document.getString("typeUtilisateur");
                                String userId = document.getId(); // Get the document ID

                                if (storedPassword != null && storedPassword.equals(password)) {
                                    // Check user type and call the appropriate onSuccess method
                                    if ("Candidat".equals(userType)) {
                                        callback.onSuccessCandidat(userId);
                                    } else if ("Employeur".equals(userType)) {
                                        callback.onSuccessEmployeur(userId);
                                    } else {
                                        // User type is neither Candidat nor Employeur or is undefined
                                        callback.onFailure("User type is undefined or incorrect.");
                                    }
                                } else {
                                    // The password does not match
                                    callback.onFailure("Incorrect password");
                                }
                            }
                        } else {
                            // No document found with the provided email
                            callback.onFailure("Email does not exist");
                        }
                    } else {
                        // Failed to query the database
                        callback.onFailure("Failed to query database: " + task.getException().getMessage());
                    }
                });
    }
}
