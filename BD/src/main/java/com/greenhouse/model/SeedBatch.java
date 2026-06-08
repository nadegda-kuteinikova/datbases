package com.greenhouse.model;

import java.time.LocalDate;

public class SeedBatch {

    private Integer id;
    private Integer cultureId;
    private Integer supplierId;
    private LocalDate deliveryDate;
    private Double germinationPercent;

    public SeedBatch() {
    }

    public SeedBatch(Integer id,
                     Integer cultureId,
                     Integer supplierId,
                     LocalDate deliveryDate,
                     Double germinationPercent) {
        this.id = id;
        this.cultureId = cultureId;
        this.supplierId = supplierId;
        this.deliveryDate = deliveryDate;
        this.germinationPercent = germinationPercent;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCultureId() {
        return cultureId;
    }

    public void setCultureId(Integer cultureId) {
        this.cultureId = cultureId;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Double getGerminationPercent() {
        return germinationPercent;
    }

    public void setGerminationPercent(Double germinationPercent) {
        this.germinationPercent = germinationPercent;
    }

    @Override
    public String toString() {
        return "SeedBatch{" +
                "id=" + id +
                ", cultureId=" + cultureId +
                ", supplierId=" + supplierId +
                ", deliveryDate=" + deliveryDate +
                ", germinationPercent=" + germinationPercent +
                '}';
    }
}
