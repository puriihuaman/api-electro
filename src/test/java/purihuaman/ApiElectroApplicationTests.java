package purihuaman;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import purihuaman.dao.repository.UserRepository;
import purihuaman.model.UserModel;

import java.util.UUID;

@SpringBootTest
class ApiElectroApplicationTests {
	@Autowired
	private UserRepository userRepository;

	private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	@Test
	void contextLoads() {
	}

	@Test
	void addUser() {
		UserModel user = new UserModel();

		user.setUserId(UUID.randomUUID().toString());
		user.setFirstName("Cielo");
		user.setLastName("Fernandez");
		user.setEmail("sky@gmail.com");
		user.setPassword(encoder.encode("CHICA_RAP"));

		UserModel savedUser = userRepository.save(user);
		Assertions.assertNotNull(savedUser);
	}

}
