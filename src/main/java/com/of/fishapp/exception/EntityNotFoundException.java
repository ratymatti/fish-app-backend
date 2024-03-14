package com.of.fishapp.exception;

import java.util.Optional;
import java.util.UUID;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(UUID id, Class<?> entity) {
        super("The " + entity.getSimpleName().toLowerCase() + " with id '" + id + "' does not exist in our records");
    }

    public EntityNotFoundException(String username, Class<?> entity) {
        super("The " + entity.getSimpleName().toLowerCase() + " with username '" + username + "' does not exist in our records");
    }

    public EntityNotFoundException(Optional<UUID> id, Class<?> entity) {
        super("The " + entity.getSimpleName().toLowerCase() + " with id '" + id + "' does not exist in our records");
    }
}
