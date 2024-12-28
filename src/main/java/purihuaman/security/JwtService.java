package purihuaman.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import purihuaman.dto.UserDTO;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

	@Value("${jwt.secret.key}")
	private String SECRET_KEY;
	@Value("${jwt.time.expiration}")
	private Long TIME_EXPIRATION;

	public String generateToken(UserDTO user) {
		return buildToken(user, TIME_EXPIRATION);
	}

	public String buildToken(UserDTO user, Long expiration) {
		return Jwts
			.builder()
			.claims(Map.of("firstName", user.getFirstName()))
			.subject(user.getUsername())
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis() + expiration))
			.signWith(getKey())
			.compact();
	}

	public SecretKey getKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String getUsernameFromToken(String token) {
		final Claims jwtToken = Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
		return jwtToken.getSubject();
	}
}