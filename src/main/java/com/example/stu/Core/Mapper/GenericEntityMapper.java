package com.example.stu.Core.Mapper;

import com.example.stu.Core.Utils.ObjectMapper;

/**
 * Generic implementation of EntityMapper
 * @param <E> Entity type
 * @param <C> Create request type
 * @param <U> Update request type
 */
public class GenericEntityMapper<E, C, U> implements EntityMapper<E, C, U> {
    
    private final Class<E> entityClass;
    
    public GenericEntityMapper(Class<E> entityClass) {
        this.entityClass = entityClass;
    }
    
    @Override
    public E toEntity(C request) {
        try {
            E entity = entityClass.getDeclaredConstructor().newInstance();
            ObjectMapper.map(request, entity);
            return entity;
        } catch (Exception e) {
            throw new RuntimeException("Error creating entity from request", e);
        }
    }
    
    @Override
    public E updateEntity(E entity, U request) {
        ObjectMapper.mapNonNullProperties(request, entity);
        return entity;
    }
    
    @Override
    public <R> R toResponse(E entity, Class<R> responseClass) {
        return ObjectMapper.mapToSnakeCase(entity, responseClass);
    }
}