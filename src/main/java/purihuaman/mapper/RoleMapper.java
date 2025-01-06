package purihuaman.mapper;

import org.mapstruct.Mapper;
import purihuaman.dto.RoleDTO;
import purihuaman.model.RoleModel;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {
	RoleModel toRoleModel(RoleDTO role);

	RoleDTO toRoleDTO(RoleModel role);

	List<RoleModel> toRoleModelList(List<RoleDTO> roles);

	List<RoleDTO> toRoleDTOList(List<RoleModel> roles);
}
