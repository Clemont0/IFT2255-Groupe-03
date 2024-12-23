package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe contenant tout ce qui est relié aux entraves.
 */
public class Entraves {

    @JsonProperty("id_request")
    private String idRequest;

    @JsonProperty("streetid")
    private String streetId;

    @JsonProperty("shortname")
    private String shortName;

    @JsonProperty("streetimpacttype")
    private String streetImpactType;


    public Entraves() {

    }


    public Entraves(String idRequest, String streetId, String shortName, String streetImpactType) {
        this.idRequest = idRequest;
        this.streetId = streetId;
        this.shortName = shortName;
        this.streetImpactType = streetImpactType;
    }

    public String getIdRequest() {
        return idRequest;
    }

    public void setIdRequest(String idRequest) {
        this.idRequest = idRequest;
    }

    public String getStreetId() {
        return streetId;
    }

    public void setStreetId(String streetId) {
        this.streetId = streetId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getStreetImpactType() {
        return streetImpactType;
    }

    public void setStreetImpactType(String streetImpactType) {
        this.streetImpactType = streetImpactType;
    }
}
