package io.tis.zoho.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZohoClient {
    @JsonProperty("streetAddr")
    private String streetAddr;
    @JsonProperty("clientId")
    private String clientId;
    @JsonProperty("clientName")
    private String clientName;
    @JsonProperty("city")
    private String city;
    @JsonProperty("billingMethod")
    private String billingMethod;
    @JsonProperty("emailId")
    private String emailId;
    @JsonProperty("mobileNo")
    private String mobileNo;
    @JsonProperty("phoneNo")
    private String phoneNo;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("faxNo")
    private String faxNo;
    @JsonProperty("createdBy")
    private String createdBy;
    @JsonProperty("modifiedBy")
    private String modifiedBy;
    @JsonProperty("currencyCode")
    private String currencyCode;
}
