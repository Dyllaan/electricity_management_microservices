package com.louisfiges.gateway;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@EnableConfigurationProperties(UriConfiguration.class)
@RestController
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
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
						.path("/citizen/**")
						.filters(f -> f
								.rewritePath("/citizen(?<segment>/?.*)", "${segment}")
								.circuitBreaker(config -> config
										.setName("citizenCB")
										.setFallbackUri("forward:/citizen-fallback")))
						.uri(uriConfiguration.getCitizenService()))
				.route(p -> p
						.path("/prov-a/**")
						.filters(f -> f
								.rewritePath("/prov-a(?<segment>/?.*)", "${segment}")
								.circuitBreaker(config -> config
										.setName("providerACB")
										.setFallbackUri("forward:/provider-a-fallback")))
						.uri(uriConfiguration.getProviderAService()))
				.route(p -> p
						.path("/prov-b/**")
						.filters(f -> f
								.rewritePath("/prov-b(?<segment>/?.*)", "${segment}")
								.circuitBreaker(config -> config
										.setName("providerBCB")
										.setFallbackUri("forward:/provider-b-fallback")))
						.uri(uriConfiguration.getProviderBService()))
				.route(p -> p
						.path("/prov-c/**")
						.filters(f -> f
								.rewritePath("/prov-c(?<segment>/?.*)", "${segment}")
								.circuitBreaker(config -> config
										.setName("providerCCB")
										.setFallbackUri("forward:/provider-c-fallback")))
						.uri(uriConfiguration.getProviderCService()))
				.route(p -> p
						.path("/smart-city/**")
						.filters(f -> f
								.rewritePath("/smart-city(?<segment>/?.*)", "${segment}")
								.circuitBreaker(config -> config
										.setName("smartCityCB")
										.setFallbackUri("forward:/smart-city-fallback")))
						.uri(uriConfiguration.getSmartCityService()))
				.build();
	}


	@RequestMapping("/fallback")
	public Mono<String> fallback() {
		return Mono.just("Smart City service is currently unavailable. Please try again later.");
	}
}

@Configuration
@ConfigurationProperties(prefix = "uri")
class UriConfiguration {

	private String citizenService;
	private String providerAService;
	private String providerBService;
	private String providerCService;
	private String smartCityService;

	public String getCitizenService() {
		return citizenService;
	}

	public void setCitizenService(String citizenService) {
		this.citizenService = citizenService;
	}

	public String getProviderAService() {
		return providerAService;
	}

	public void setProviderAService(String providerAService) {
		this.providerAService = providerAService;
	}

	public String getProviderBService() {
		return providerBService;
	}

	public void setProviderBService(String providerBService) {
		this.providerBService = providerBService;
	}

	public String getProviderCService() {
		return providerCService;
	}

	public void setProviderCService(String providerCService) {
		this.providerCService = providerCService;
	}

	public String getSmartCityService() {
		return smartCityService;
	}

	public void setSmartCityService(String smartCityService) {
		this.smartCityService = smartCityService;
	}
}
