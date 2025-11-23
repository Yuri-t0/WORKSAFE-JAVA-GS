package com.worksafe.api.service;

import com.worksafe.api.entity.*;
import com.worksafe.api.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
public class AlertService {

    private final AlertRepository repository;
    private final UserService userService;
    private final WorkstationService workstationService;

    @Autowired
    public AlertService(AlertRepository repository,
                        UserService userService,
                        WorkstationService workstationService) {
        this.repository = repository;
        this.userService = userService;
        this.workstationService = workstationService;
    }

    public Page<Alert> list(Long userId,
                            AlertType type,
                            Severity severity,
                            Pageable pageable) {

        if (userId != null && severity != null) {
            User user = userService.getById(userId);
            return repository.findByUserAndSeverity(user, severity, pageable);
        }

        if (userId != null) {
            User user = userService.getById(userId);
            return repository.findByUser(user, pageable);
        }

        if (type != null) {
            return repository.findByType(type, pageable);
        }

        if (severity != null) {
            return repository.findBySeverity(severity, pageable);
        }

        return repository.findAll(pageable);
    }

    public Alert getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Alert not found: " + id));
    }

    public Alert create(Long userId,
                        Long workstationId,
                        AlertType type,
                        Severity severity,
                        String message,
                        LocalDateTime createdAt) {

        User user = null;
        if (userId != null) {
            user = userService.getById(userId);
        }

        Workstation workstation = null;
        if (workstationId != null) {
            workstation = workstationService.getById(workstationId);
        }

        Alert alert = new Alert(
                null,
                user,
                workstation,
                type,
                severity,
                message,
                createdAt != null ? createdAt : LocalDateTime.now()
        );

        return repository.save(alert);
    }

    public Alert update(Long id,
                        Long userId,
                        Long workstationId,
                        AlertType type,
                        Severity severity,
                        String message) {

        Alert alert = getById(id);

        if (userId != null) {
            alert.setUser(userService.getById(userId));
        }

        if (workstationId != null) {
            alert.setWorkstation(workstationService.getById(workstationId));
        }

        if (type != null) {
            alert.setType(type);
        }

        if (severity != null) {
            alert.setSeverity(severity);
        }

        if (message != null && !message.trim().isEmpty()) {
            alert.setMessage(message);
        }

        return repository.save(alert);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Alert not found: " + id);
        }
        repository.deleteById(id);
    }
}
