package com.kevinlearn.accounts.service.client;

import com.kevinlearn.accounts.dto.LoansDto;
import org.hibernate.annotations.Comment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

// notes: define the fallback function if loan fail to response
// see this url: https://docs.spring.io/spring-cloud-openfeign/docs/4.0.4/reference/html/#spring-cloud-feign-circuitbreaker-fallback
@Component
public class LoansFallback implements LoansFeignClient{
    @Override
    public ResponseEntity<LoansDto> fetchLoanDetails(String correlationId, String mobileNumber) {
        return null;
    }
}
