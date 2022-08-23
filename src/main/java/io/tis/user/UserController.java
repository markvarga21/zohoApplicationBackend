package io.tis.user;

import io.tis.zoho.ZohoService;
import io.tis.zoho.dto.TimeLogDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin
public class UserController {
    private final ZohoService zohoService;
    private final UserService userService;

    @GetMapping("/redirect")
    public ModelAndView getGrantToken(@RequestParam(name = "code") String code) {
        log.info(code);
        String refreshToken = this.zohoService.generateRefreshToken(code);
        this.userService.assignRefreshToken(refreshToken);
        return new ModelAndView("standard_login");
    }

    @GetMapping("/jobs")
    public List<String> getJobForClient(@RequestParam("clientName") String clientName) {
        return this.zohoService.getJobsForClient(clientName);
    }

    @GetMapping("/clientlist")
    public List<String> getClients() {
        return this.zohoService.getClientNames();
    }

    @GetMapping("/projectlist")
    public List<String> getProjects() {
        return this.zohoService.getProjectNames();
    }

    @GetMapping("/joblist")
    public List<String> getJobs() {
        return this.zohoService.getJobNames();
    }

    @PostMapping("/add")
    public ResponseEntity<String> addTimeLog(@RequestBody TimeLogDTO timeLogDTO) {
        System.out.println(timeLogDTO);
        return new ResponseEntity<>("Added successfully!", HttpStatus.OK);
    }
}
