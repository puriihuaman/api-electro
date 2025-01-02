package purihuaman.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import purihuaman.dto.UserDTO;
import purihuaman.enums.APIError;
import purihuaman.exception.APIRequestException;
import purihuaman.service.UserService;

@Service
@RequiredArgsConstructor
public class SecurityService {
	//	private final JwtService jwtService;
	//	private final UserService userService;

	//	public SecurityService(JwtService jwtService, UserService userService) {
	//		this.jwtService = jwtService;
	//		this.userService = userService;
	//	}

	//	@Override
	//	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
	//	throws AuthenticationException
	//	{
	//		UserDTO userModel = null;
	//		String username = "";
	//		String password = "";
	//
	//		try {
	//			userModel = new ObjectMapper().readValue(request.getInputStream(), UserDTO.class);
	//			username = userModel.getUsername();
	//			password = userModel.getPassword();
	//
	//		} catch (IOException e) {
	//			throw new RuntimeException(e);
	//		}
	//		UsernamePasswordAuthenticationToken
	//			authenticationToken =
	//			new UsernamePasswordAuthenticationToken(username, password);
	//		return authManager.authenticate(authenticationToken);
	//	}
	//
	//	@Override
	//	protected void successfulAuthentication(
	//		HttpServletRequest request,
	//		HttpServletResponse response,
	//		FilterChain chain,
	//		Authentication authResult
	//	) throws IOException, ServletException
	//	{
	//		User user = (User) authResult.getPrincipal();
	//		UserDTO userFound = userService.getUserByUsername(user.getUsername());
	//		String token = jwtService.generateToken(userFound);
	//		response.addHeader("Authorization ", token);
	//		Map<String, Object> httpResponse = new HashMap<>();
	//		httpResponse.put("Token", token);
	//		httpResponse.put("Message", "Autenticacion Correcta");
	//		httpResponse.put("Username", user.getUsername());
	//
	//		response.getWriter().write(new ObjectMapper().writeValueAsString(httpResponse));
	//		response.setStatus(HttpStatus.OK.value());
	//		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
	//		response.getWriter().flush();
	//
	//		super.successfulAuthentication(request, response, chain, authResult);
	//	}


	private final AuthenticationManager authManager;
	private final UserService userService;
	private final JwtService jwtService;

	public TokenResponse login(UserDTO user) {
		try {
			authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		} catch (BadCredentialsException ex) {
			throw new APIRequestException(APIError.INVALID_CREDENTIALS);
		} catch (Exception ex) {
			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
		}

		UserDTO userFound = userService.getUserByUsername(user.getUsername());
		String token = jwtService.generateToken(userFound);

		return new TokenResponse(token);
	}
}
