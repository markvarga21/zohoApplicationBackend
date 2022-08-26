package io.tis.zoho.converter;

import com.google.gson.Gson;
import io.tis.zoho.client.ZohoClient;
import io.tis.zoho.client.ZohoClientResponse;
import io.tis.zoho.job.ZohoJob;
import io.tis.zoho.job.ZohoJobResponse;
import io.tis.zoho.project.ZohoProject;
import io.tis.zoho.project.ZohoProjectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ZohoResponseConverter {
    private final Gson gson;
    public List<ZohoClient> convertZohoClientListToEntityList(String zohoClientJsonString) {
        return this.gson
                .fromJson(zohoClientJsonString, ZohoClientResponse.class)
                .getResponse()
                .getResult();
    }

    public List<ZohoJob> convertZohoJobListToEntityList(String jobsJsonString) {
        return this.gson
                .fromJson(jobsJsonString, ZohoJobResponse.class)
                .getResponse()
                .getResult();
    }

    public List<ZohoProject> convertZohoProjectListToEntityList(String projectsJsonString) {
        return this.gson
                .fromJson(projectsJsonString, ZohoProjectResponse.class)
                .getResponse()
                .getResult();
    }
}
