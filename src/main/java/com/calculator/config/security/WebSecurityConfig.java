package com.calculator.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration for Spring Security.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private CustomBasicAuthenticationEntryPoint authenticationEntryPoint;

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(userDetailsService);
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// Disabling CSRF to permit POST requests without using a CSRF token.
		// Enabling Basic authentication.
		http.csrf().disable().authorizeRequests()
			.antMatchers("/password-reset-tokens").hasRole("ADMIN")
			.antMatchers("/", "/greeting", "/status", "/users/registration", "/users/password-reset", "/users/password-change", "/users/password-update").permitAll()
			.antMatchers("/swagger-ui").permitAll()
			.anyRequest().authenticated().and().httpBasic().authenticationEntryPoint(authenticationEntryPoint);
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {

		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	//	@Bean
	//	@Override
	//	public UserDetailsService userDetailsService() {
	//
	//		UserDetails user = User.withDefaultPasswordEncoder().username("john.doe@gmail.com").password("Password").roles("USER").build();
	//
	//		return new InMemoryUserDetailsManager(user);
	//	}

	@Bean
	public PasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();
	}
}