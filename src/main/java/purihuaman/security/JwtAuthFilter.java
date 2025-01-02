package purihuaman.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import purihuaman.dto.UserDTO;
import purihuaman.enums.APIError;
import purihuaman.exception.APIRequestException;
import purihuaman.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
	private final AppConfig appConfig;
	private final JwtService jwtService;
	private final UserService userService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	throws APIRequestException
	{
		try {
			if (isAuthPath(request)) {
				filterChain.doFilter(request, response);
				return;
			}

			final String TOKEN = getTokenFromRequest(request);

			if (jwtService.isTokenValid(TOKEN)) {
				final String USERNAME = jwtService.getUsernameFromToken(TOKEN);

				if (USERNAME == null || SecurityContextHolder.getContext().getAuthentication() != null) {
					filterChain.doFilter(request, response);
					return;
				}

				final UserDetails userDetails = appConfig.userDetailsService().loadUserByUsername(USERNAME);
				UserDTO userDTO = userService.authentication(userDetails.getUsername(), userDetails.getPassword());

				if (userDTO == null) {
					filterChain.doFilter(request, response);
					return;
				}

				List<GrantedAuthority> roles = new ArrayList<>();
				roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

				var auth = new UsernamePasswordAuthenticationToken(userDTO, null, roles);
				auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(auth);
			}

			filterChain.doFilter(request, response);
		} catch (APIRequestException ex) {
			handleException(response, ex);
		} catch (Exception ex) {
			handleException(response, new APIRequestException(APIError.INTERNAL_SERVER_ERROR));
			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
		}
	}

	private boolean isAuthPath(HttpServletRequest request) {
		return request.getServletPath().contains("/api/auth");
	}

	private String getTokenFromRequest(final HttpServletRequest request) {
		try {

			final String AUTH_HEADER = request.getHeader(HttpHeaders.AUTHORIZATION);

			if (AUTH_HEADER == null) {
				System.out.println("Authorization header is missing.");
				throw new APIRequestException(
					APIError.UNAUTHORIZED_ACCESS,
					"Authorization header is missing",
					"Authorization header is missing."
				);
			}

			if (!AUTH_HEADER.startsWith("Bearer ")) {
				throw new APIRequestException(
					APIError.UNAUTHORIZED_ACCESS,
					"Authorization header must start with 'Bearer '",
					"The Authorization header must start with 'Bearer ' followed by a space and the actual token."
				);
			}

			final String TOKEN = AUTH_HEADER.replace("Bearer ", "");
			if (TOKEN.isEmpty())
				throw new APIRequestException(APIError.UNAUTHORIZED_ACCESS, "Empty token", "Do not provide a token");

			return TOKEN;
		} catch (APIRequestException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
		}
	}

	private void handleException(HttpServletResponse response, APIRequestException error) {
		response.setContentType("application/json");
		response.setStatus(error.getStatusCode().value());
		try {
			Map<String, Object> errorDetails = Map.of(
				"hasError",
				true,
				"message",
				error.getMessage(),
				"description",
				error.getDescription(),
				"statusCode",
				error.getStatusCode().value()
			);
			String jsonResponse = new ObjectMapper().writeValueAsString(errorDetails);
			response.getWriter().write(jsonResponse);
		} catch (Exception ex) {
			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
		}
	}
}
