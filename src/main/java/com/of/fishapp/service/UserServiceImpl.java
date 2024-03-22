package com.of.fishapp.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.of.fishapp.entity.User;
import com.of.fishapp.exception.EntityNotFoundException;
import com.of.fishapp.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Override
    public User getUser(UUID id) {
        if (id == null) throw new IllegalArgumentException("ID cannot be null");
        Optional<User> user = userRepository.findById(id);
        return unwrapUser(user, Optional.ofNullable(id));
    }

    @Override
    public User saveUser(User user) {
        if (user != null) return userRepository.save(user);
        return null;
    }

    @Override
    public User getUserByGoogleId(String googleId) {
        if (googleId == null) throw new IllegalArgumentException("Google ID cannot be null");
        User user = userRepository.findByGoogleId(googleId);
        return user;
    }

    static User unwrapUser(Optional<User> entity, Optional<UUID> id) {
        if (entity.isPresent()) {
            return entity.get();
        } else {
                throw new EntityNotFoundException(id.get(), User.class);    
        }
    }
}
