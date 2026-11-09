package ra.edu.gateway.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtils {
    @Value("${jwt.secret-key}")
    private String secretKey;

    private SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes()); // nếu chứa kí tự đặc biệt
    }


    // Xác minh token
    public boolean validateToken(String token){
        try{
            extractClaimsJws(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    // Giải mã token
    public Claims extractClaimsJws(String token){
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
