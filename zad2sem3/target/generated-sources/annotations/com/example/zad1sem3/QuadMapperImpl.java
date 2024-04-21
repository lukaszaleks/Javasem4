package com.example.zad1sem3;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-21T10:24:22+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class QuadMapperImpl implements QuadMapper {

    @Override
    public Quad mapRequestToEntity(QuadCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        Quad quad = new Quad();

        quad.setBrand( request.getBrand() );
        quad.setModel( request.getModel() );
        quad.setYear( request.getYear() );

        return quad;
    }

    @Override
    public QuadResponse mapEntityToResponse(Quad entity) {
        if ( entity == null ) {
            return null;
        }

        QuadResponse quadResponse = new QuadResponse();

        quadResponse.setQuadId( entity.getQuadId() );
        quadResponse.setBrand( entity.getBrand() );
        quadResponse.setModel( entity.getModel() );
        quadResponse.setYear( entity.getYear() );

        return quadResponse;
    }

    @Override
    public Quad update(QuadCreateRequest request, Quad quad) {
        if ( request == null ) {
            return quad;
        }

        quad.setBrand( request.getBrand() );
        quad.setModel( request.getModel() );
        quad.setYear( request.getYear() );

        return quad;
    }
}
