package com.example.stu.Core.Mapper;

/**
 * Interface for mapping between request objects and entity objects
 * @param <E> Entity type
 * @param <C> Create request type
 * @param <U> Update request type
 */
public interface EntityMapper<E, C, U> {
    
    /**
     * Map create request to entity
     * @param request Create request
     * @return Entity
     */
    E toEntity(C request);
    
    /**
     * Map update request to entity
     * @param entity Existing entity
     * @param request Update request
     * @return Updated entity
     */
    E updateEntity(E entity, U request);
    
    /**
     * Map entity to response
     * @param entity Entity
     * @param responseClass Response class
     * @return Response object
     */
    <R> R toResponse(E entity, Class<R> responseClass);
}