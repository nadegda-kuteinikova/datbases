package com.greenhouse.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class ChangeHistory {

    private Long id;
    private Integer sensorId;
    private BigDecimal value;
    private OffsetDateTime recordedAt;

    public ChangeHistory() {
    }

    public ChangeHistory(Long id,
                         Integer sensorId,
                         BigDecimal value,
                         OffsetDateTime recordedAt) {
        this.id = id;
        this.sensorId = sensorId;
        this.value = value;
        this.recordedAt = recordedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSensorId() {
        return sensorId;
    }

    public void setSensorId(Integer sensorId) {
        this.sensorId = sensorId;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public OffsetDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(OffsetDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }

    @Override
    public String toString() {
        return "ChangeHistory{" +
                "id=" + id +
                ", sensorId=" + sensorId +
                ", value=" + value +
                ", recordedAt=" + recordedAt +
                '}';
    }
}
