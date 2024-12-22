package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

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

    @JsonProperty("duration_start_date")
    private String duration_start_date;


    public Travail() {

    }

    public Travail(String id, String boroughId, String currentStatus, String reasonCategory,
                   String submitterCategory, String organizationName,  String duration_start_date) {
        this.id = id;
        this.boroughId = boroughId;
        this.currentStatus = currentStatus;
        this.reasonCategory = reasonCategory;
        this.submitterCategory = submitterCategory;
        this.organizationName = organizationName;
        this.duration_start_date = duration_start_date;
    }

    public String getduration_start_date() {
        return duration_start_date;
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

    public void setDuration_start_date(String duration_start_date) {
        this.duration_start_date = duration_start_date;
    }


}
