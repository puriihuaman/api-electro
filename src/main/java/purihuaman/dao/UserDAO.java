package purihuaman.dao;

import org.springframework.data.domain.Pageable;
import purihuaman.dto.UserDTO;

import java.util.List;
import java.util.Map;

public interface UserDAO {
	List<UserDTO> getAllUsers(Pageable page);

	UserDTO getUserById(String userId);

	List<UserDTO> filterUsers(Map<String, String> filters, Pageable page);

	UserDTO addUser(UserDTO user);

	UserDTO updateUser(String userId, UserDTO user);
	
	Integer deleteUser(String userId);

	UserDTO authentication(String username, String password);

	UserDTO getUserByUsername(String username);
}
