package purihuaman.security;

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

		String token = null;
		String username = null;
		final String BEARER_TEXT = "Bearer ";
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
					throw new APIRequestException(APIError.UNAUTHORIZED_ACCESS);
				}
			}

			filterChain.doFilter(request, response);
		} catch (APIRequestException | IllegalArgumentException ex) {
			throw new APIRequestException(APIError.UNAUTHORIZED_ACCESS);
		}
	}

	private boolean isAuthPath(HttpServletRequest request) {
		String servletPath = request.getServletPath();
		return servletPath.contains("/users/login") || servletPath.startsWith("/swagger-ui") || servletPath.contains(
			"/v3/api-docs/");
	}
}
