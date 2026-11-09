package ra.edu.identityservice.security;

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
    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)); // nếu chứa kí tự đặc biệt
    }

    // Tạo token JWT từ thông tin người dùng
    public String generateToken(String username,String roles){
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSecretKey())
                .compact();
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
