package ra.edu.identityservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class JwtResponse {
    private String token;
    private String username;
    private Long id;
    private LocalDateTime timestamp;
}
