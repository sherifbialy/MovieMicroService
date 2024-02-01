    package com.sumerge.movie.config;


    import jakarta.servlet.FilterChain;
    import jakarta.servlet.ServletException;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import lombok.RequiredArgsConstructor;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.http.*;
    import org.springframework.stereotype.Component;
    import org.springframework.web.client.HttpClientErrorException;
    import org.springframework.web.client.RestTemplate;
    import org.springframework.web.filter.OncePerRequestFilter;
    import java.io.IOException;



    @Component
    @RequiredArgsConstructor
    public class JwtAuthenticationFilter extends OncePerRequestFilter {

        @Value("${myapp.auth_endpoint}")
        private String authUrl;
        private final RestTemplate restTemplate = new RestTemplate();
        @Override
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

            try{

                ResponseEntity<Void> authResponse = restTemplate.exchange(authUrl, HttpMethod.GET, entity, Void.class);

                if(authResponse.getStatusCode()== HttpStatus.OK) {

                    filterChain.doFilter(request, response);
                }
            } catch (HttpClientErrorException e) {


                response.setStatus(403);

            }

        }

    }
