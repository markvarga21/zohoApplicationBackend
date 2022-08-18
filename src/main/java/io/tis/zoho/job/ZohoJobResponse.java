package io.tis.zoho.job;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ZohoJobResponse {
    @JsonProperty("response")
    private ZohoJobResponseList zohoJobResponseList;
}
