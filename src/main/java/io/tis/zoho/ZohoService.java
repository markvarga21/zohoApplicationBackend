package io.tis.zoho;

import io.tis.zoho.client.ZohoClientResponse;
import io.tis.zoho.client.ZohoClient;
import io.tis.zoho.job.ZohoJob;
import io.tis.zoho.job.ZohoJobResponse;
import lombok.Getter;
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

import java.util.List;

@Service
@Getter
@Slf4j
public class ZohoService {
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
        return "1000.1b6275f244944878e2eb395f2808c1e5.9e95829bae62b5852ce4077431c7c788";
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
                .getResult()
                .stream()
                .map(ZohoClient::getClientName)
                .toList();
    }

    public List<String> getJobs(String userRefreshToken) {
        String accessToken = this.generateAccessToken(userRefreshToken);
        var requestEntity = this.generateRequestEntity(accessToken);
        String requestUrl = generateRequestUrl("/getjobs");

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
                .getResult()
                .stream()
                .map(ZohoJob::getJobName)
                .toList();
    }
}
