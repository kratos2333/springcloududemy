package com.kevinlearn.accounts;

import com.kevinlearn.accounts.dto.AccountsContactInfoDto;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
// note: enable the jpa auditing so in the entity we can audit the annotation like @CreatedDate @LastModifiedDate ...
// and add the auditor bean here for the modified by, see AuditAwareImpl.java
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
// notes: enable the properties class
@EnableConfigurationProperties(value={AccountsContactInfoDto.class})
// enable openapi and mentioned info metadata
// notes: to run the application we can also do mvn spring-boot:run (after mvn clean install to generate jar)
@OpenAPIDefinition(
        info = @Info(
                title = "Accounts microservice REST API Documentation",
                description = "Accounts microservice REST API Documentation",
                version = "v1",
                contact = @Contact(
                        name = "Kevin Jia",
                        email = "Kevinjia1984@gmail.com",
                        url = "www.kevinjia.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "www.kevinjia.com"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Accounts microservice REST API Documentation",
                url = "www.kevinjia.com"
        )
)
public class AccountsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountsApplication.class, args);
    }

}
