package com.example.stu.Core.Service;

import com.example.stu.Core.Exceptions.ResourceNotFoundException;
import com.example.stu.Core.Models.ISoftDeleteAware;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
     * Check if an entity supports soft delete
     * @param entity Entity to check
     * @return true if the entity supports soft delete, false otherwise
     */
    protected boolean supportsSoftDelete(T entity) {
        return entity instanceof ISoftDeleteAware;
    }

    /**
     * Get all entities
     * @return List of all entities
     */
    public List<T> findAll() {
        List<T> result = new ArrayList<>();
        repository.findAll().forEach(result::add);

        if (!result.isEmpty() && supportsSoftDelete(result.get(0))) {
            return result.stream()
                    .filter(entity -> !((ISoftDeleteAware) entity).isDeleted())
                    .collect(Collectors.toList());
        }

        return result;
    }


    /**
     * Get entity by ID
     * @param id Entity ID
     * @return Entity
     * @throws ResourceNotFoundException if entity not found or is soft-deleted
     */
    public T findById(ID id) {
        T entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("not_found_data"));
        
        // Check if entity is soft-deleted
        if (supportsSoftDelete(entity) && ((ISoftDeleteAware) entity).isDeleted()) {
            throw new ResourceNotFoundException("not_found_data");
        }
        
        return entity;
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
        // Use soft delete if entity supports it
        if (supportsSoftDelete(entity)) {
            // Simply set isDeleted = true and save
            ((ISoftDeleteAware) entity).markAsDeleted();
            repository.save(entity);
        } else {
            // Use hard delete for entities that don't support soft delete
            repository.delete(entity);
        }
    }

    /**
     * Delete entity by ID
     * @param id Entity ID
     * @throws ResourceNotFoundException if entity not found
     */
    public void deleteById(ID id) {
        T entity = findById(id);
        delete(entity);
    }


    /**
     * Restore soft-deleted entity
     * @param entity Entity to restore
     * @return Restored entity
     * @throws IllegalArgumentException if entity does not support soft delete
     */
    public T restore(T entity) {
        if (supportsSoftDelete(entity)) {
            ((ISoftDeleteAware) entity).restore();
            return repository.save(entity);
        } else {
            throw new IllegalArgumentException("Entity_does_not_support_soft_delete");
        }
    }

}