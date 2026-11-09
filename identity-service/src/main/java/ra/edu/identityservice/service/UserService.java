package ra.edu.identityservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ra.edu.identityservice.dto.request.LoginForm;
import ra.edu.identityservice.dto.request.RegisterForm;
import ra.edu.identityservice.dto.response.JwtResponse;
import ra.edu.identityservice.entity.User;
import ra.edu.identityservice.repository.IUserRepository;
import ra.edu.identityservice.security.JwtUtils;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    public User register(RegisterForm request){
        User user = new User(null, request.getUsername(), passwordEncoder.encode(request.getPassword()), request.getRoles());
        return userRepository.save(user);
    }

    public JwtResponse login(LoginForm  request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        User userLogin = userRepository.findByUsername(request.getUsername()).get();
        return JwtResponse.builder()
                .id(userLogin.getId())
                .username(userLogin.getUsername())
                .token(jwtUtils.generateToken(userLogin.getUsername(), userLogin.getRoles()))
                .timestamp(LocalDateTime.now())
                .build();
    }
}
