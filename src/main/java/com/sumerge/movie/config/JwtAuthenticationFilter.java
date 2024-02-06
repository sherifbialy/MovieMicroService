    package com.sumerge.movie.config;


    import com.sumerge.movie.clients.AuthServiceFeignClient;
    import feign.FeignException;
    import jakarta.servlet.FilterChain;
    import jakarta.servlet.ServletException;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.context.annotation.Import;
    import org.springframework.http.*;
    import org.springframework.stereotype.Component;
    import org.springframework.web.client.HttpClientErrorException;
    import org.springframework.web.client.RestTemplate;
    import org.springframework.web.filter.OncePerRequestFilter;
    import java.io.IOException;



    @Component
    @RequiredArgsConstructor
    @Slf4j
    public class JwtAuthenticationFilter extends OncePerRequestFilter {

        @Value("${myapp.auth_endpoint}/api/validate")
        private String authUrl;
        private final AuthServiceFeignClient authServiceFeignClient;
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            final String authHeader = request.getHeader("Authorization");
            final String jwtToken;

            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                response.setStatus(403);

                return;
            }

            jwtToken = authHeader.substring(7);
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(jwtToken);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            try {
                authServiceFeignClient.validateToken(authHeader);
                filterChain.doFilter(request, response);
            } catch (FeignException.Forbidden e) {
                response.setStatus(403);
            }

        }

    }
