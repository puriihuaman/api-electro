package purihuaman.dao;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import purihuaman.entity.UserEntity;

public interface UserDAO {
	Page<UserEntity> findAllUsers(Pageable page);

	Optional<UserEntity> findUserById(String userId);

	Page<UserEntity> filterUsers(Specification<UserEntity> spec, Pageable page);

	UserEntity createUser(UserEntity userEntity);

	UserEntity updateUser(UserEntity userEntity);

	void deleteUser(String userId);

	Optional<UserEntity> authentication(String username, String password);

	Optional<UserEntity> findUserByUsername(String username);
}
