package com.kevinlearn.accounts.service;

import com.kevinlearn.accounts.dto.CustomerDetailsDto;

public interface ICustomersService {
    /**
     * @param mobileNumber
     * @return
     */
    CustomerDetailsDto fetchCustomerDetails(String mobileNumber, String correlationId);
}
