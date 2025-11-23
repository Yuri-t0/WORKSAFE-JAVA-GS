package com.worksafe.api.service;

import com.worksafe.api.entity.Workstation;
import com.worksafe.api.entity.ErgonomicLevel;
import com.worksafe.api.repository.WorkstationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class WorkstationService {

    private final WorkstationRepository repository;

    @Autowired
    public WorkstationService(WorkstationRepository repository) {
        this.repository = repository;
    }

    public Workstation create(Workstation w) {
        return repository.save(w);
    }

    public Page<Workstation> list(String sector, ErgonomicLevel level, Pageable pageable) {
        boolean hasSector = sector != null && !sector.trim().isEmpty();
        boolean hasLevel = level != null;

        if (hasSector && hasLevel) {
            return repository.findBySectorContainingIgnoreCaseAndErgonomicLevel(sector, level, pageable);
        } else if (hasSector) {
            return repository.findBySectorContainingIgnoreCase(sector, pageable);
        } else if (hasLevel) {
            return repository.findByErgonomicLevel(level, pageable);
        } else {
            return repository.findAll(pageable);
        }
    }

    public Workstation getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Workstation not found: " + id));
    }

    public Workstation update(Long id, Workstation patch) {
        Workstation w = getById(id);

        if (patch.getCode() != null) w.setCode(patch.getCode());
        if (patch.getSector() != null) w.setSector(patch.getSector());
        if (patch.getErgonomicLevel() != null) w.setErgonomicLevel(patch.getErgonomicLevel());

        w.setActive(patch.isActive());

        return repository.save(w);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Workstation not found: " + id);
        }
        repository.deleteById(id);
    }
}
