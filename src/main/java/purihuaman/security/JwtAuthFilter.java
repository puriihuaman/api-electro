package purihuaman.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import purihuaman.exception.ErrorResponse;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
	private final AppConfig appConfig;
	private final JwtService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	throws ServletException, IOException
	{
		if (isAuthPath(request)) {
			filterChain.doFilter(request, response);
			return;
		}
		final String TOKEN = getTokenFromRequest(request, response);

		if (TOKEN == null) return;

		if (!jwtService.isTokenValid(TOKEN)) {
			sendErrorResponse(
				response,
				"Token expired",
				"The provided token has expired. Please authenticate again to obtain a new token."
			);
			return;
		}

		final String USERNAME = jwtService.getUsernameFromToken(TOKEN);

		if (USERNAME == null || SecurityContextHolder.getContext().getAuthentication() != null) {
			filterChain.doFilter(request, response);
			return;
		}

		final UserDetails user = appConfig.userDetailsService().loadUserByUsername(USERNAME);

		var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(auth);
		filterChain.doFilter(request, response);
	}

	private boolean isAuthPath(HttpServletRequest request) {
		return request.getServletPath().contains("/api/auth");
	}

	private String getTokenFromRequest(final HttpServletRequest request, final HttpServletResponse response)
	throws IOException
	{
		final String AUTH_HEADER = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (AUTH_HEADER == null || !AUTH_HEADER.startsWith("Bearer ")) {
			sendErrorResponse(
				response,
				"Authorization header must start with 'Bearer '",
				"The Authorization header must start with 'Bearer ' followed by a space and the actual token."
			);
			return null;
		}

		final String TOKEN = AUTH_HEADER.replace("Bearer ", "");

		if (TOKEN.isEmpty()) {
			sendErrorResponse(response, "Empty token", "Do not provide a token");
			return null;
		}

		return TOKEN;
	}

	private void sendErrorResponse(final HttpServletResponse response, final String message, final String description)
	throws IOException
	{
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		ErrorResponse error = new ErrorResponse(true, message, description, HttpServletResponse.SC_UNAUTHORIZED);
		String jsonResponse = new ObjectMapper().writeValueAsString(error);
		response.getWriter().write(jsonResponse);
	}
}
