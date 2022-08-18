package io.tis.zoho.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZohoProject {
    @JsonProperty("projectStatus")
    private String projectStatus;
    @JsonProperty("clientId")
    private String clientId;
    @JsonProperty("ownerName")
    private String ownerName;
    @JsonProperty("projectCost")
    private Double projectCost;
    @JsonProperty("clientName")
    private String clientName;
    @JsonProperty("isDeleteAllowed")
    private Boolean isDeleteAllowed;
    @JsonProperty("projectName")
    private String projectName;
    @JsonProperty("projectHead")
    private ZohoProjectHead projectHead;
    @JsonProperty("ownerId")
    private String ownerId;
    @JsonProperty("projectId")
    private String projectId;
}
