package com.sumerge.movie.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth-service", url = "${myapp.auth_endpoint}/api",configuration = CustomFeignClientConfig.class)
public interface AuthServiceFeignClient {





        @GetMapping("/validate")
        ResponseEntity<Void> validateToken(@RequestHeader("Authorization") String authorizationHeader);


}
