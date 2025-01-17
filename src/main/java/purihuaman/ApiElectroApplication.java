package purihuaman;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import purihuaman.dao.repository.RoleRepository;
import purihuaman.dao.repository.UserRepository;
import purihuaman.enums.ERole;
import purihuaman.model.RoleModel;
import purihuaman.model.UserModel;

import java.util.Set;
import java.util.UUID;

@SpringBootApplication
public class ApiElectroApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiElectroApplication.class, args);
	}

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private UserRepository userRepository;

	@Bean
	CommandLineRunner init(RoleRepository roleRepository) {
		return args -> {
			UserModel user = new UserModel();

			RoleModel role = roleRepository.findByRoleName(ERole.ADMIN).orElse(null);
			assert role != null;

			String encodedPassword = encoder.encode("AdM¡N&20_25");

			user.setUserId(UUID.randomUUID().toString());
			user.setFirstName("Admin");
			user.setLastName("Anonymous");
			user.setEmail("anonymous@gmail.com");
			user.setRoles(Set.of(role));
			user.setPassword(encodedPassword);

			UserModel savedUser = userRepository.save(user);

			System.out.println("-------- Created user --------");
			System.out.println(savedUser.getFirstName());
			System.out.println(savedUser.getUsername());
			System.out.println(savedUser.getLastName());
			System.out.println(savedUser.getEmail());
			System.out.println("----------------");

			/**
			 * The username is made up of:
			 * 	The first 4 letters of the surname in capital letters concatenated with the current year.
			 *
			 * 	In this case it would be: ANON2025
			 *
			 * The username is auto-generated by a trigger in the database, when creating the user.
			 */
		};
	}
}
