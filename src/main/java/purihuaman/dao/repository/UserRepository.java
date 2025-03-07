package purihuaman.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import purihuaman.entity.UserEntity;
import purihuaman.enums.RoleType;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity> {
//	@Query(value = "CALL get_users_by_filters(:firstName, :lastName, :email, :username, :offset, :limit)", nativeQuery = true)
//	List<UserEntity> filterUsers(@Param("firstName") String firstName, @Param("lastName") String lastName,
//	                             @Param("email") String email, @Param("username") String username, @Param("offset") short offset,
//	                             @Param("limit") short limit);

	Optional<UserEntity> findByUsernameAndPassword(String username, String password);

	Optional<UserEntity> findByUsername(String username);

//	Optional<UserEntity> findByRoleEntity(String roleName);
}
