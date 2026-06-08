package com.greenhouse.model;


public class Supplier {

    private Integer id;
    private String companyName;
    private String contactInfo;
    private Double rating;

    public Supplier() {
    }

    public Supplier(Integer id,
                    String companyName,
                    String contactInfo,
                    Double rating) {
        this.id = id;
        this.companyName = companyName;
        this.contactInfo = contactInfo;
        this.rating = rating;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "id=" + id +
                ", companyName='" + companyName + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                ", rating=" + rating +
                '}';
    }
}
