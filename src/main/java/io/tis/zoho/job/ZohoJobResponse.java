package io.tis.zoho.job;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.tis.zoho.client.ZohoClient;
import io.tis.zoho.client.ZohoClientResponse;
import lombok.Data;

import java.util.List;

@Data
public class ZohoJobResponse {
    @JsonProperty("response")
    private Response response;

    @Data
    public class Response {
        @JsonProperty("result")
        private List<ZohoJob> result;
    }
}
