package purihuaman.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import purihuaman.enums.RoleType;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	private final JwtRequestFilter jwtRequestFilter;

	public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
		this.jwtRequestFilter = jwtRequestFilter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
	{
		http.csrf(AbstractHttpConfigurer::disable);

		http.authorizeHttpRequests((authRequest) -> {
			authRequest.requestMatchers(
				"/swagger-ui/**",
				"/v3/api-docs/**",
				"/swagger-ui.html",
				"/swagger-resources/**"
			).permitAll();

			authRequest.requestMatchers(HttpMethod.POST, "/users/login").permitAll();
			authRequest.requestMatchers("/users/**").hasAnyRole(RoleType.ADMIN.toString());

			authRequest.requestMatchers(HttpMethod.GET, "/categories/**").hasAnyRole("ADMIN", "USER", "INVITED");
			authRequest.requestMatchers(HttpMethod.POST, "/categories/**").hasAnyRole("ADMIN", "USER");
			authRequest.requestMatchers(HttpMethod.PUT, "/categories/**").hasAnyRole("ADMIN", "USER");
			authRequest.requestMatchers(HttpMethod.DELETE, "/categories/**").hasRole("ADMIN");

			authRequest.requestMatchers(HttpMethod.GET, "/products/**").hasAnyRole("ADMIN", "USER", "INVITED");
			authRequest.requestMatchers(HttpMethod.POST, "/products/**").hasAnyRole("ADMIN", "USER");
			authRequest.requestMatchers(HttpMethod.PUT, "/products/**").hasAnyRole("ADMIN", "USER");
			authRequest.requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("ADMIN");

			authRequest.anyRequest().authenticated();
		});

		http.sessionManagement((session) -> {
			session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

			session.maximumSessions(1);
		}).addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
