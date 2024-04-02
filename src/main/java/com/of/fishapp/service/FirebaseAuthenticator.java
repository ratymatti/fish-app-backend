package com.of.fishapp.service;

import static com.of.fishapp.util.IdTokenUtil.removeBearerPrefix;

import org.springframework.stereotype.Service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.of.fishapp.dto.IdToken;
import com.of.fishapp.dto.UserDetails;
import com.of.fishapp.entity.User;
import com.of.fishapp.exception.EntityNotFoundException;
import com.of.fishapp.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuthException;

@Service
public class FirebaseAuthenticator {

    private FirebaseAuth firebaseAuth;
    private UserService userService;
    private UserRepository userRepository;

    public FirebaseAuthenticator(UserRepository userRepository) {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.userRepository = userRepository;
        this.userService = new UserServiceImpl(this.userRepository);
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

    public User validateUser(IdToken idToken) throws FirebaseAuthException {
        removeBearerPrefix(idToken);
        if (!verifyIdToken(idToken)) {
            throw new IllegalArgumentException("Invalid token");
        }
        String googleId = getUidFromToken(idToken);
        User user = userService.getUserByGoogleId(googleId);
        if (user == null) {
            throw new EntityNotFoundException(User.class);
        }
        return user;
    }
}
