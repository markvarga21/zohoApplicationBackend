package io.tis.zoho.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ZohoProjectResponseList {
    @JsonProperty("result")
    private List<ZohoProject> result = null;
    @JsonProperty("message")
    private String message;
    @JsonProperty("uri")
    private String uri;
    @JsonProperty("status")
    private Integer status;
}
