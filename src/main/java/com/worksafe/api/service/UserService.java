package com.worksafe.api.service;

import com.worksafe.api.entity.User;
import com.worksafe.api.entity.UserRole;
import com.worksafe.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String name,
                         String email,
                         String rawPassword,
                         String roleString) {

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("E-mail obrigatório");
        }
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Senha obrigatória");
        }

        Optional<User> existing = userRepository.findByEmail(email);
        if (existing.isPresent()) {
            throw new IllegalArgumentException("E-mail já cadastrado");
        }

        UserRole role = parseRole(roleString);

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);

        return userRepository.save(user);
    }

    public Page<User> listUsers(String name, UserRole role, Pageable pageable) {
        boolean hasName = name != null && !name.trim().isEmpty();
        boolean hasRole = role != null;

        if (hasName && hasRole) {
            return userRepository
                    .findByNameContainingIgnoreCaseAndRole(name, role, pageable);
        } else if (hasName) {
            return userRepository
                    .findByNameContainingIgnoreCase(name, pageable);
        } else if (hasRole) {
            return userRepository
                    .findByRole(role, pageable);
        } else {
            return userRepository.findAll(pageable);
        }
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException("User não encontrado: " + id));
    }

    public User update(Long id, String name, UserRole role) {
        User user = getById(id);

        if (name != null && !name.trim().isEmpty()) {
            user.setName(name);
        }
        if (role != null) {
            user.setRole(role);
        }

        return userRepository.save(user);
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("User não encontrado: " + id);
        }
        userRepository.deleteById(id);
    }


    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new NoSuchElementException("User não encontrado com e-mail: " + email));
    }

    private UserRole parseRole(String roleString) {
        if (roleString == null || roleString.trim().isEmpty()) {
            return UserRole.WORKER; // default
        }
        try {
            return UserRole.valueOf(roleString.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return UserRole.WORKER;
        }
    }
}
