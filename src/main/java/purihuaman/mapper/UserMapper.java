package purihuaman.mapper;

import org.mapstruct.Mapper;
import purihuaman.dto.UserDTO;
import purihuaman.entity.UserEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
	UserEntity toUserModel(UserDTO userDTO);

	UserDTO toUserDTO(UserEntity userEntity) ;

	List<UserEntity> toUserModelList(List<UserDTO> userDTOList);

	List<UserDTO> toUserDTOList(List<UserEntity> userEntityModelList);
}
