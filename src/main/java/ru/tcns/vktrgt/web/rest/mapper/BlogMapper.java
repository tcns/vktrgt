package ru.tcns.vktrgt.web.rest.mapper;

import ru.tcns.vktrgt.domain.*;
import ru.tcns.vktrgt.web.rest.dto.BlogDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Blog and its DTO BlogDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BlogMapper {

    BlogDTO blogToBlogDTO(Blog blog);

    Blog blogDTOToBlog(BlogDTO blogDTO);
}
