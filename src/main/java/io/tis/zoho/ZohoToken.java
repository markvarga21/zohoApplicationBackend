package io.tis.zoho;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;


@ToString
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZohoToken {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("api_domain")
    private String apiDomain;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("expires_in")
    private Integer expiresIn;
}
