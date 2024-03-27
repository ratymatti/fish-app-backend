package com.of.fishapp.util;

import com.of.fishapp.dto.IdToken;

public class IdTokenUtil {

    public static void removeBearerPrefix(IdToken idToken) {
        String token = idToken.getIdToken();
        if (token != null && token.startsWith("Bearer ")) {
            idToken.setIdToken(token.substring(7));
        }
    }
}