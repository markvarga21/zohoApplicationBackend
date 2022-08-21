package io.tis.zoho.job;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class ZohoJob {
    @JsonProperty("jobName")
    private String jobName;
    @JsonProperty("owner")
    private String owner;
    @JsonProperty("jobStatus")
    private String jobStatus;
    @JsonProperty("hours")
    private String hours;
    @JsonProperty("assignedBy")
    private String assignedBy;
    @JsonProperty("clientId")
    private String clientId;
    @JsonProperty("clientName")
    private String clientName;
    @JsonProperty("isDeleteAllowed")
    private Boolean isDeleteAllowed;
    @JsonProperty("projectHead")
    private String projectHead;
    @JsonProperty("isActive")
    private Boolean isActive;
    @JsonProperty("fromDate")
    private String fromDate;
    @JsonProperty("jobId")
    @Id
    private String jobId;
    @JsonProperty("jobcolor")
    private Integer jobcolor;
    @JsonProperty("ratePerHour")
    private Integer ratePerHour;
    @JsonProperty("totalhours")
    private String totalhours;
    @JsonProperty("projectName")
    private String projectName;
    @JsonProperty("projectId")
    private String projectId;
}
