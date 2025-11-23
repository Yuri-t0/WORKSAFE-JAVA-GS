package com.worksafe.api.repository;

import com.worksafe.api.entity.Workstation;
import com.worksafe.api.entity.ErgonomicLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkstationRepository extends JpaRepository<Workstation, Long> {

    Page<Workstation> findBySectorContainingIgnoreCase(String sector, Pageable pageable);

    Page<Workstation> findByErgonomicLevel(ErgonomicLevel level, Pageable pageable);

    Page<Workstation> findBySectorContainingIgnoreCaseAndErgonomicLevel(
            String sector,
            ErgonomicLevel level,
            Pageable pageable
    );
}
