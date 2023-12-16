package com.kevinlearn.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class GatewayserverApplication {
	// todo: notes: http://localhost:8072/actuator/gateway/routes to see the routes
	// todo: notes: http://localhost:8072/ACCOUNTS/api/create this will redirect to ACCOUNTS ms (ACCOUTNS has to be upper case !!! defined in the eureka server)
	// todo: notes: see the configurations in the application.yml(lower-case-service-id: true) for lowercase the appname
	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

	// todo: notes: custom routing path, note that the default one will still work
	// todo: notes: we can achieve the same thing in properties file but use java bean is more flexible
	@Bean
	public RouteLocator kevinRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
		return routeLocatorBuilder.routes()
				.route(p -> p.path("/kevin/accounts/**")
					.filters(f -> f.rewritePath("/kevin/accounts/(?<segment>.*)","/${segment}")
							// todo: notes: add the response header in the response
							.addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
						.uri("lb://ACCOUNTS"))
				.route(p -> p.path("/kevin/loans/**")
					.filters(f -> f.rewritePath("/kevin/loans/(?<segment>.*)","/${segment}")
							.addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
					.uri("lb://LOANS"))
				.route(p -> p.path("/kevin/cards/**")
						.filters(f -> f.rewritePath("/kevin/cards/(?<segment>.*)","/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
						.uri("lb://CARDS")).build();

	}

}
