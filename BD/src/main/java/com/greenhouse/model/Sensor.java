package com.greenhouse.model;


import java.time.LocalDate;

public class Sensor {

    private Integer id;
    private Integer cellId;
    private String sensorType;
    private LocalDate calibrationDate;

    public Sensor() {
    }

    public Sensor(Integer id,
                  Integer cellId,
                  String sensorType,
                  LocalDate calibrationDate) {
        this.id = id;
        this.cellId = cellId;
        this.sensorType = sensorType;
        this.calibrationDate = calibrationDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCellId() {
        return cellId;
    }

    public void setCellId(Integer cellId) {
        this.cellId = cellId;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public LocalDate getCalibrationDate() {
        return calibrationDate;
    }

    public void setCalibrationDate(LocalDate calibrationDate) {
        this.calibrationDate = calibrationDate;
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "id=" + id +
                ", cellId=" + cellId +
                ", sensorType='" + sensorType + '\'' +
                ", calibrationDate=" + calibrationDate +
                '}';
    }
}
