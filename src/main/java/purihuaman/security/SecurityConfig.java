package purihuaman.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import purihuaman.enums.APIError;
import purihuaman.exception.APIRequestException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
	private final AuthenticationProvider authProvider;
	private final JwtAuthFilter jwtAuthFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests((authRequest) -> authRequest
				.requestMatchers("/api/auth/**")
				.permitAll()
				.anyRequest()
				.authenticated());

		http
			.sessionManagement((session) -> {
				session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

				session.maximumSessions(1);
			})
			.authenticationProvider(authProvider)
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
			.logout((logout) -> {
				logout.logoutUrl("/api/auth/logout");
				logout.addLogoutHandler((request, response, authentication) -> {
					final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
					logout(authHeader);
				});
				logout.logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());
			});

		return http.build();
	}

	private void logout(String token) {
		if (token == null || !token.startsWith("Bearer ")) {
			throw new APIRequestException(
				APIError.UNAUTHORIZED_ACCESS,
				"Invalid token. Please re-login",
				"The provided token is invalid or has expired."
			);
		}
	}
}
