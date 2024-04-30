package com.aytronn.kibvet.mapper;

import com.aytronn.kibvet.dao.Food;
import com.aytronn.kibvet.dto.FoodCreateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FoodMapper {

    @Mapping(target = "id", ignore = true)
    Food toFood(FoodCreateDto foodCreateDto);
}
