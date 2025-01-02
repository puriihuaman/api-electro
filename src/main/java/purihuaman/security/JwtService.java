package purihuaman.security;

import io.jsonwebtoken.*;
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
		try {
			if (token == null || token.isEmpty()) {
				throw new APIRequestException(APIError.UNAUTHORIZED_ACCESS, "Invalid token", "Token is null or empty");
			}

			return getUsernameOrExpirationFromToken(token, Claims::getSubject);
		} catch (Exception ex) {
			throw new APIRequestException(APIError.INTERNAL_SERVER_ERROR);
		}
	}

	public boolean isTokenValid(final String token) {
		return isTokenExpired(token);
	}

	private boolean isTokenExpired(final String token) {
		try {
			return extractExpiration(token).before(new Date());
		} catch (ExpiredJwtException ex) {
			throw new APIRequestException(
				APIError.UNAUTHORIZED_ACCESS,
				"Token expired",
				"The provided token has expired. Please authenticate again to obtain a new token."
			);
		}
	}

	private Date extractExpiration(final String token) {
		return getUsernameOrExpirationFromToken(token, Claims::getExpiration);
	}

	private <T> T getUsernameOrExpirationFromToken(final String token, Function<Claims, T> claimsResolver) {
		try {
			Claims claims = Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
			return claimsResolver.apply(claims);
		} catch (SignatureException ex) {
			throw new APIRequestException(
				APIError.UNAUTHORIZED_ACCESS,
				"Invalid token signature",
				"The token signature is invalid. Ensure the token is correctly signed with the expected key."
			);
		} catch (UnsupportedJwtException ex) {
			throw new APIRequestException(
				APIError.UNAUTHORIZED_ACCESS,
				"Unsupported token",
				"The provided token is unsupported. Please use a compatible JWT format."
			);
		} catch (MalformedJwtException ex) {
			throw new APIRequestException(
				APIError.UNAUTHORIZED_ACCESS,
				"Malformed token",
				"The token structure is malformed. Verify that the token is well-formed and complete."
			);
		} catch (IllegalArgumentException ex) {
			throw new APIRequestException(
				APIError.UNAUTHORIZED_ACCESS,
				"Empty or null token",
				"The token is empty or null. Ensure a valid token is provided."
			);
		}
	}
}