package io.tis.zoho.job;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZohoJobResponseList {
    @JsonProperty("result")
    private List<ZohoJob> result = null;
    @JsonProperty("message")
    private String message;
    @JsonProperty("uri")
    private String uri;
    @JsonProperty("status")
    private Integer status;
}
