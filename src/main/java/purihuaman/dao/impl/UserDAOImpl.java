package purihuaman.dao.impl;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import purihuaman.dao.UserDAO;
import purihuaman.dao.repository.UserRepository;
import purihuaman.entity.UserEntity;

import java.util.Optional;

@Repository
public class UserDAOImpl implements UserDAO {
	private final UserRepository userRepository;

	public UserDAOImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public Page<UserEntity> findAllUsers(Pageable page) {
		return userRepository.findAll(page);
	}

	@Override
	public Optional<UserEntity> findUserById(String userId) {
		return userRepository.findById(userId);
	}

	@Override
	public Optional<UserEntity> findUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public Page<UserEntity> filterUsers(Specification<UserEntity> spec, Pageable page) {
		return userRepository.findAll(spec, page);
	}

	@Override
	public UserEntity createUser(@Valid UserEntity userEntity) {
		return userRepository.save(userEntity);
	}

	@Override
	public UserEntity updateUser(@Valid UserEntity userEntity) {
		return userRepository.save(userEntity);
	}

	@Override
	public void deleteUser(String userId) {
		userRepository.deleteById(userId);
	}
}
