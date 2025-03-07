package purihuaman.mapper;

import org.mapstruct.Mapper;
import purihuaman.dto.RoleDTO;
import purihuaman.entity.RoleEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {
	RoleEntity toRoleModel(RoleDTO role);

	RoleDTO toRoleDTO(RoleEntity roleEntity);

	List<RoleEntity> toRoleModelList(List<RoleDTO> roles);

	List<RoleDTO> toRoleDTOList(List<RoleEntity> roleEntities);
}
