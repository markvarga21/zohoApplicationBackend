package io.tis.zoho.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZohoProjectHead {
    @JsonProperty("empId")
    private String empId;
    @JsonProperty("erecno")
    private String erecno;
    @JsonProperty("rate")
    private Integer rate;
    @JsonProperty("name")
    private String name;
}
