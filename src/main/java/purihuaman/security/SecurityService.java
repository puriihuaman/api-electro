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

	public SecurityService(AuthenticationManager authManager, UserService userService, JwtUtil jwtUtil) {
		this.authManager = authManager;
		this.userService = userService;
		this.jwtUtil = jwtUtil;
	}

	// TODO: CHECK
	public TokenResponse login(LoginRequestDTO loginRequest) {
		try {
			UsernamePasswordAuthenticationToken
				authRequest =
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
			Authentication auth = authManager.authenticate(authRequest);

			UserDetails userDetails = (UserDetails) auth.getPrincipal();
			UserDTO existingUser = userService.findUserByUsername(userDetails.getUsername());

			String token = jwtUtil.generateToken(existingUser);

			return new TokenResponse(token, existingUser.getUsername(), existingUser.getRole().getRoleName());
		} catch (BadCredentialsException ex) {
			throw new APIRequestException(APIError.INVALID_CREDENTIALS);
		} catch (Exception ex) {
			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
		}
	}
}
