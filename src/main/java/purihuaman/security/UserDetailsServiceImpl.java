package purihuaman.security;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import purihuaman.dao.RoleDAO;
import purihuaman.dto.UserDTO;
import purihuaman.entity.RoleEntity;
import purihuaman.enums.RoleType;
import purihuaman.mapper.UserMapper;
import purihuaman.service.UserService;

import java.util.Optional;
import java.util.UUID;

//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
public class UserDetailsServiceImpl{
//	private final UserService userService;
//	private final RoleDAO roleDao;
//	private final UserMapper userMapper;
//
//	public UserDetailsServiceImpl(UserService userService, RoleDAO roleDao, UserMapper userMapper) {
//		this.userService = userService;
//		this.roleDao = roleDao;
//		this.userMapper = userMapper;
//	}
//
//	@PostConstruct
//	public void init() {
//		try {
//			Optional<RoleEntity> optionalRole = roleDao.findRoleByRoleName(RoleType.ADMIN);
//
//			if (optionalRole.isPresent()) {
//				System.out.println("Creating admin user ....");
//				String encodedPassword = this.passwordEncoder().encode("AdM¡N&20_25");
//
//				UserDTO
//					admin =
//					UserDTO
//						.builder()
//						.firstName("admin")
//						.lastName("admin")
//						.email("anonymous@gmail.com")
//						.username("admin")
//						.password(encodedPassword)
//						.role(optionalRole.get())
//						.build();
//				admin.setId(UUID.randomUUID().toString());
//
//				UserDTO savedUser = userService.createUser(admin);
//				System.out.println("User created ....: " + savedUser.getUsername());
//			}
//
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//	}
//
//	//	@Bean
//	//	public UserDetailsService userDetailsService() {
//	//		return (username) -> {
//	//			final UserDTO userFound = userService.getUserByUsername(username);
//	//			UserDTO user = userService.authentication(userFound.getUsername(), userFound.getPassword());
//	//
//	//			//List<GrantedAuthority> roles = user
//	//			//	.getRole()
//	//			//	.stream()
//	//			//	.map((role) -> new SimpleGrantedAuthority("ROLE_" +
//	//			//		                                          role))
//	//			//	.collect(Collectors.toList());
//	//
//	//			SimpleGrantedAuthority role = new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName());
//	//
//	//			return User.builder().username(user.getUsername()).password(user.getPassword()).authorities(role).build();
//	//		};
//	//	}
//
//	//	@Bean
//	//	public AuthenticationProvider authenticationProvider() {
//	//		DaoAuthenticationProvider daoAuthProvider = new DaoAuthenticationProvider();
//	//		daoAuthProvider.setUserDetailsService(this.userDetailsService());
//	//		daoAuthProvider.setPasswordEncoder(this.passwordEncoder());
//	//
//	//		return daoAuthProvider;
//	//	}
//
//	@Bean
//	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//		return authConfig.getAuthenticationManager();
//	}
//
//	@Lazy
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//
//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		UserDTO userDTO = userService.getUserByUsername(username);
//
//		return User.builder().username(userDTO.getUsername()).password(userDTO.getPassword()).roles(userDTO
//			                                                                                            .getRole()
//			                                                                                            .getRoleName()
//			                                                                                            .getRoleName()
//			                                                                                            .replace(
//				                                                                                            "ROLE_",
//				                                                                                            ""
//			                                                                                            )).build();
//	}
}
