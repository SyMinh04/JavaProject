package com.example.stu.Core.Controller;

import com.example.stu.Core.Response.ApiResponse;
import com.example.stu.Core.Service.BaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Base controller class that provides common CRUD endpoints
 * @param <T> Entity type
 * @param <ID> ID type
 * @param <S> Service type
 */
public abstract class BaseController<T, ID, S extends BaseService<T, ID, ?>> {
    
    protected final S service;
    
    protected BaseController(S service) {
        this.service = service;
    }
    
    /**
     * Get all entities
     * @return List of all entities
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<T>>> getAll() {
        List<T> entities = service.findAll();
        return ResponseEntity.ok(ApiResponse.success(entities, "Retrieved all " + getEntityName() + "s successfully"));
    }
    
    /**
     * Get entity by ID
     * @param id Entity ID
     * @return Entity
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<T>> getById(@PathVariable ID id) {
        T entity = service.findById(id);
        return ResponseEntity.ok(ApiResponse.success(entity, getEntityName() + " retrieved successfully"));
    }
    
    /**
     * Delete entity by ID
     * @param id Entity ID
     * @return Success response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable ID id) {
        service.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success(null, getEntityName() + " deleted successfully"));
    }
    
    /**
     * Get entity name for messages
     * @return Entity name
     */
    protected abstract String getEntityName();
}