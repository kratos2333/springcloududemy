package com.kevinlearn.accounts.service.impl;

import com.kevinlearn.accounts.constants.AccountsConstants;
import com.kevinlearn.accounts.dto.AccountsDto;
import com.kevinlearn.accounts.dto.CustomerDto;
import com.kevinlearn.accounts.entity.Accounts;
import com.kevinlearn.accounts.entity.Customer;
import com.kevinlearn.accounts.exception.CustomerAlreadyExistsException;
import com.kevinlearn.accounts.exception.ResourceNotFoundException;
import com.kevinlearn.accounts.mapper.AccountsMapper;
import com.kevinlearn.accounts.mapper.CustomerMapper;
import com.kevinlearn.accounts.repository.AccountsRepository;
import com.kevinlearn.accounts.repository.CustomerRepository;
import com.kevinlearn.accounts.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements IAccountsService {

    private AccountsRepository accountsRepo;
    private CustomerRepository customerRepo;
    @Override
    public void createAccount(CustomerDto customerDto) {
        // Notes: based on the requested dto(external) converted it to the Customer entity(internal)
        // notes: customerId will be generated by spring data jpa framework
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());

        Optional<Customer> foundCustomer = customerRepo.findByMobileNumber(customerDto.getMobileNumber());
        if(foundCustomer.isPresent()){
            throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber"
                    + customerDto.getAccountsDto());
        }

        // notes: don't forget to set the createdAt and createdBy field which defined under the base entity
        // notes: already added auditing so we can comment out the following lines
//        customer.setCreatedAt(LocalDateTime.now());
//        customer.setCreatedBy("Kevin Jia");
        // notes: this savedCustomer has the id
        Customer savedCustomer = customerRepo.save(customer);
        // notes: link customer with account
        accountsRepo.save(createNewAccount(savedCustomer));
    }
    private Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);

        // notes: already added auditing so we can comment out the following lines
//        newAccount.setCreatedAt(LocalDateTime.now());
//        newAccount.setCreatedBy("Kevin Jia");
        return newAccount;
    }

    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        // notes: this is an elegant way to throw resource not found exception
        Customer customer = customerRepo.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));

        Accounts accounts = accountsRepo.findByCustomerId(customer.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Accounts", "customerId", customer.getCustomerId().toString()));

        // notes: we need to response CustomerDto to the client
        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));
        return customerDto;
    }

    /**
     * @param customerDto
     * @return boolean indicating if the update of Account details is successful or not
     */
    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if(accountsDto !=null ){
            Accounts accounts = accountsRepo.findById(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString())
            );
            AccountsMapper.mapToAccounts(accountsDto, accounts);
            accounts = accountsRepo.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepo.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString())
            );
            CustomerMapper.mapToCustomer(customerDto,customer);
            customerRepo.save(customer);

            // note: it is a good practice to return boolean for update api call
            isUpdated = true;
        }
        return  isUpdated;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepo.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        accountsRepo.deleteByCustomerId(customer.getCustomerId());
        customerRepo.deleteById(customer.getCustomerId());
        return true;
    }


}
