package purihuaman.dao.repository;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import purihuaman.enums.ERole;
import purihuaman.model.RoleModel;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleModel, String> {
	Optional<RoleModel> findByRoleName(
		@NotNull(message = "${field.null}") @NotEmpty(message = "${field.empty}") ERole roleName
	);
}
