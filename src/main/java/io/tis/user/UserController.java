package io.tis.user;

import io.tis.exception.RefreshTokenNotFoundException;
import io.tis.zoho.ZohoService;
import io.tis.zoho.dto.TimeLogDTO;
import io.tis.zoho.job.ZohoJobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
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
    private final ZohoJobService zohoJobService;

    @GetMapping("/redirect")
    public ModelAndView getGrantToken(@RequestParam(name = "code") String code) {
        log.info(code);
        String refreshToken = this.zohoService.generateRefreshToken(code);
        this.userService.assignRefreshToken(refreshToken);
        return new ModelAndView("standard_login");
    }

    @GetMapping
    public ModelAndView index() {
        // To be removed after deployment
        this.userService.addDummyUser();
        //
        var doesUserHaveRefreshToken = this.userService.checkUserRefreshToken();
        ModelAndView modelAndView = new ModelAndView();
        if (doesUserHaveRefreshToken) {
            if (!this.zohoService.isZohoDatabaseSetup()) {
                log.info("Zoho repository not setup, filling in...");
                this.zohoService.gatherZohoInformation(this.userService.getUserRefreshToken());
            }
            log.info("Zoho repository setup, proceeding to login!");
            modelAndView.setViewName("standard_login");
        } else {
            modelAndView.setViewName("first_login");
        }
        return modelAndView;
    }

//    @GetMapping("/add")
//    public ModelAndView showStandardForm(Model model) {
//        TimeLogDTO timeLog = TimeLogDTO.builder().build();
//        model.addAttribute("timelog", timeLog);
//        var projectNames = this.userService.getProjects();
//        var jobNames = this.userService.getJobs();
//        model.addAttribute("listProject", projectNames);
//        model.addAttribute("listJob", jobNames);
//        var clientNames = this.userService.getClients();
//        model.addAttribute("listClient", clientNames);
//        model.addAttribute("zohoJobService", this.zohoJobService);
//
//
//        return new ModelAndView("register_time_log");
//    }

//    @PostMapping("/add")
//    public ModelAndView submitForm(@ModelAttribute("log") TimeLogDTO timeLog) {
//        String refreshToken = this.userService.getUserRefreshToken();
////        To be removed
////        this.zohoService.addNewTimeLog(timeLog, refreshToken);
//        System.out.println(timeLog);
//        return new ModelAndView("register_success");
//    }
//
    @GetMapping("/jobs")
    public List<String> getJobForClient(@RequestParam("clientName") String clientName) {
        return this.zohoService.getJobsForClient(clientName);
    }

    @GetMapping("/clients")
    public List<String> getClients() {
        return this.zohoService.getClients();
    }

    @GetMapping("/fill")
    public ResponseEntity<String> fillDatabase() {
        // To be removed after deployment
        this.userService.addDummyUser();
        var doesUserHaveRefreshToken = this.userService.checkUserRefreshToken();
        if (!doesUserHaveRefreshToken) {
            String message = "User does not have a refresh token yet!";
            throw new RefreshTokenNotFoundException(message);
        }
        if (!this.zohoService.isZohoDatabaseSetup()) {
            log.info("Zoho repository not setup, filling in...");
            String refreshToken = this.userService.getUserRefreshToken();
            this.zohoService.gatherZohoInformation(refreshToken);
        }
        return new ResponseEntity<>("Database filled successfully!", HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addTimeLog(@RequestBody TimeLogDTO timeLogDTO) {
        var doesUserHaveRefreshToken = this.userService.checkUserRefreshToken();
        if (doesUserHaveRefreshToken) {
            log.info("Zoho repository setup, proceeding to login!");
        }
        return new ResponseEntity<>("Added successfully!", HttpStatus.OK);
    }
}
