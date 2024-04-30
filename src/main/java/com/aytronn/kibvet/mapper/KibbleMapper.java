package com.aytronn.kibvet.mapper;

import com.aytronn.kibvet.dao.Kibble;
import com.aytronn.kibvet.dto.CreateKibbleDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface KibbleMapper {

    @Mapping(target = "food", ignore = true)
    @Mapping(target = "id", ignore = true)
    Kibble toKibble(CreateKibbleDto createKibbleDto);
}
