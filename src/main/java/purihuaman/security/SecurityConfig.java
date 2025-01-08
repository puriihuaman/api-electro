package purihuaman.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final AuthenticationProvider authProvider;
	private final JwtAuthFilter jwtAuthFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable);

		http.authorizeHttpRequests((authRequest) -> {
			authRequest.requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll();

			authRequest.requestMatchers(HttpMethod.GET, "/api/categories/**").hasAnyRole("ADMIN", "USER", "INVITED");
			authRequest.requestMatchers(HttpMethod.POST, "/api/categories/**").hasAnyRole("ADMIN", "USER");
			authRequest.requestMatchers(HttpMethod.PUT, "/api/categories/**").hasAnyRole("ADMIN", "USER");
			authRequest.requestMatchers(HttpMethod.DELETE, "/api/categories/**").hasRole("ADMIN");

			authRequest.requestMatchers(HttpMethod.GET, "/api/products/**").hasAnyRole("ADMIN", "USER", "INVITED");
			authRequest.requestMatchers(HttpMethod.POST, "/api/products/**").hasAnyRole("ADMIN", "USER");
			authRequest.requestMatchers(HttpMethod.PUT, "/api/products/**").hasAnyRole("ADMIN", "USER");
			authRequest.requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN");

			authRequest.anyRequest().authenticated();
		});

		http.sessionManagement((session) -> {
			session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

			session.maximumSessions(1);
		}).authenticationProvider(authProvider).addFilterBefore(
			jwtAuthFilter,
			UsernamePasswordAuthenticationFilter.class
		);

		return http.build();
	}
}
