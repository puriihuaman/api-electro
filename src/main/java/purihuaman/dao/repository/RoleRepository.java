package purihuaman.dao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import purihuaman.entity.RoleEntity;
import purihuaman.enums.RoleType;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, String> {
	Optional<RoleEntity> findRoleByRoleName(RoleType roleName);
}
