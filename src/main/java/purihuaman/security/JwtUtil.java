package purihuaman.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.lang.Function;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import purihuaman.dto.UserDTO;
import purihuaman.enums.APIError;
import purihuaman.exception.APIRequestException;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
	@Value("${jwt.secret.key}")
	private String SECRET_KEY;
	@Value("${jwt.time.expiration}")
	private Long TIME_EXPIRATION;

	public String extractUsername(String token) {
		if (token == null || token.isEmpty()) {
			throw new APIRequestException(APIError.UNAUTHORIZED_ACCESS, "Invalid token", "Token is null or empty");
		}

		return this.extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return this.extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = this.extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().verifyWith(this.getKey()).build().parseSignedClaims(token).getPayload();
	}

	public boolean isTokenExpired(String token) {
		return this.extractExpiration(token).before(new Date());
	}

	public String generateToken(UserDTO userDTO) {
		final String ROLE_PREFIX = "ROLE_";
		Map<String, Object> claims = new HashMap<>();
		claims.put("firstName", userDTO.getFirstName());
		claims.put("lastName", userDTO.getLastName());
		claims.put("role", ROLE_PREFIX + userDTO.getRole().getRoleName());

		return this.buildToken(claims, userDTO.getUsername());
	}

	public String buildToken(Map<String, Object> claims, String subject) {
		String
			token =
			Jwts
				.builder()
				.claims(claims)
				.subject(subject)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + TIME_EXPIRATION))
				.signWith(this.getKey(), Jwts.SIG.HS256)
				.compact();
		System.out.println("token: " + token);
		return token;
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		final String username = this.extractUsername(token);
		return (username.equals(userDetails.getUsername())) && !this.isTokenExpired(token);
	}

	public SecretKey getKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		//byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}