package com.worksafe.api.controller;

import com.worksafe.api.entity.User;
import com.worksafe.api.security.jwt.JwtService;
import com.worksafe.api.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final UserDetailsService userDetailsService;

    public AuthController(UserService userService,
                          JwtService jwtService,
                          AuthenticationManager authManager,
                          UserDetailsService userDetailsService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authManager = authManager;
        this.userDetailsService = userDetailsService;
    }

    // ---------- DTOs internos ----------

    public static class AuthRequestDTO {
        private String email;
        private String password;

        public AuthRequestDTO() {}

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class RegisterRequestDTO {
        private String name;
        private String email;
        private String password;
        private String role; // WORKER, MANAGER, ADMIN

        public RegisterRequestDTO() {}

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }

    public static class AuthResponseDTO {
        private String token;

        public AuthResponseDTO() {}
        public AuthResponseDTO(String token) { this.token = token; }

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
    }

    // ---------- Endpoints ----------

    @PostMapping("/register")
    public User register(@RequestBody RegisterRequestDTO dto) {
        return userService.register(
                dto.getName(),
                dto.getEmail(),
                dto.getPassword(),
                dto.getRole()
        );
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody AuthRequestDTO dto) {
        // Autentica usuário
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        // Carrega os detalhes do usuário
        UserDetails userDetails =
                userDetailsService.loadUserByUsername(dto.getEmail());

        // Gera token com UserDetails, não com String
        String token = jwtService.generateToken(userDetails);

        return new AuthResponseDTO(token);
    }
}
