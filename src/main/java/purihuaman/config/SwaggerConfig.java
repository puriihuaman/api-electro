package purihuaman.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@OpenAPIDefinition(
	info = @Info(
		title = "API Electro",
		version = "1.0.0",
		description = "Documentación de la API para gestionar usuarios, productos y categorias.",
		termsOfService = "http://swagger.io/terms/"
	), security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
	name = "bearerAuth",
	type = SecuritySchemeType.HTTP,
	paramName = HttpHeaders.AUTHORIZATION,
	in = SecuritySchemeIn.HEADER,
	scheme = "bearer",
	bearerFormat = "JWT"
)
public class SwaggerConfig {
	//	@Bean
	//	public OpenAPI getOpenAPI() {
	//		return new OpenAPI().components(new Components().addSecuritySchemes(
	//			"bearer-key",
	//			new SecurityScheme()
	//				.type(Type.HTTP)
	//				.scheme("bearer")
	//				.bearerFormat("JWT")
	//		)).info(new Info()
	//			        .title("API Electro Documentation")
	//			        .version("1.0.0")
	//			        .description("API Documentation")
	//			        .contact(new Contact()
	//				                 .name("Back-End Team")
	//				                 .email("pedropuriihuaman@gmail.com")));
	//	}
}
