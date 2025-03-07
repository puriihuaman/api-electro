package purihuaman.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import purihuaman.dto.LoginRequestDTO;
import purihuaman.dto.UserDTO;
import purihuaman.enums.APIError;
import purihuaman.exception.APIRequestException;
import purihuaman.service.UserService;

@Service
public class SecurityService {
	private final AuthenticationManager authManager;
	private final UserService userService;
	private final JwtUtil jwtUtil;

	public SecurityService(AuthenticationManager authManager, UserService userService, JwtUtil jwtUtil)
	{
		this.authManager = authManager;
		this.userService = userService;
		this.jwtUtil = jwtUtil;
	}

	// TODO: CHECK
	public TokenResponse login(LoginRequestDTO loginRequest) {
		try {
			Authentication
				auth =
				authManager.authenticate(new UsernamePasswordAuthenticationToken(
					loginRequest.getUsername(),
				                                                                 loginRequest.getPassword()
				));
			System.out.println("Authentication token: " + auth.isAuthenticated());
			UserDetails userDetails = (UserDetails) auth.getPrincipal();
			UserDTO existingUser = userService.findUserByUsername(userDetails.getUsername());

			System.out.println("User found: " + existingUser);

			String token = jwtUtil.generateToken(existingUser);
			System.out.println("TOKEN: " + token);

			return new TokenResponse(token, existingUser.getUsername(), existingUser.getRole().getRoleName());
		} catch (BadCredentialsException ex) {
			throw new APIRequestException(APIError.INVALID_CREDENTIALS);
		} catch (Exception ex) {
			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
		}

	}
}
