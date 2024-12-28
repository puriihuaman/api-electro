package purihuaman.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import purihuaman.dto.UserDTO;
import purihuaman.exception.ApiRequestException;
import purihuaman.service.UserService;

@Service
@RequiredArgsConstructor
public class SecurityService {
	private final AuthenticationManager authManager;
	private final UserService userService;
	private final JwtService jwtService;

	public TokenResponse login(UserDTO user) {
		try {
			authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		} catch (BadCredentialsException ex) {
			throw new ApiRequestException(
				true,
				"Invalid credentials",
				"Incorrect username or password. Try again with correct credentials.",
				HttpStatus.UNAUTHORIZED
			);
		} catch (Exception ex) {
			throw new ApiRequestException(true, "An occurred error", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		UserDTO userFound = userService.getUserByUsername(user.getUsername());

		String token = jwtService.generateToken(userFound);

		return new TokenResponse(token);
	}
}
