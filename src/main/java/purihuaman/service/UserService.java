package purihuaman.service;

import org.springframework.data.domain.Pageable;
import purihuaman.dto.UserDTO;

import java.util.List;
import java.util.Map;

public interface UserService {
	List<UserDTO> findAllUsers(Pageable page);

	UserDTO findUserById(String userId);

	List<UserDTO> filterUsers(Map<String, String> filters, Pageable page);

	UserDTO createUser(UserDTO user);

	UserDTO updateUser(String userId, UserDTO user);

	void deleteUser(String userId);

	UserDTO authentication(String username, String password);

	UserDTO findUserByUsername(String username);
}
