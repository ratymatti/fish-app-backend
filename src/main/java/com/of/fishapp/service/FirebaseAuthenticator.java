package com.of.fishapp.service;

import org.springframework.stereotype.Service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.of.fishapp.dto.IdToken;
import com.of.fishapp.dto.UserDetails;
import com.google.firebase.auth.FirebaseAuthException;

@Service
public class FirebaseAuthenticator {

    private final FirebaseAuth firebaseAuth;

    public FirebaseAuthenticator() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public boolean verifyIdToken(IdToken idToken) {
        String idTokenString = idToken.getIdToken();
        try {
            firebaseAuth.verifyIdToken(idTokenString);
            return true;
        } catch (FirebaseAuthException e) {
            return false;
        }
    }

    public String getUidFromToken(IdToken idToken) throws FirebaseAuthException {
        String idTokenString = idToken.getIdToken();
        return firebaseAuth.verifyIdToken(idTokenString).getUid();
    }

    public UserDetails getUserDetails(IdToken idToken) throws FirebaseAuthException {
        String idTokenString = idToken.getIdToken();
        FirebaseToken token = firebaseAuth.verifyIdToken(idTokenString);
        return new UserDetails(token.getUid(), token.getName(), token.getEmail());
    }

}
