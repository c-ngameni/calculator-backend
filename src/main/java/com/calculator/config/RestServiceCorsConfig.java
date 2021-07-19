package com.calculator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration to enable Cross-Origin Resource Sharing (CORS).
 */
@Configuration
public class RestServiceCorsConfig {

	@Bean
	public WebMvcConfigurer corsConfigurer() {

		return new WebMvcConfigurer() {

			@Override
			public void addCorsMappings(CorsRegistry registry) {

				registry.addMapping("/greetings").allowedMethods("GET");
				registry.addMapping("/users").allowedMethods("GET", "POST");
			}
		};
	}

}
