package com.worksafe.api.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Quem recebeu o alerta
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Em qual estação de trabalho aconteceu
    @ManyToOne
    @JoinColumn(name = "workstation_id")
    private Workstation workstation;

    @Enumerated(EnumType.STRING)
    private AlertType type;

    @Enumerated(EnumType.STRING)
    private Severity severity;

    private String message;

    private LocalDateTime createdAt;

    public Alert() {
        this.createdAt = LocalDateTime.now();
    }

    public Alert(Long id, User user, Workstation workstation,
                 AlertType type, Severity severity, String message,
                 LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.workstation = workstation;
        this.type = type;
        this.severity = severity;
        this.message = message;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }


    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public Workstation getWorkstation() { return workstation; }

    public void setWorkstation(Workstation workstation) {
        this.workstation = workstation;
    }

    public AlertType getType() { return type; }

    public void setType(AlertType type) { this.type = type; }

    public Severity getSeverity() { return severity; }

    public void setSeverity(Severity severity) { this.severity = severity; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
