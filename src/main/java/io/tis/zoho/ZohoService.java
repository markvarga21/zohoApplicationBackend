package io.tis.zoho;

import io.tis.exception.ClientNotFound;
import io.tis.zoho.client.ZohoClientResponse;
import io.tis.zoho.client.ZohoClient;
import io.tis.zoho.dto.TimeLogDTO;
import io.tis.zoho.job.ZohoJob;
import io.tis.zoho.job.ZohoJobResponse;
import io.tis.zoho.project.ZohoProject;
import io.tis.zoho.project.ZohoProjectResponse;
import io.tis.zoho.timelog.DateTimeConverter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Getter
@Slf4j
@RequiredArgsConstructor
public class ZohoService {
    private final DateTimeConverter dateTimeConverter;
    @Value("${zoho.client.id}")
    private String clientId;
    @Value("${zoho.client.secret}")
    private String clientSecret;
    @Value("${zoho.redirect}")
    private String redirectUrl;
    @Value("${base.zoho.url}")
    private String zohoBaseUrl;
    @Value("${base.zoho.people.url}")
    private String zohoPeopleBaseUrl;
    @Value("${user.email}")
    private String userEmail;

    public String generateRefreshToken(String code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", this.clientId);
        formData.add("client_secret", this.clientSecret);
        formData.add("redirect_uri", this.redirectUrl);
        formData.add("code", code);

        WebClient webClient1 = WebClient.create(zohoBaseUrl);
        var response = webClient1
                .post()
                .uri("oauth/v2/token")
                .body(BodyInserters.fromMultipartData(formData))
                .retrieve()
                .bodyToMono(ZohoToken.class)
                .block();
        log.info(response.getRefreshToken());
        return response.getRefreshToken();
    }

    private String generateAccessToken(String refreshToken) {
        return "1000.0e4bb61794dffe240b3d29b6e66f4bb9.c465ac41d229b01443e9d93ae5a0956c";
    }

    private HttpEntity<?> generateRequestEntity(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Zoho-oauthtoken " + accessToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        return entity;
    }

    private String generateRequestUrl(String path) {
        return this.zohoPeopleBaseUrl + path;
    }

    public List<String> getClients(String refreshToken) {
        String accessToken = this.generateAccessToken(refreshToken);
        return this.getAllClientInformation(accessToken)
                .stream()
                .map(ZohoClient::getClientName)
                .toList();
    }
    private List<ZohoClient> getAllClientInformation(String accessToken) {
        var requestEntity = this.generateRequestEntity(accessToken);
        String requestUrl = generateRequestUrl("/getclients");

        RestTemplate restTemplate = new RestTemplate();
        var clients = restTemplate.exchange(
                requestUrl,
                HttpMethod.GET,
                requestEntity,
                ZohoClientResponse.class
        );
        return clients
                .getBody()
                .getZohoClientResponseList()
                .getResult();
    }
    public List<String> getJobs(String userRefreshToken) {
        String accessToken = this.generateAccessToken(userRefreshToken);
        return this.getAllJobInformation(accessToken)
                .stream()
                .map(ZohoJob::getJobName)
                .toList();
    }
    private List<ZohoJob> getAllJobInformation(String accessToken) {
        String requestUrl = generateRequestUrl("/getjobs");
        var requestEntity = this.generateRequestEntity(accessToken);

        RestTemplate restTemplate = new RestTemplate();
        var jobs = restTemplate.exchange(
                requestUrl,
                HttpMethod.GET,
                requestEntity,
                ZohoJobResponse.class
        );
        return jobs
                .getBody()
                .getZohoJobResponseList()
                .getResult();
    }

    public List<String> getProjects(String userRefreshToken) {
        String accessToken = this.generateAccessToken(userRefreshToken);
        return this.getAllProjectInformation(accessToken)
                .stream()
                .map(ZohoProject::getProjectName)
                .toList();
    }

    private List<ZohoProject> getAllProjectInformation(String accessToken) {
        var requestEntity = this.generateRequestEntity(accessToken);
        String requestUrl = generateRequestUrl("/getprojects");

        RestTemplate restTemplate = new RestTemplate();
        var projects = restTemplate.exchange(
                requestUrl,
                HttpMethod.GET,
                requestEntity,
                ZohoProjectResponse.class
        );
        return projects
                .getBody()
                .getZohoProjectResponseList()
                .getResult();
    }

    public void addNewTimeLog(TimeLogDTO timeLogDTO, String userRefreshToken) {
//        WHen adding job and project with whitespace it adds another one with a %20, correct it TODO
        String accessToken = this.generateAccessToken(userRefreshToken);
        var requestEntity = this.generateRequestEntity(accessToken);
        String requestUrl = generateRequestUrl("/addtimelog");

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(requestUrl)
        .queryParam("user", this.userEmail)
        .queryParam("projectName", timeLogDTO.getProjectName())
        .queryParam("jobName", timeLogDTO.getJobName())
        .queryParam("workDate", this.dateTimeConverter.convertDatePickerFormatToStandard(timeLogDTO.getWorkDate()))
        .queryParam("billingStatus", timeLogDTO.isBillable() ? "billable" : "non-billable")
        .queryParam("fromTime", this.dateTimeConverter.convertTimePickerFormatToStandard(timeLogDTO.getFromTime()))
        .queryParam("toTime", this.dateTimeConverter.convertTimePickerFormatToStandard(timeLogDTO.getToTime()))
        .queryParam("workItem", timeLogDTO.getWorkItem())
        .queryParam("description", timeLogDTO.getDescription());

        RestTemplate restTemplate = new RestTemplate();
        var statusCode = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                requestEntity,
                String.class
        ).getStatusCode();
        log.info("Adding new time log was: {}", statusCode);
    }

    public List<String> getJobsForClient(String clientName, String refreshToken) {
        String accessToken = this.generateAccessToken(refreshToken);
        String clientId = this.findIdForClient(clientName, accessToken);
        return this.getAllJobInformation(accessToken)
                .stream()
                .filter(zohoJob -> zohoJob.getClientId().equals(clientId))
                .map(ZohoJob::getJobName)
                .toList();
    }

    private String findIdForClient(String clientName, String accessToken) {
        var clientOptional = this.getAllClientInformation(accessToken)
                .stream()
                .filter(zohoClient -> zohoClient.getClientName().equals(clientName))
                .map(ZohoClient::getClientId)
                .findFirst();
        if (clientOptional.isEmpty()) {
            String message = String.format("Client not found with name: %s", clientName);
            throw new ClientNotFound(message);
        }
        return clientOptional.get();
    }
}
