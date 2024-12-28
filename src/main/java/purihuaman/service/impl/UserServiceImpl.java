package purihuaman.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import purihuaman.dao.UserDAO;
import purihuaman.dto.UserDTO;
import purihuaman.service.UserService;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserDAO userDAO;

	@Override
	public List<UserDTO> getAllUsers(Pageable page) {
		return userDAO.getAllUsers(page);
	}

	@Override
	public UserDTO getUserById(String userId) {
		return userDAO.getUserById(userId);
	}

	@Override
	public List<UserDTO> filterUsers(Map<String, String> filters, Pageable page) {
		return userDAO.filterUsers(filters, page);
	}

	@Override
	public UserDTO addUser(UserDTO user) {
		return userDAO.addUser(user);
	}

	@Override
	public UserDTO updateUser(String userId, UserDTO user) {
		return userDAO.updateUser(userId, user);
	}

	@Override
	public Integer deleteUser(String userId) {
		return userDAO.deleteUser(userId);
	}

	@Override
	public UserDTO authentication(String username, String password) {
		return userDAO.authentication(username, password);
	}

	@Override
	public UserDTO getUserByUsername(String username) {
		return userDAO.getUserByUsername(username);
	}
}
