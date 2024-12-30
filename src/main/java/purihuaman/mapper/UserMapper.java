package purihuaman.mapper;

import org.mapstruct.Mapper;
import purihuaman.dto.UserDTO;
import purihuaman.model.UserModel;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
	UserModel toUserModel(UserDTO userDTO);

	UserDTO toUserDTO(UserModel userModel);

	List<UserModel> toUserModelList(List<UserDTO> userDTOList);

	List<UserDTO> toUserDTOList(List<UserModel> userModelList);
}
