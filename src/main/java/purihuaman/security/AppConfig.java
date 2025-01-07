package purihuaman.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import purihuaman.dto.UserDTO;
import purihuaman.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
	private final UserService userService;

	@Bean
	public UserDetailsService userDetailsService() {
		return (username) -> {
			final UserDTO userFound = userService.getUserByUsername(username);
			UserDTO user = userService.authentication(userFound.getUsername(), userFound.getPassword());

			List<GrantedAuthority>
				roles =
				user
					.getRoles()
					.stream()
					.map((role) -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName().name()))
					.collect(Collectors.toList());

			return User.builder().username(user.getUsername()).password(user.getPassword()).authorities(roles).build();
		};
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthProvider = new DaoAuthenticationProvider();
		daoAuthProvider.setUserDetailsService(userDetailsService());
		daoAuthProvider.setPasswordEncoder(passwordEncoder());

		return daoAuthProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
