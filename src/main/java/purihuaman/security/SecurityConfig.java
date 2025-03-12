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

	private static final String ROLE_ADMIN = RoleType.ADMIN.toString();
	private static final String ROLE_USER = RoleType.USER.toString();
	private static final String ROLE_INVITED = RoleType.INVITED.toString();

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

			authRequest.requestMatchers(HttpMethod.POST, "/auth/**").permitAll();

			authRequest.requestMatchers("/users/**").hasAnyRole(ROLE_ADMIN);

			authRequest.requestMatchers(HttpMethod.GET, "/categories/**").hasAnyRole(
				ROLE_ADMIN,
				ROLE_USER,
				ROLE_INVITED
			);
			authRequest.requestMatchers(HttpMethod.POST, "/categories/**").hasAnyRole(ROLE_ADMIN, ROLE_USER);
			authRequest.requestMatchers(HttpMethod.PUT, "/categories/**").hasAnyRole(ROLE_ADMIN, ROLE_USER);
			authRequest.requestMatchers(HttpMethod.DELETE, "/categories/**").hasRole(ROLE_ADMIN);

			authRequest.requestMatchers(HttpMethod.GET, "/products/**").hasAnyRole(ROLE_ADMIN, ROLE_USER, ROLE_INVITED);
			authRequest.requestMatchers(HttpMethod.POST, "/products/**").hasAnyRole(ROLE_ADMIN, ROLE_USER);
			authRequest.requestMatchers(HttpMethod.PUT, "/products/**").hasAnyRole(ROLE_ADMIN, ROLE_USER);
			authRequest.requestMatchers(HttpMethod.DELETE, "/products/**").hasRole(ROLE_ADMIN);

			authRequest.anyRequest().authenticated();
		});

		http.sessionManagement((session) -> {
			session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

			session.maximumSessions(1);
		}).addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
