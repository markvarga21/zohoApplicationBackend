package io.tis.zoho.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ZohoClientResponse {
    @JsonProperty("response")
    private Response response;

    @Data
    public class Response {
        @JsonProperty("result")
        private List<ZohoClient> result;
    }
}
