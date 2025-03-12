package purihuaman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@SpringBootApplication
public class ApiElectroApplication {
	public static void main(String[] args) {
		SpringApplication.run(ApiElectroApplication.class, args);
	}
}
