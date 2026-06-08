package com.greenhouse.model;

import java.time.LocalDate;

public class Rack {

    private Integer id;
    private LocalDate installationDate;
    private Integer tierCount;
    private String name;

    public Rack() {
    }

    public Rack(Integer id,
                LocalDate installationDate,
                Integer tierCount,
                String name) {
        this.id = id;
        this.installationDate = installationDate;
        this.tierCount = tierCount;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(LocalDate installationDate) {
        this.installationDate = installationDate;
    }

    public Integer getTierCount() {
        return tierCount;
    }

    public void setTierCount(Integer tierCount) {
        this.tierCount = tierCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Rack{" +
                "id=" + id +
                ", installationDate=" + installationDate +
                ", tierCount=" + tierCount +
                ", name='" + name + '\'' +
                '}';
    }
}
