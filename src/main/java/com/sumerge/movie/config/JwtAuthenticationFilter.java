    package com.sumerge.movie.config;

    import io.jsonwebtoken.Claims;
    import io.jsonwebtoken.JwtException;
    import io.jsonwebtoken.Jwts;
    import io.jsonwebtoken.io.Decoders;
    import io.jsonwebtoken.security.Keys;
    import jakarta.servlet.FilterChain;
    import jakarta.servlet.ServletException;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import lombok.RequiredArgsConstructor;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.http.*;
    import org.springframework.lang.NonNull;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContext;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
    import org.springframework.stereotype.Component;
    import org.springframework.web.client.HttpClientErrorException;
    import org.springframework.web.client.RestTemplate;
    import org.springframework.web.filter.OncePerRequestFilter;

    import java.io.IOException;
    import java.security.Key;
    import java.util.ArrayList;


    @Component
    @RequiredArgsConstructor
    public class JwtAuthenticationFilter extends OncePerRequestFilter {

        @Value("${myapp.auth_endpoint}")
        private String AUTH_URL;
        private final RestTemplate restTemplate = new RestTemplate();
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            final String authHeader = request.getHeader("Authorization");
            final String jwtToken;

            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                response.setStatus(403);
                System.out.println(authHeader);
                return;
            }
            jwtToken = authHeader.substring(7);
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(jwtToken);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            try{
                //String AUTH_URL = "http://localhost:8080/api/validate";
                ResponseEntity<Void> authResponse = restTemplate.exchange(AUTH_URL, HttpMethod.GET, entity, Void.class);
                System.out.println(authResponse);
                System.out.println(jwtToken);
                if(authResponse.getStatusCode()== HttpStatus.OK) {
                    System.out.println("Code200");
                    filterChain.doFilter(request, response);
                }
            } catch (HttpClientErrorException e) {

                System.out.println(e.toString());
                response.setStatus(403);
                return;
            }

        }

    }
