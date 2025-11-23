package com.worksafe.api.repository;

import com.worksafe.api.entity.Alert;
import com.worksafe.api.entity.AlertType;
import com.worksafe.api.entity.Severity;
import com.worksafe.api.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertRepository extends JpaRepository<Alert, Long> {

    Page<Alert> findByUser(User user, Pageable pageable);

    Page<Alert> findByType(AlertType type, Pageable pageable);

    Page<Alert> findBySeverity(Severity severity, Pageable pageable);

    Page<Alert> findByUserAndSeverity(User user, Severity severity, Pageable pageable);
}
