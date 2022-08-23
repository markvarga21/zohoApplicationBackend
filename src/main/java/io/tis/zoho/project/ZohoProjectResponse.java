package io.tis.zoho.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.tis.zoho.job.ZohoJob;
import io.tis.zoho.job.ZohoJobResponse;
import lombok.Data;

import java.util.List;

@Data
public class ZohoProjectResponse {
    @JsonProperty("response")
    private Response response;

    @Data
    public class Response {
        @JsonProperty("result")
        private List<ZohoProject> result;
    }
}
