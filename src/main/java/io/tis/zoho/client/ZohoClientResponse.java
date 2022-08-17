package io.tis.zoho.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ZohoClientResponse {
    @JsonProperty("result")
    private List<ZohoClient> result = null;
    @JsonProperty("message")
    private String message;
    @JsonProperty("uri")
    private String uri;
    @JsonProperty("status")
    private Integer status;
}
