package purihuaman.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import purihuaman.model.UserModel;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, String> {
	@Query(
		nativeQuery = true, value = "CALL get_users_by_filters(:firstName, :lastName, :email, :username, :offset, :limit)"
	)
	List<UserModel> filterUsers(
		@Param("firstName") String firstName,
		@Param("lastName") String lastName,
		@Param("email") String email,
		@Param("username") String username,
		@Param("offset") Short offset,
		@Param("limit") Short limit
	);

	Optional<UserModel> findByUsernameAndPassword(String username, String password);

	Optional<UserModel> findByUsername(String username);
}
