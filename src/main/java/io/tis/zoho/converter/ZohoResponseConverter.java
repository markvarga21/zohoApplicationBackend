package io.tis.zoho.converter;

import com.google.gson.GsonBuilder;
import io.tis.zoho.client.ZohoClient;
import io.tis.zoho.job.ZohoJob;
import io.tis.zoho.project.ZohoProject;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ZohoResponseConverter {
    public List<ZohoClient> convertZohoClientListToEntityList(String zohoClientJsonString) {
        return Arrays.asList(
                new GsonBuilder()
                .create()
                .fromJson(zohoClientJsonString, ZohoClient[].class));
    }

    public List<ZohoJob> convertZohoJobListToEntityList(String jobsJsonString) {
        return Arrays.asList(
                new GsonBuilder()
                        .create()
                        .fromJson(jobsJsonString, ZohoJob[].class));
    }

    public List<ZohoProject> convertZohoProjectListToEntityList(String projectsJsonString) {
        return Arrays.asList(
                new GsonBuilder()
                        .create()
                        .fromJson(projectsJsonString, ZohoProject[].class));
    }
}
