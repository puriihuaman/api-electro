package purihuaman.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import purihuaman.dto.UserDTO;
import purihuaman.security.SecurityService;
import purihuaman.security.TokenResponse;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final SecurityService securityService;

	@PostMapping("/login")
	public ResponseEntity<TokenResponse> authenticate(@RequestBody final UserDTO user) {
		final TokenResponse token = securityService.login(user);
		return ResponseEntity.ok(token);
	}
}
