package com.example.projet_mobile;

public interface AuthCallback {
    void onSuccessCandidat();
    void onSuccessEmployeur();
    void onFailure(String message);
}