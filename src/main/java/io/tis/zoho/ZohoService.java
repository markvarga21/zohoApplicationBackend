package io.tis.zoho;

import com.google.gson.Gson;
import io.tis.exception.ClientNotFoundException;
import io.tis.exception.JobNotFoundException;
import io.tis.exception.ProjectNotFoundException;
import io.tis.user.UserService;
import io.tis.zoho.client.ZohoClientRepository;
import io.tis.zoho.client.ZohoClient;
import io.tis.zoho.client.ZohoClientResponse;
import io.tis.zoho.converter.ZohoResponseConverter;
import io.tis.zoho.dto.TimeLogDTO;
import io.tis.zoho.job.ZohoJob;
import io.tis.zoho.job.ZohoJobRepository;
import io.tis.zoho.job.ZohoJobResponse;
import io.tis.zoho.project.ZohoProject;
import io.tis.zoho.project.ZohoProjectRepository;
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

import java.util.List;

@Service
@Getter
@Slf4j
@RequiredArgsConstructor
public class ZohoService {
    private final UserService userService;
    private final DateTimeConverter dateTimeConverter;
    private final ZohoClientRepository zohoClientRepository;
    private final ZohoJobRepository zohoJobRepository;
    private final ZohoProjectRepository zohoProjectRepository;
    private final ZohoResponseConverter responseConverter;
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

    // TO be removed
    @Value("${user.access.token}")
    private String accessToken;

