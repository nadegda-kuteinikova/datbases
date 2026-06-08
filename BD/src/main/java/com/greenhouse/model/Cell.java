package com.greenhouse.model;

import java.time.LocalDate;

public class Cell {

    private Integer id;
    private Integer rackId;
    private Integer currentCulture;
    private LocalDate plantingDate;

    public Cell() {
    }

    public Cell(Integer id,
                Integer rackId,
                Integer currentCulture,
                LocalDate plantingDate) {
        this.id = id;
        this.rackId = rackId;
        this.currentCulture = currentCulture;
        this.plantingDate = plantingDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRackId() {
        return rackId;
    }

    public void setRackId(Integer rackId) {
        this.rackId = rackId;
    }

    public Integer getCurrentCulture() {
        return currentCulture;
    }

    public void setCurrentCulture(Integer currentCulture) {
        this.currentCulture = currentCulture;
    }

    public LocalDate getPlantingDate() {
        return plantingDate;
    }

    public void setPlantingDate(LocalDate plantingDate) {
        this.plantingDate = plantingDate;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "id=" + id +
                ", rackId=" + rackId +
                ", currentCulture=" + currentCulture +
                ", plantingDate=" + plantingDate +
                '}';
    }
}
