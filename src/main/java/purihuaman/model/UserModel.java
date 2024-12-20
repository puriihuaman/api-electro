package purihuaman.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class UserModel {
  @Id
  @Column(name = "user_id", length = 40)
  private String userId;

  @NotNull(message = "The field is null")
  @NotEmpty(message = "The field is empty")
  @Size(min = 2, message = "The field must contain at least 2 characters")
  @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*$", message = "The field can only contain letters and spaces")
  @Column(name = "first_name", length = 45, nullable = false)
  private String firstName;

  @NotNull(message = "The field is null")
  @NotEmpty(message = "The field is empty")
  @Size(min = 2, message = "The field must contain at least 2 characters")
  @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*$", message = "The field can only contain letters and spaces")
  @Column(name = "last_name", length = 60, nullable = false)
  private String lastName;

  @NotNull(message = "The field is null")
  @NotEmpty(message = "The field is empty")
  @Email(message = "The field must be a valida email")
  @Column(name = "email", length = 50, nullable = false)
  private String email;

  @NotNull(message = "The field is null")
  @NotEmpty(message = "The field is empty")
  @Column(name = "username", length = 15, nullable = false)
  private String username;

  @NotNull(message = "The field is null")
  @NotEmpty(message = "The field is empty")
  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$",
      message = "The field must contain at least 1 lowercase letter, 1 uppercase letter, 1 number and 1 special character")
  @Column(name = "password", length = 100, nullable = false)
  private String password;
}