    final static int RESPONSE_BEGIN_CHAR_INDEX = 22;
    final static int RESPONSE_END_CHAR_INDEX = 87;

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
        // TODO
        return this.accessToken;
    }

    private HttpEntity<?> generateRequestEntity(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Zoho-oauthtoken " + accessToken);
        return new HttpEntity<>(headers);
    }

    private String generateRequestUrl(String path) {
        return this.zohoPeopleBaseUrl + path;
    }

    public List<String> getClientNames() {
        if (!this.isReadyToPerformOperations()) {
            log.info("Initiating database fill..");
            String refreshToken = this.userService.getUserRefreshToken();
            this.gatherZohoInformation(refreshToken);
        }
        log.info("Databased filled up, starting working!");

        var zohoClients = this.zohoClientRepository.findAll();
        if (zohoClients.isEmpty()) {
            String message = "Clients not found!";
            throw new ClientNotFoundException(message);
        }
        return zohoClients
                .stream()
                .map(ZohoClient::getClientName)
                .toList();
    }
    private List<ZohoClient> getClientsInformation(String accessToken) {
        var requestEntity = this.generateRequestEntity(accessToken);
        String requestUrl = generateRequestUrl("/getclients");

        RestTemplate restTemplate = new RestTemplate();
        var clients = restTemplate.exchange(
                requestUrl,
                HttpMethod.GET,
                requestEntity,
                String.class
        );
        return new Gson().fromJson(clients.getBody(), ZohoClientResponse.class).getResponse().getResult();
    }

    public List<String> getJobNames() {
        if (!this.isReadyToPerformOperations()) {
            String refreshToken = this.userService.getUserRefreshToken();
            this.gatherZohoInformation(refreshToken);
        }

        var zohoJobs = this.zohoJobRepository.findAll();
        if (zohoJobs.isEmpty()) {
            String message = "No job found!";
            throw new JobNotFoundException(message);
        }
        return zohoJobs
                .stream()
                .map(ZohoJob::getJobName)
                .toList();
    }
    private List<ZohoJob> getJobsInformation(String accessToken) {
        String requestUrl = generateRequestUrl("/getjobs");
        var requestEntity = this.generateRequestEntity(accessToken);

        RestTemplate restTemplate = new RestTemplate();
        var jobs = restTemplate.exchange(
                requestUrl,
                HttpMethod.GET,
                requestEntity,
                String.class
        );
        System.out.println(new Gson().fromJson(jobs.getBody(), ZohoJobResponse.class).getResponse().getResult());
        return new Gson().fromJson(jobs.getBody(), ZohoJobResponse.class).getResponse().getResult();
    }

    public List<String> getProjectNames() {
        if (!this.isReadyToPerformOperations()) {
            String refreshToken = this.userService.getUserRefreshToken();
            this.gatherZohoInformation(refreshToken);
        }

        var zohoProjects = this.zohoProjectRepository.findAll();
        if (zohoProjects.isEmpty()) {
            String message = "No projects found!";
            throw new ProjectNotFoundException(message);
        }
        return zohoProjects
                .stream()
                .map(ZohoProject::getProjectName)
                .toList();
    }
    private List<ZohoProject> getProjectsInformation(String accessToken) {
        var requestEntity = this.generateRequestEntity(accessToken);
        String requestUrl = generateRequestUrl("/getprojects");

        RestTemplate restTemplate = new RestTemplate();
        var projects = restTemplate.exchange(
                requestUrl,
                HttpMethod.GET,
                requestEntity,
                String.class
        );
        return new Gson().fromJson(projects.getBody(), ZohoProjectResponse.class).getResponse().getResult();
    }

    public void addNewTimeLog(TimeLogDTO timeLogDTO, String userRefreshToken) {
//        WHen adding job and project with whitespace it adds another one with a %20, correct it TODO
        var accessToken = this.generateAccessToken(userRefreshToken);
        var workDates = timeLogDTO.getWorkDate().split(",");
        if (workDates.length == 1) {
            this.addSingleTimeLog(timeLogDTO, accessToken);
        } else {
            for (int i = 0; i < workDates.length; i++) {
                var tempTimeLog = TimeLogDTO.builder()
                        .projectName(timeLogDTO.getProjectName())
                        .jobName(timeLogDTO.getJobName())
                        .workDate(workDates[i])
                        .billable(timeLogDTO.getBillable())
                        .fromTime(timeLogDTO.getFromTime())
                        .toTime(timeLogDTO.getToTime())
                        .workItem(timeLogDTO.getWorkItem())
                        .description(timeLogDTO.getDescription()).build();
                this.addSingleTimeLog(tempTimeLog, accessToken);
            }
        }
    }

    private void addSingleTimeLog(TimeLogDTO timeLogDTO, String accessToken) {
        var requestEntity = this.generateRequestEntity(accessToken);
        String requestUrl = generateRequestUrl("/addtimelog");

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(requestUrl)
                .queryParam("user", this.userEmail)
                .queryParam("projectId", this.getIdForProjectAndClientName(timeLogDTO.getProjectName(), timeLogDTO.getClientName()))
                .queryParam("jobId", this.getIdForJobAndClientName(timeLogDTO.getJobName(), timeLogDTO.getClientName()))
                .queryParam("workDate", timeLogDTO.getWorkDate())
                .queryParam("billingStatus", timeLogDTO.getBillable().replace(" ", ""))
                .queryParam("fromTime", timeLogDTO.getFromTime().replace(" ", ""))
                .queryParam("toTime", timeLogDTO.getToTime().replace(" ", ""))
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

    public List<String> getJobsForClient(String clientName) {
        var zohoJobOptional = this.zohoJobRepository.getZohoJobsByClientName(clientName);
        if (zohoJobOptional.isEmpty()) {
            String message = String.format("No job(s) found for client: %s", clientName);
            throw new JobNotFoundException(message);
        }
        return zohoJobOptional
                .get()
                .stream()
                .map(ZohoJob::getJobName)
                .toList();
    }

    public List<String> getProjectsForClient(String clientName) {
        var zohoProjectOptional = this.zohoProjectRepository.getZohoProjectsByClientName(clientName);
        if (zohoProjectOptional.isEmpty()) {
            String message = String.format("No project(s) found for client: %s", clientName);
            throw new JobNotFoundException(message);
        }
        return zohoProjectOptional
                .get()
                .stream()
                .map(ZohoProject::getProjectName)
                .toList();
    }

    private boolean isReadyToPerformOperations() {
//        To be removed after deployment
        this.userService.addDummyUser();

        return !this.userService.getUserRefreshToken().isEmpty()
                && this.isZohoDatabaseSetup();
    }

    private boolean isZohoDatabaseSetup() {
        return !this.zohoJobRepository.findAll().isEmpty();
    }

    public void gatherZohoInformation(String refreshToken) {
        String accessToken = this.generateAccessToken(refreshToken);
        this.zohoClientRepository.saveAll(this.getClientsInformation(accessToken));
        this.zohoJobRepository.saveAll(this.getJobsInformation(accessToken));
        this.zohoProjectRepository.saveAll(this.getProjectsInformation(accessToken));
    }

    public String getIdForProjectAndClientName(String projectName, String clientName) {
        var projectIdOptional = this.zohoProjectRepository.getIdForProjectAndClientName(projectName, clientName);
        if (projectIdOptional.isEmpty()) {
            String message = String.format("Project ID not found for project name: %s", projectName);
            throw new ProjectNotFoundException(message);
        }
        return projectIdOptional.get();
    }

    public String getIdForJobAndClientName(String jobName, String clientName) {
        var jobIdOptional = this.zohoJobRepository.getIdForJobName(jobName, clientName);
        if (jobIdOptional.isEmpty()) {
            String message = String.format("Job ID not found for project name: %s", jobName);
            throw new ProjectNotFoundException(message);
        }
        return jobIdOptional.get();
    }
}
