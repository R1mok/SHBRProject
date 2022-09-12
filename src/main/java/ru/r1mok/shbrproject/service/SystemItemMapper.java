package ru.r1mok.shbrproject.service;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.r1mok.shbrproject.controllers.entities.SystemItemDTO;
import ru.r1mok.shbrproject.repository.entity.SystemItem;
import ru.r1mok.shbrproject.repository.entity.SystemItemType;

import java.util.ArrayList;
import java.util.List;

@Mapper(imports = SystemItemType.class, componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SystemItemMapper {
    @Mapping(target = "parentId", expression =
            "java(systemItem.getParent() != null ? systemItem.getParent().getId() : null)")
    default SystemItemDTO entityToDTO(SystemItem systemItem) {
        SystemItemDTO ret = SystemItemDTO.builder()
                .url(systemItem.getUrl())
                .type(systemItem.getType())
                .id(systemItem.getId())
                .size(systemItem.getSize())
                .date(systemItem.getDateTime())
                .parentId(systemItem.getParent() != null ? systemItem.getParent().getId() : null)
                .build();
        if (systemItem.getChildren().size() > 0) {
            List<SystemItemDTO> itemDTO = new ArrayList<>();
            for (SystemItem elem : systemItem.getChildren()) {
                itemDTO.add(entityToDTO(elem));
            }
            ret.setChildren(itemDTO);
        } else {
            if (systemItem.getType().equals(SystemItemType.FILE))
                ret.setChildren(null);
            else ret.setChildren(new ArrayList<>());
        }
        return ret;
    }
}
