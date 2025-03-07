package purihuaman;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import purihuaman.dao.repository.UserRepository;
import purihuaman.entity.UserEntity;

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
		UserEntity userEntity = new UserEntity();

		userEntity.setUserId(UUID.randomUUID().toString());
		userEntity.setFirstName("Spring");
		userEntity.setLastName("Spring Boot");
		userEntity.setEmail("spring@gmail.com");
		userEntity.setPassword(encoder.encode("SPR¡NG_boot_2025"));

		UserEntity savedUserEntity = userRepository.save(userEntity);
		Assertions.assertNotNull(savedUserEntity);
	}

}
