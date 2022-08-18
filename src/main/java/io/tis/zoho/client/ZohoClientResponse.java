package io.tis.zoho.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ZohoClientResponse {
    @JsonProperty("response")
    private ZohoClientResponseList zohoClientResponseList;
}
