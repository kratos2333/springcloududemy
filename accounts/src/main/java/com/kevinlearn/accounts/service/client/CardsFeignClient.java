package com.kevinlearn.accounts.service.client;

import com.kevinlearn.accounts.dto.CardsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// todo: notes: this is what registered on eureka server
@FeignClient("cards")
public interface CardsFeignClient {
    // todo: notes: method name can be different but the signature and return type should match the target controller
    // todo: notes: for the mapping path specify the fullpath
    // todo: notes: CardsDto copied from cards microservice
    // todo: notes: this is very similar to the JPA code, we just need to define the interface
    @GetMapping(value = "/api/fetch", consumes = "application/json")
    public ResponseEntity<CardsDto> fetchCardDetails(@RequestParam String mobileNumber);

}
