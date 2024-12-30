package purihuaman.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
import purihuaman.service.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
	private final AppConfig appConfig;
	private final JwtService jwtService;
	private final UserService userService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	throws ServletException, IOException
	{
		if (request.getServletPath().contains("/api/auth")) {
			filterChain.doFilter(request, response);
			return;
		}

		final String TOKEN = getTokenFromRequest(request, response, filterChain);
		final String USERNAME = jwtService.getUsernameFromToken(TOKEN);

		if (USERNAME == null || SecurityContextHolder.getContext().getAuthentication() != null) {
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

		filterChain.doFilter(request, response);
	}

	private String getTokenFromRequest(
		final HttpServletRequest request,
		final HttpServletResponse response,
		final FilterChain chain
	) throws ServletException, IOException
	{
		final String AUTH_HEADER = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (AUTH_HEADER == null || !AUTH_HEADER.startsWith("Bearer ")) {
			 chain.doFilter(request, response);

			 return null;
		}

		return AUTH_HEADER.replace("Bearer ", "");
	}

}
