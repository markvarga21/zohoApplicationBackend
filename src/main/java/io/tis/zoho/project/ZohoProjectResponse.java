package io.tis.zoho.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ZohoProjectResponse {
    @JsonProperty("response")
    private ZohoProjectResponseList zohoProjectResponseList;
}
