package io.tis.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public boolean checkUserRefreshToken() {
        return !this.userRepository.findAll().isEmpty();
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public void assignRefreshToken(String refreshToken) {
        User user = new User();
        user.setEmail("john@gmail.com");
        user.setRefreshToken(refreshToken);
        this.userRepository.save(user);
    }

    public List<String> getProjects() {
        return List.of("Project1", "Project2");
    }

    public List<String> getJobs() {
        return List.of("Job1", "Job2", "Job3");
    }

    public void addDummyUser() {
        User user = new User();
        user.setEmail("john@gmail.com");
        user.setRefreshToken("100.blablabla");
        this.userRepository.save(user);
    }
}
