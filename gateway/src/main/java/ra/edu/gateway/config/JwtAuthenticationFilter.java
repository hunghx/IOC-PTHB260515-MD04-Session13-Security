package ra.edu.gateway.config;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import  org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {
    private final JwtUtils jwtUtils;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // xử lý chuỗi JWWT
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // bỏ qua các đường dẫn ko cần token
        if (path.contains("/api/auth/")){
            return chain.filter(exchange);
        }

        // Kiểm tra header
        if (!request.getHeaders().containsHeader("Authorization")){
           sendError(exchange, HttpStatus.UNAUTHORIZED,"Missing Authorization Header");
        }
        String authHeader = request.getHeaders().getFirst("Authorization");
        if (authHeader==null || !authHeader.startsWith("Bearer ")){
            throw new RuntimeException("Missing authorization header");
        }
        String token = authHeader.substring(7);
        // giải mã và xác minh
        if (!jwtUtils.validateToken(token)){
            throw new RuntimeException("Invalid token");
        }

        Claims claims = jwtUtils.extractClaimsJws(token);
        String username = claims.getSubject();
        String roles = claims.get("roles",String.class);
        // Gửi thông tin này vào request để tiếp tục đi tới các service khác
        ServerHttpRequest httpRequest = request.mutate()
                .header("X-User-Id", username)
                .header("X-User-Roles", roles)
                .build();
        return chain.filter(exchange.mutate().request(httpRequest).build());
    }

    private Mono<Void> sendError(ServerWebExchange exchange, HttpStatus status, String errorMessage) {
        ServerHttpResponse response = exchange.getResponse();

        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String body = """
            {
                "status": %d,
                "message": "%s"
            }
            """.formatted(status.value(), errorMessage);

        DataBuffer buffer = response.bufferFactory()
                .wrap(body.getBytes(StandardCharsets.UTF_8));

        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
