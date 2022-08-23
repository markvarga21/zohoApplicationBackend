package io.tis.user;

import io.tis.zoho.ZohoService;
import io.tis.zoho.dto.TimeLogDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public boolean checkUserRefreshToken() {
        return !this.userRepository.findAll().isEmpty();
    }

    public void assignRefreshToken(String refreshToken) {
        User user = new User();
        user.setEmail("john@gmail.com");
        user.setRefreshToken(refreshToken);
        this.userRepository.save(user);
    }

    public String getUserRefreshToken() {
        return this.userRepository.findAll().get(0).getRefreshToken();
    }

    public void addDummyUser() {
        User user = new User();
        user.setEmail("vmark2145@gmail.com");
        user.setRefreshToken("100.blablabla");
        this.userRepository.save(user);
    }
}
