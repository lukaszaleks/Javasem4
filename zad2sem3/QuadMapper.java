package com.example.zad1sem3;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface QuadMapper {

    @Mapping(target = "quadId", ignore = true)
    Quad mapRequestToEntity(QuadCreateRequest request);

    QuadResponse mapEntityToResponse(Quad entity);


    @Mapping(target = "quadId", ignore = true)
    Quad update(QuadCreateRequest request, @MappingTarget Quad quad);
}
