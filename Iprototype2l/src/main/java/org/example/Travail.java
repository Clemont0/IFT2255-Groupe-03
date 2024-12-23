package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Travail {

    @JsonProperty("id") // Correspond à "id" dans le JSON
    private String id;

    @JsonProperty("boroughid")
    private String boroughId;

    @JsonProperty("currentstatus")
    private String currentStatus;

    @JsonProperty("reason_category")
    private String reasonCategory;

    @JsonProperty("submittercategory")
    private String submitterCategory;

    @JsonProperty("organizationname")
    private String organizationName;


    public Travail() {

    }

    public Travail(String id, String boroughId, String currentStatus, String reasonCategory,
                   String submitterCategory, String organizationName) {
        this.id = id;
        this.boroughId = boroughId;
        this.currentStatus = currentStatus;
        this.reasonCategory = reasonCategory;
        this.submitterCategory = submitterCategory;
        this.organizationName = organizationName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBoroughId() {
        return boroughId;
    }

    public void setBoroughId(String boroughId) {
        this.boroughId = boroughId;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getReasonCategory() {
        return reasonCategory;
    }

    public void setReasonCategory(String reasonCategory) {
        this.reasonCategory = reasonCategory;
    }

    public String getSubmitterCategory() {
        return submitterCategory;
    }

    public void setSubmitterCategory(String submitterCategory) {
        this.submitterCategory = submitterCategory;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
}
