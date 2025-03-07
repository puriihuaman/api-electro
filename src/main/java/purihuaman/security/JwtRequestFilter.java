package purihuaman.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import purihuaman.enums.APIError;
import purihuaman.exception.APIRequestException;
import purihuaman.exception.ErrorResponse;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	private final UserDetailsService userDetailsService;
	private final JwtUtil jwtUtil;

	public JwtRequestFilter(UserDetailsService userDetailsService, JwtUtil jwtUtil) {
		this.userDetailsService = userDetailsService;
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(
		@NonNull HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain
	) throws ServletException, IOException
	{
		if (this.isAuthPath(request)) {
			filterChain.doFilter(request, response);
			return;
		}

		final String BEARER_TEXT = "Bearer ";
		String token = null;
		String username = null;
		final String AUTH_HEADER = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (AUTH_HEADER != null && AUTH_HEADER.startsWith(BEARER_TEXT)) {
			token = AUTH_HEADER.substring(BEARER_TEXT.length());
			username = jwtUtil.extractUsername(token);
		}


		try {
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);

				if (jwtUtil.validateToken(token, userDetails)) {
					UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
						userDetails,
					                                                                                   null,
					                                                                                   userDetails.getAuthorities()
					);
					auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(auth);
				} else {
					this.sendErrorResponse(response, "Invalid Token", "The token is invalid or has expired.");
					return;
				}
			}

			filterChain.doFilter(request, response);
		} catch (APIRequestException | IllegalArgumentException ex) {
			this.sendErrorResponse(response, APIError.UNAUTHORIZED_ACCESS.getMessage(), ex.getMessage());
		}
	}

	private boolean isAuthPath(HttpServletRequest request) {
		String servletPath = request.getServletPath();
		return servletPath.contains("/api/users/login") ||
			servletPath.startsWith("/swagger-ui") ||
			servletPath.contains("/v3/api-docs/");
	}

	private String getTokenFromRequest(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		final String BEARER = "Bearer ";
		final String AUTH_HEADER = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (AUTH_HEADER == null || !AUTH_HEADER.startsWith(BEARER)) {
			this.sendErrorResponse(
				response,
				"Authorization header must start with 'Bearer '",
				"The Authorization header must start with 'Bearer ' followed by a space and the actual token."
			);
			throw new APIRequestException(APIError.UNAUTHORIZED_ACCESS);
		}

		final String TOKEN = AUTH_HEADER.replace(BEARER, "");

		if (TOKEN.isEmpty()) {
			this.sendErrorResponse(response, "Empty token", "Do not provide a token");
			throw new APIRequestException(APIError.UNAUTHORIZED_ACCESS);
		}

		return TOKEN;
	}

	private void sendErrorResponse(HttpServletResponse response, String message, String description) throws IOException
	{
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		ErrorResponse error = new ErrorResponse(true, message, description, HttpServletResponse.SC_UNAUTHORIZED);
		String jsonResponse = new ObjectMapper().writeValueAsString(error);
		response.getWriter().write(jsonResponse);
	}
}
