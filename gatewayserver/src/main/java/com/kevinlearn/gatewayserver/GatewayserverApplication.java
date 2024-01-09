package com.kevinlearn.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

import java.time.Duration;
import java.time.LocalDateTime;

@SpringBootApplication
public class GatewayserverApplication {
	// notes: http://localhost:8072/actuator/gateway/routes to see the routes
	// notes: http://localhost:8072/ACCOUNTS/api/create this will redirect to ACCOUNTS ms
	// notes: see the configurations in the application.yml(lower-case-service-id: true) for lowercase the appname
	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

	// notes: custom routing path, note that the default one will still work
	// notes: we can achieve the same thing in properties file but use java bean is more flexible
	@Bean
	public RouteLocator kevinRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
		return routeLocatorBuilder.routes()
				.route(p -> p.path("/kevin/accounts/**")
					.filters(f -> f.rewritePath("/kevin/accounts/(?<segment>.*)","/${segment}")
							// notes: add the response header in the response
							.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
							// notes: add account circuitbreaker
							// notes: http://localhost:8072/actuator/circuitbreakers to check the circuitbreaker
							// notes: http://localhost:8072/actuator/circuitbreakerevents?name=accountsCircuitBreaker check the requests
							// notes: when the circuitbreaker status is open the service will return http 503 (service not available)
							.circuitBreaker(config -> config.setName("accountsCircuitBreaker")
									// notes: fallback
									.setFallbackUri("forward:/contactSupport")))
						.uri("lb://ACCOUNTS"))
				.route(p -> p.path("/kevin/loans/**")
					.filters(f -> f.rewritePath("/kevin/loans/(?<segment>.*)","/${segment}")
							.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
							// todo: notes: enable the retry pattern in gateway in loans service and only for get method
							// note that the retry should be idemponent
							.retry(retryConfig -> retryConfig.setRetries(3)
									.setMethods(HttpMethod.GET)
									.setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000),2,true)))
					.uri("lb://LOANS"))
				.route(p -> p.path("/kevin/cards/**")
						.filters(f -> f.rewritePath("/kevin/cards/(?<segment>.*)","/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
						.uri("lb://CARDS")).build();

	}

}
