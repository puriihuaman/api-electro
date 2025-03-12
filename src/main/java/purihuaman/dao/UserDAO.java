package purihuaman.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import purihuaman.entity.UserEntity;

import java.util.Optional;

public interface UserDAO {
	Page<UserEntity> findAllUsers(Pageable page);

	Optional<UserEntity> findUserById(String userId);

	Page<UserEntity> filterUsers(Specification<UserEntity> spec, Pageable page);

	UserEntity createUser(UserEntity userEntity);

	UserEntity updateUser(UserEntity userEntity);

	void deleteUser(String userId);

	Optional<UserEntity> findUserByUsername(String username);
}
