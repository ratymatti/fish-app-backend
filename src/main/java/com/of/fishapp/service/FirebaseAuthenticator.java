package com.of.fishapp.service;

import org.springframework.stereotype.Service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;

import com.google.firebase.auth.FirebaseAuthException;

@Service
public class FirebaseAuthenticator {

    private final FirebaseAuth firebaseAuth;

    public FirebaseAuthenticator() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public boolean verifyIdToken(String idToken) {
        try {
            firebaseAuth.verifyIdToken(idToken);
            return true;
        } catch (FirebaseAuthException e) {
            return false;
        }
    }

    public String getUidFromToken(String idToken) throws FirebaseAuthException {
        FirebaseToken token = firebaseAuth.verifyIdToken(idToken);
        return token.getUid();
    }

}
