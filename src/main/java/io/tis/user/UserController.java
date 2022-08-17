package io.tis.user;

import io.tis.zoho.TimeLog;
import io.tis.zoho.ZohoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final ZohoService zohoService;
    private final UserService userService;

    @GetMapping("/redirect")
    public ModelAndView getGrantToken(@RequestParam(name = "code") String code) {
        log.info(code);
        String refreshToken = this.zohoService.generateRefreshToken(code);
        this.userService.assignRefreshToken(refreshToken);
        String message = "Refresh token generated successfully!";
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
            modelAndView.setViewName("standard_login");
        } else {
            modelAndView.setViewName("first_login");
        }
        return modelAndView;
    }

    @GetMapping("/add")
    public ModelAndView showStandardForm(Model model) {
        TimeLog timeLog = new TimeLog();
        model.addAttribute("timelog", timeLog);
        var projectNames = this.userService.getProjects();
        var jobNames = this.userService.getJobs();
        model.addAttribute("listProject", projectNames);
        model.addAttribute("listJob", jobNames);
        return new ModelAndView("register_time_log");
    }

    @PostMapping("/add")
    public ModelAndView submitForm(@ModelAttribute("log") TimeLog timeLog) {
        System.out.println(timeLog);
        return new ModelAndView("register_success");
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return this.userService.getAllUsers();
    }
}
