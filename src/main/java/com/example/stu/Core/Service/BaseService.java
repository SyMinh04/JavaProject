package com.example.stu.Core.Service;

import com.example.stu.Core.Exception.ResourceNotFoundException;
import com.example.stu.Core.Utils.ObjectMapper;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Base service class that provides common CRUD operations
 * @param <T> Entity type
 * @param <ID> ID type
 * @param <R> Repository type
 */
public abstract class BaseService<T, ID, R extends CrudRepository<T, ID>> {
    
    protected final R repository;
    
    protected BaseService(R repository) {
        this.repository = repository;
    }
    
    /**
     * Get all entities
     * @return List of all entities
     */
    public List<T> findAll() {
        List<T> result = new ArrayList<>();
        repository.findAll().forEach(result::add);
        return result;
    }
    
    /**
     * Get entity by ID
     * @param id Entity ID
     * @return Entity
     * @throws ResourceNotFoundException if entity not found
     */
    public T findById(ID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getEntityName() + " not found with id: " + id));
    }
    
    /**
     * Get entity by ID
     * @param id Entity ID
     * @return Optional entity
     */
    public Optional<T> findByIdOptional(ID id) {
        return repository.findById(id);
    }
    
    /**
     * Save entity
     * @param entity Entity to save
     * @return Saved entity
     */
    public T save(T entity) {
        return repository.save(entity);
    }
    
    /**
     * Delete entity
     * @param entity Entity to delete
     */
    public void delete(T entity) {
        repository.delete(entity);
    }
    
    /**
     * Delete entity by ID
     * @param id Entity ID
     * @throws ResourceNotFoundException if entity not found
     */
    public void deleteById(ID id) {
        T entity = findById(id);
        repository.delete(entity);
    }
    
    /**
     * Create entity from request
     * @param request Request object
     * @param entityClass Entity class
     * @return Created entity
     */
    public <REQ, RES> RES createFromRequest(REQ request, Class<RES> entityClass) {
        RES entity = ObjectMapper.map(request, entityClass);
        return (RES) repository.save((T) entity);
    }
    
    /**
     * Update entity from request
     * @param id Entity ID
     * @param request Request object
     * @return Updated entity
     * @throws ResourceNotFoundException if entity not found
     */
    public <REQ> T updateFromRequest(ID id, REQ request) {
        T existingEntity = findById(id);
        ObjectMapper.mapNonNullProperties(request, existingEntity);
        return repository.save(existingEntity);
    }
    
    /**
     * Get entity name for error messages
     * @return Entity name
     */
    protected abstract String getEntityName();
}