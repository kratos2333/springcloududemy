package com.kevinlearn.accounts.service.client;

import com.kevinlearn.accounts.dto.CardsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

// notes: this is what registered on eureka server
@FeignClient(name = "cards", fallback = CardsFallback.class)
public interface CardsFeignClient {
    // notes: method name can be different but the signature and return type should match the target controller
    // notes: for the mapping path specify the fullpath
    // notes: CardsDto copied from cards microservice
    // notes: this is very similar to the JPA code, we just need to define the interface
    @GetMapping(value = "/api/fetch", consumes = "application/json")
    public ResponseEntity<CardsDto> fetchCardDetails(@RequestHeader("kevin-correlation-id") String correlationId, @RequestParam String mobileNumber);

}
