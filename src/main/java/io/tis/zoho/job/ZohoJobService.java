package io.tis.zoho.job;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class ZohoJobService {
    private String clientName;

    public List<String> hello(String name) {
        if (name.equals(1)) {
            return List.of("1", "1");
        } else return List.of("2", "2");
    }
}
