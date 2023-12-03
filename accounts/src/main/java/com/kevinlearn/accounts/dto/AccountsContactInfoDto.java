package com.kevinlearn.accounts.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

// note: java will generate getter and setter and contstructor, java will treat the object as final object
// note: need to put @EnableConfigurationProperties in the main class
// note: when spring start up it will try to map the following properties into a pojo class
//accounts:
//        message: "Welcome to EazyBank accounts related local APIs "
//        contactDetails:
//        name: "John Doe - Developer"
//        email: "john@eazybank.com"
//        onCallSupport:
//        - (555) 555-1234
//        - (555) 523-1345
// NOTE: use record cannot be updated
@ConfigurationProperties(prefix = "accounts")
@Getter
@Setter
public class AccountsContactInfoDto {

    private String message;
    private Map<String, String> contactDetails;
    private List<String> onCallSupport;

}
