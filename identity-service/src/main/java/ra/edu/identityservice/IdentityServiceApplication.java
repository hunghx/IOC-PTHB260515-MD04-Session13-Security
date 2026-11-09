package ra.edu.identityservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ra.edu.identityservice.security.JwtUtils;

@SpringBootApplication
public class IdentityServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(IdentityServiceApplication.class, args);
    }
    @Bean
    public CommandLineRunner runner(JwtUtils  jwtUtils) {
        return args -> {
            System.out.println("token 1 : "+ jwtUtils.generateToken("admin","ROLE_ADMIN"));
            System.out.println("token 2 : "+ jwtUtils.generateToken("hunghx","ROLE_USER"));

        };
    }

}
