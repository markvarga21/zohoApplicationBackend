package io.tis.zoho;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

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
}
