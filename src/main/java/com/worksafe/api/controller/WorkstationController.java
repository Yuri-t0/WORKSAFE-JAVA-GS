package com.worksafe.api.controller;

import com.worksafe.api.entity.Workstation;
import com.worksafe.api.entity.ErgonomicLevel;
import com.worksafe.api.service.WorkstationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workstations")
public class WorkstationController {

    private final WorkstationService service;

    @Autowired
    public WorkstationController(WorkstationService service) {
        this.service = service;
    }

    // DTOs
    public static class WorkstationRequestDTO {
        public String code;
        public String sector;
        public ErgonomicLevel ergonomicLevel;
        public boolean active;
    }

    public static class WorkstationResponseDTO {
        public Long id;
        public String code;
        public String sector;
        public ErgonomicLevel ergonomicLevel;
        public boolean active;

        public WorkstationResponseDTO(Workstation w) {
            this.id = w.getId();
            this.code = w.getCode();
            this.sector = w.getSector();
            this.ergonomicLevel = w.getErgonomicLevel();
            this.active = w.isActive();
        }
    }

    // CREATE
    @PostMapping
    public ResponseEntity<WorkstationResponseDTO> create(
            @RequestBody WorkstationRequestDTO body
    ) {
        Workstation entity = new Workstation(
                null,
                body.code,
                body.sector,
                body.ergonomicLevel,
                body.active
        );

        return ResponseEntity.ok(
                new WorkstationResponseDTO(service.create(entity))
        );
    }

    // LIST (CORRIGIDO)
    @GetMapping
    public ResponseEntity<Page<WorkstationResponseDTO>> list(
            @RequestParam(name = "sector", required = false) String sector,
            @RequestParam(name = "level", required = false) String levelParam,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "id,asc") String sort
    ) {

        // converte level String → enum
        ErgonomicLevel level = null;
        if (levelParam != null && !levelParam.trim().isEmpty()) {
            try {
                level = ErgonomicLevel.valueOf(levelParam.toUpperCase());
            } catch (Exception ignored) {}
        }

        // sort
        String[] parts = sort.split(",");
        String sortField = parts[0];
        Sort.Direction direction = parts.length > 1
                ? Sort.Direction.fromString(parts[1])
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        return ResponseEntity.ok(
                service.list(sector, level, pageable)
                        .map(WorkstationResponseDTO::new)
        );
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<WorkstationResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                new WorkstationResponseDTO(service.getById(id))
        );
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<WorkstationResponseDTO> update(
            @PathVariable Long id,
            @RequestBody WorkstationRequestDTO body
    ) {
        Workstation patch = new Workstation(
                null,
                body.code,
                body.sector,
                body.ergonomicLevel,
                body.active
        );

        return ResponseEntity.ok(
                new WorkstationResponseDTO(service.update(id, patch))
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
