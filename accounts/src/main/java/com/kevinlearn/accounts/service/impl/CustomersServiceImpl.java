package com.kevinlearn.accounts.service.impl;

import com.kevinlearn.accounts.dto.AccountsDto;
import com.kevinlearn.accounts.dto.CardsDto;
import com.kevinlearn.accounts.dto.CustomerDetailsDto;
import com.kevinlearn.accounts.dto.LoansDto;
import com.kevinlearn.accounts.entity.Accounts;
import com.kevinlearn.accounts.entity.Customer;
import com.kevinlearn.accounts.exception.ResourceNotFoundException;
import com.kevinlearn.accounts.mapper.AccountsMapper;
import com.kevinlearn.accounts.mapper.CustomerMapper;
import com.kevinlearn.accounts.repository.AccountsRepository;
import com.kevinlearn.accounts.repository.CustomerRepository;
import com.kevinlearn.accounts.service.ICustomersService;
import com.kevinlearn.accounts.service.client.CardsFeignClient;
import com.kevinlearn.accounts.service.client.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
// todo: note: when there is a single constructor the autowire will annotated automatically
public class CustomersServiceImpl implements ICustomersService {

    private AccountsRepository accountsRepo;
    private CustomerRepository customerRepo;
    private CardsFeignClient cardsFeignClient;
    private LoansFeignClient loansFeignClient;

    /**
     * @param mobileNumber
     * @return
     */
    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber) {
        Customer customer = customerRepo.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));

        Accounts accounts = accountsRepo.findByCustomerId(customer.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Accounts", "customerId", customer.getCustomerId().toString()));

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        // todo: notes: call loan feign client, the body will be the object defined in the generic
        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(mobileNumber);
        customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());

        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(mobileNumber);
        customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());

        return customerDetailsDto;
    }
}
