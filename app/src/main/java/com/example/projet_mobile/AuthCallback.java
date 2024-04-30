package com.example.projet_mobile;

public interface AuthCallback {
    void onSuccess();
    void onFailure(String message);
}