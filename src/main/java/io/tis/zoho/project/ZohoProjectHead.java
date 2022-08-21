package io.tis.zoho.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class ZohoProjectHead {
    @JsonProperty("empId")
    @Id
    private String empId;
    @JsonProperty("erecno")
    private String erecno;
    @JsonProperty("rate")
    private Integer rate;
    @JsonProperty("name")
    private String name;
}
