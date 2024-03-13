package com.of.fishapp.service;

import java.util.Optional;

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
    public User getUser(Long id) {
        if (id == null) throw new EntityNotFoundException(id, User.class);
        Optional<User> user = userRepository.findById(id);
        return unwrapUser(user, id);
    }

    @Override
    public User getUser(String username) {
        if (username == null) throw new EntityNotFoundException(username, User.class);
        Optional<User> user = userRepository.findByUsername(username);
        return unwrapUser(user, 404L);
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

    static User unwrapUser(Optional<User> entity, Long id) {
        if (entity.isPresent()) return entity.get();
        else throw new EntityNotFoundException(id, User.class);
    }
}
