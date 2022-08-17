package io.tis.zoho;

import io.tis.zoho.client.Response;
import io.tis.zoho.client.ZohoClient;
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
        return "1000.0831dde99e45010fb05e9c872ebc572d.37b49c5250b2e858a3172811a100fb44";
    }

    public List<String> getClients(String refreshToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Zoho-oauthtoken " + this.generateAccessToken(refreshToken));

        HttpEntity<?> entity = new HttpEntity<>(headers);


        String requestUrl = this.zohoPeopleBaseUrl + "/people/api/timetracker/getclients";
        System.out.println(requestUrl);
        var clients = restTemplate.exchange(
                requestUrl,
                HttpMethod.GET,
                entity,
                Response.class
        );
        return clients
                .getBody()
                .getZohoClientResponse()
                .getResult()
                .stream()
                .map(ZohoClient::getClientName)
                .toList();
    }

}
