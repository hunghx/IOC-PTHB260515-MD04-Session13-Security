package ra.edu.identityservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/info")
    public String info(@RequestHeader("X-User-Id") String username, @RequestHeader("X-User-Roles") String roles){
        return "Hello World:: "+ username + " " + roles;
    }
}
