package com.worksafe.api.repository;

import com.worksafe.api.entity.User;
import com.worksafe.api.entity.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Page<User> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<User> findByRole(UserRole role, Pageable pageable);

    Page<User> findByNameContainingIgnoreCaseAndRole(String name, UserRole role, Pageable pageable);
}
