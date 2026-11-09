package ra.edu.identityservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ra.edu.identityservice.dto.request.LoginForm;
import ra.edu.identityservice.dto.request.RegisterForm;
import ra.edu.identityservice.dto.response.JwtResponse;
import ra.edu.identityservice.entity.User;
import ra.edu.identityservice.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginForm req){
        return ResponseEntity.ok(userService.login(req)); // 200
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterForm req){
        return new ResponseEntity<>(userService.register(req), HttpStatus.CREATED);
    }
}
