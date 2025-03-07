package purihuaman.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import purihuaman.dao.UserDAO;
import purihuaman.dto.UserDTO;
import purihuaman.entity.UserEntity;
import purihuaman.enums.APIError;
import purihuaman.exception.APIRequestException;
import purihuaman.mapper.UserMapper;

import java.util.Optional;

@Configuration
public class AppConfig {
	private final UserDAO userDAO;
	private final UserMapper userMapper;

	public AppConfig(UserMapper userMapper, UserDAO userDAO) {
		this.userMapper = userMapper;
		this.userDAO = userDAO;
	}

	// TODO: CHECK
	@Bean
	public UserDetailsService userDetailsService() {
		final String ROLE_PREFIX = "ROLE_";
		return (username) -> {
			Optional<UserEntity> existingUser = userDAO.findUserByUsername(username.toLowerCase());
			if (existingUser.isEmpty()) {
				throw new APIRequestException(APIError.ENDPOINT_NOT_FOUND);
			}
			UserDTO userDTO = userMapper.toUserDTO(existingUser.get());
			String roleName = ROLE_PREFIX + userDTO.getRole().getRoleName();

			SimpleGrantedAuthority role = new SimpleGrantedAuthority(roleName);

			return User
				.builder()
				.username(userDTO.getUsername())
				.password(userDTO.getPassword())
				.authorities(role)
				.build();
		};
	}

	// TODO: CHECK
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthProvider = new DaoAuthenticationProvider();
		daoAuthProvider.setUserDetailsService(this.userDetailsService());
		daoAuthProvider.setPasswordEncoder(this.passwordEncoder());

		return daoAuthProvider;
	}

	// TODO: CHECK
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	// TODO: CHECK
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}