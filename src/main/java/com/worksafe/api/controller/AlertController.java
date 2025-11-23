package com.worksafe.api.controller;

import com.worksafe.api.entity.Alert;
import com.worksafe.api.entity.AlertType;
import com.worksafe.api.entity.Severity;
import com.worksafe.api.service.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/alerts")
@Tag(name = "Alerts", description = "Gerenciamento de alertas ergonômicos e de segurança")
@SecurityRequirement(name = "bearerAuth")
public class AlertController {

    private final AlertService service;

    @Autowired
    public AlertController(AlertService service) {
        this.service = service;
    }

    // DTO de requisição (criação/atualização)
    public static class AlertRequestDTO {
        private Long userId;
        private Long workstationId;
        private AlertType type;
        private Severity severity;
        private String message;
        private LocalDateTime createdAt;

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }

        public Long getWorkstationId() { return workstationId; }
        public void setWorkstationId(Long workstationId) { this.workstationId = workstationId; }

        public AlertType getType() { return type; }
        public void setType(AlertType type) { this.type = type; }

        public Severity getSeverity() { return severity; }
        public void setSeverity(Severity severity) { this.severity = severity; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    }

    // DTO de resposta
    public static class AlertResponseDTO {
        private Long id;
        private Long userId;
        private Long workstationId;
        private AlertType type;
        private Severity severity;
        private String message;
        private LocalDateTime createdAt;

        public AlertResponseDTO() {}

        public AlertResponseDTO(Alert alert) {
            this.id = alert.getId();
            this.userId = alert.getUser() != null ? alert.getUser().getId() : null;
            this.workstationId = alert.getWorkstation() != null ? alert.getWorkstation().getId() : null;
            this.type = alert.getType();
            this.severity = alert.getSeverity();
            this.message = alert.getMessage();
            this.createdAt = alert.getCreatedAt();
        }

        public Long getId() { return id; }
        public Long getUserId() { return userId; }
        public Long getWorkstationId() { return workstationId; }
        public AlertType getType() { return type; }
        public Severity getSeverity() { return severity; }
        public String getMessage() { return message; }
        public LocalDateTime getCreatedAt() { return createdAt; }
    }

    @Operation(
            summary = "Listar alertas",
            description = "Retorna uma página de alertas, com filtros opcionais por usuário, tipo e severidade."
    )
    @GetMapping
    public ResponseEntity<Page<AlertResponseDTO>> list(
            @RequestParam(name = "userId", required = false) Long userId,
            @RequestParam(name = "type", required = false) AlertType type,
            @RequestParam(name = "severity", required = false) Severity severity,
            @ParameterObject Pageable pageable
    ) {
        Page<AlertResponseDTO> page = service
                .list(userId, type, severity, pageable)
                .map(AlertResponseDTO::new);

        return ResponseEntity.ok(page);
    }

    @Operation(
            summary = "Buscar alerta por ID",
            description = "Retorna os detalhes de um alerta específico."
    )
    @GetMapping("/{id}")
    public ResponseEntity<AlertResponseDTO> getById(@PathVariable Long id) {
        Alert alert = service.getById(id);
        return ResponseEntity.ok(new AlertResponseDTO(alert));
    }

    @Operation(
            summary = "Criar novo alerta",
            description = "Cria um novo alerta vinculado a um usuário e/ou estação de trabalho."
    )
    @PostMapping
    public ResponseEntity<AlertResponseDTO> create(@RequestBody AlertRequestDTO body) {
        Alert alert = service.create(
                body.getUserId(),
                body.getWorkstationId(),
                body.getType(),
                body.getSeverity(),
                body.getMessage(),
                body.getCreatedAt()
        );
        return ResponseEntity.ok(new AlertResponseDTO(alert));
    }

    @Operation(
            summary = "Atualizar alerta",
            description = "Atualiza os dados de um alerta existente."
    )
    @PutMapping("/{id}")
    public ResponseEntity<AlertResponseDTO> update(
            @PathVariable Long id,
            @RequestBody AlertRequestDTO body
    ) {
        Alert alert = service.update(
                id,
                body.getUserId(),
                body.getWorkstationId(),
                body.getType(),
                body.getSeverity(),
                body.getMessage()
        );
        return ResponseEntity.ok(new AlertResponseDTO(alert));
    }

    @Operation(
            summary = "Excluir alerta",
            description = "Remove um alerta pelo seu identificador."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
