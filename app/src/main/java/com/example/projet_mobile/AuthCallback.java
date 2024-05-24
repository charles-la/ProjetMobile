package com.example.projet_mobile;

public interface AuthCallback {
    void onSuccessCandidat(String userId);
    void onSuccessEmployeur(String userId);
    void onFailure(String message);
}