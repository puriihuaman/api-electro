package purihuaman.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import purihuaman.dto.UserDTO;
import purihuaman.enums.APIError;
import purihuaman.exception.APIRequestException;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class JwtService {
	@Value("${jwt.secret.key}")
	private String SECRET_KEY;
	@Value("${jwt.time.expiration}")
	private Long TIME_EXPIRATION;

	public String generateToken(UserDTO user) {
		return buildToken(user);
	}

	public String buildToken(UserDTO user) {
		return Jwts
			.builder()
			.claims(Map.of("firstName", user.getFirstName()))
			.subject(user.getUsername())
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis() + TIME_EXPIRATION))
			.signWith(getKey())
			.compact();
	}

	public SecretKey getKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String getUsernameFromToken(String token) {
		if (token == null || token.isEmpty()) {
			throw new APIRequestException(APIError.UNAUTHORIZED_ACCESS, "Invalid token", "Token is null or empty");
		}
		return getUsernameOrExpirationFromToken(token, Claims::getSubject);
	}

	public boolean isTokenValid(final String token) {
		try {
			Date getExpiration = getUsernameOrExpirationFromToken(token, Claims::getExpiration);
			return !getExpiration.before(new Date());
		} catch (ExpiredJwtException | MalformedJwtException | SignatureException | IllegalArgumentException ex) {
			return false;
		}
	}

	private <T> T getUsernameOrExpirationFromToken(final String token, Function<Claims, T> claimsResolver) {
		Claims claims = Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
		return claimsResolver.apply(claims);
	}
}