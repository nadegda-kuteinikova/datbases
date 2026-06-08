package com.greenhouse.model;

public class SeedCulture {

    private Integer id;
    private String idealConditions;
    private String variety;
    private String name;

    public SeedCulture() {
    }

    public SeedCulture(Integer id,
                       String idealConditions,
                       String variety,
                       String name) {
        this.id = id;
        this.idealConditions = idealConditions;
        this.variety = variety;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdealConditions() {
        return idealConditions;
    }

    public void setIdealConditions(String idealConditions) {
        this.idealConditions = idealConditions;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SeedCulture{" +
                "id=" + id +
                ", idealConditions='" + idealConditions + '\'' +
                ", variety='" + variety + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}