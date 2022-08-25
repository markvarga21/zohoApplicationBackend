package io.tis.zoho.converter;

import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConverterConfiguration {
    @Bean
    public Gson getGsonBean() {
        return new Gson();
    }
}
