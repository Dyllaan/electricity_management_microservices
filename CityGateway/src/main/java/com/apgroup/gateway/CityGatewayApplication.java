package com.apgroup.gateway;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Louis Figes (w21017657)
 * Gateway to all our four cities
 * these names needs changed but yknow.
 */
@SpringBootApplication
@EnableConfigurationProperties(UriConfiguration.class)
@RestController
public class CityGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(CityGatewayApplication.class, args);
	}

	/**
	 * This method creates routes for the gateway
	 * With Circuit Breaker pattern and Fallback URI for each route
	 * @param builder RouteLocatorBuilder object to build routes
	 * @param uriConfiguration uris for each microservice as specified in application.properties
	 * @return
	 */
	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder, UriConfiguration uriConfiguration) {
		return builder.routes()
				.route(p -> p
						.path("/newcastle/**")
						.filters(f -> f
								.rewritePath("/newcastle(?<segment>/?.*)", "${segment}")
								.circuitBreaker(config -> config
										.setName("newcastle-CB")
										.setFallbackUri("forward:/newcastle-fallback")))

						.uri(uriConfiguration.getNewcastleService()))
				.route(p -> p
						.path("/sunderland/**")
						.filters(f -> f
								.rewritePath("/sunderland(?<segment>/?.*)", "${segment}")
								.circuitBreaker(config -> config
										.setName("sunderland-CB")
										.setFallbackUri("forward:/sunderland-fallback")))
						.uri(uriConfiguration.getSunderlandService()))
				.route(p -> p
						.path("/durham/**")
						.filters(f -> f
								.rewritePath("/durham(?<segment>/?.*)", "${segment}")
								.circuitBreaker(config -> config
										.setName("durham-CB")
										.setFallbackUri("forward:/durham-fallback")))
						.uri(uriConfiguration.getDurhamService()))
				.route(p -> p
						.path("/darlington/**")
						.filters(f -> f
								.rewritePath("/darlington(?<segment>/?.*)", "${segment}")
								.circuitBreaker(config -> config
										.setName("darlington-CB")
										.setFallbackUri("forward:/darlington-fallback")))
						.uri(uriConfiguration.getDarlingtonService()))
				.route(p -> p
						.path("/auth/**")
						.filters(f -> f
								.rewritePath("/auth(?<segment>/?.*)", "${segment}")
								.circuitBreaker(config -> config
										.setName("auth-CB")
										.setFallbackUri("forward:/fallback")))
						.uri(uriConfiguration.getAuthService()))
				.build();
	}

	@RequestMapping("/fallback")
	public Mono<ResponseEntity<Object>> fallback() {
		Map<String, String> responseBody = new HashMap<>();
		responseBody.put("cause", "NE Electricity is currently unavailable. Please try again later.");

		return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody));
	}
}

@Configuration
@ConfigurationProperties(prefix = "uri")
class UriConfiguration {

	private String newcastleService;
	private String sunderlandService;
	private String durhamService;
	private String darlingtonService;
	private String authService;

	public String getNewcastleService() {
		return newcastleService;
	}

	public void setNewcastleService(String newcastleService) {
		this.newcastleService = newcastleService;
	}

	public String getSunderlandService() {
		return sunderlandService;
	}

	public void setSunderlandService(String sunderlandService) {
		this.sunderlandService = sunderlandService;
	}

	public String getDurhamService() {
		return durhamService;
	}

	public void setDurhamService(String durhamService) {
		this.durhamService = durhamService;
	}

	public String getDarlingtonService() {
		return darlingtonService;
	}

	public void setDarlingtonService(String darlingtonService) {
		this.darlingtonService = darlingtonService;
	}

	public String getAuthService() {
		return authService;
	}

	public void setAuthService(String authService) {
		this.authService = authService;
	}

}
