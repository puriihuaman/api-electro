package purihuaman;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import purihuaman.dao.RoleDAO;
import purihuaman.dto.UserDTO;
import purihuaman.entity.RoleEntity;
import purihuaman.enums.RoleType;
import purihuaman.service.UserService;

import java.util.Optional;
import java.util.UUID;

@SpringBootTest
class ApiElectroApplicationTests {
	@Autowired
	private UserService userService;

	@Autowired
	private RoleDAO roleDao;

	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Test
	void contextLoads() {
	}

	@Test
	void createUser() {
		try {
			Optional<RoleEntity> optionalRole = roleDao.findRoleByRoleName(RoleType.ADMIN);

			if (optionalRole.isPresent()) {
				System.out.println("Creating admin user ....");
				String encodedPassword = passwordEncoder.encode("AdM¡N&20_25");

				UserDTO
					admin =
					UserDTO
						.builder()
						.firstName("admin")
						.lastName("admin")
						.email("anonymous@gmail.com")
						.username("admin")
						.password(encodedPassword)
						.role(optionalRole.get())
						.build();
				admin.setId(UUID.randomUUID().toString());

				UserDTO savedUser = userService.createUser(admin);
				System.out.println("User created ....: " + savedUser.getUsername());
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
