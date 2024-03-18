package com.of.fishapp.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.of.fishapp.entity.User;
import com.of.fishapp.exception.EntityNotFoundException;
import com.of.fishapp.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User getUser(UUID id) {
        if (id == null) throw new IllegalArgumentException("ID cannot be null");
        Optional<User> user = userRepository.findById(id);
        return unwrapUser(user, Optional.ofNullable(id));
    }

    @Override
    public User getUser(String username) {
        if (username == null) throw new EntityNotFoundException(username, User.class);
        Optional<User> user = userRepository.findByUsername(username);
        return unwrapUser(user, Optional.empty());
    }

    @Override
    public User saveUser(User user) {
        if (user.getPassword() != null) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        } else {
            throw new IllegalArgumentException("Password cannot be null");
        }
        return userRepository.save(user);
    }

    static User unwrapUser(Optional<User> entity, Optional<UUID> id) {
        if (entity.isPresent()) {
            return entity.get();
        } else {
                throw new EntityNotFoundException(id.get(), User.class);    
        }
    }
}
