package com.ioriocars.service;

import com.ioriocars.domain.User;
import com.ioriocars.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Recupera tutti gli utenti
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Recupera utente per email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Crea un nuovo utente
    public User createUser(String email, String rawPassword, String role) {
        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .role(role)
                .build();
        return userRepository.save(user);
    }

    // Aggiorna utente
    public User updateUser(Long id, String email, String rawPassword, String role) {
        Optional<User> opt = userRepository.findById(id);
        if (opt.isPresent()) {
            User user = opt.get();
            user.setEmail(email);
            if (rawPassword != null && !rawPassword.isEmpty()) {
                user.setPassword(passwordEncoder.encode(rawPassword));
            }
            user.setRole(role);
            return userRepository.save(user);
        }
        return null;
    }

    // Cancella utente
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
