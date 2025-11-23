package com.worksafe.api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "workstations")
public class Workstation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String sector;

    @Enumerated(EnumType.STRING)
    private ErgonomicLevel ergonomicLevel;

    private boolean active = true;

    public Workstation() {}

    public Workstation(Long id, String code, String sector,
                       ErgonomicLevel ergonomicLevel, boolean active) {
        this.id = id;
        this.code = code;
        this.sector = sector;
        this.ergonomicLevel = ergonomicLevel;
        this.active = active;
    }


    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }

    public String getSector() { return sector; }

    public void setSector(String sector) { this.sector = sector; }

    public ErgonomicLevel getErgonomicLevel() { return ergonomicLevel; }

    public void setErgonomicLevel(ErgonomicLevel ergonomicLevel) {
        this.ergonomicLevel = ergonomicLevel;
    }

    public boolean isActive() { return active; }

    public void setActive(boolean active) { this.active = active; }
}
