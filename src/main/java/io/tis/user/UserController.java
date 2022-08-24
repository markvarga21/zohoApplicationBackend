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
    public List<String> getProjects(@RequestParam(required = false) String clientName) {
        if (clientName == null || clientName.isEmpty()) {
            log.error("Client name is empty, returning the full list!");
            return this.zohoService.getProjectNames();
        }
        log.info("Client name is {}, returning data ({}) for him/her!", clientName, this.zohoService.getProjectsForClient(clientName));
        return this.zohoService.getProjectsForClient(clientName);
    }

    @GetMapping("/joblist")
    public List<String> getJobs(@RequestParam(required = false) String clientName) {
        if (clientName == null || clientName.isEmpty()) {
            log.error("Client name is empty, returning the full list!");
            return this.zohoService.getJobNames();
        }
        log.info("Client name is {}, returning data ({}) for him/her!", clientName, this.zohoService.getJobsForClient(clientName));
        return this.zohoService.getJobsForClient(clientName);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addTimeLog(@RequestBody TimeLogDTO timeLogDTO) {
        System.out.println(timeLogDTO);
        return new ResponseEntity<>("Added successfully!", HttpStatus.OK);
    }

    @GetMapping("/dummy")
    public void getIdForJobName(@RequestParam("projectName") String projectName, @RequestParam("clientName") String clientName) {
        System.out.println(this.zohoService.getIdForProjectAndClientName(projectName, clientName));
    }
}
